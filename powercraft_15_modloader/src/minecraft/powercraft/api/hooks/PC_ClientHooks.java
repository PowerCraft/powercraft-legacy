package powercraft.api.hooks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import net.minecraft.src.mod_PowerCraft;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.renderer.PC_OverlayRenderer;
import powercraft.api.utils.PC_ClientUtils;
import powercraft.api.utils.PC_GlobalVariables;
import powercraft.launcher.PC_Logger;

public class PC_ClientHooks {

	private static boolean started;
	private static GuiScreen lastGuiScreen;
	private static Random rand = new Random();
	
	public static void registerClientHooks() {
		registerMinecraftSaverHook();
	}

	private static void registerMinecraftSaverHook() {
		Minecraft mc = PC_ClientUtils.mc();
		PC_ReflectHelper.setValue(Minecraft.class, mc, 44,
				new PC_HackedSaveConverter(new File(mc.mcDataDir, "saves")),
				ISaveFormat.class);
	}

	private static List<String> getBrandings() {
		int modsLoaded = ModLoader.getLoadedMods().size() + 1;
		int modsActive = modsLoaded;
		List<String> brandings = new ArrayList<String>();
		brandings.add(ModLoader.VERSION);
		brandings.add("PowerCraft " + mod_PowerCraft.getInstance().getVersion());
		brandings.add(modsLoaded + " mods loaded, " + modsActive + " mods active");
		return brandings;
	}
	
	private static String getRandomSplash() {
		return PC_GlobalVariables.splashes.get(rand.nextInt(PC_GlobalVariables.splashes.size()));
	}
	
	
	private static void guiChangeHook(Minecraft mc, GuiScreen guiScreen){
		if(guiScreen instanceof GuiMainMenu){
			mc.displayGuiScreen(new PC_GuiMainMenuHook(getBrandings()));
			if (PC_GlobalVariables.hackSplashes){
				PC_Logger.finest("Hacking main menu splashes");
				if (rand.nextInt(2) == 0) {
					try {
						PC_ReflectHelper.setValue(GuiMainMenu.class, guiScreen, 2, getRandomSplash(), String.class);
					} catch (Throwable t) {}
				}
			}
		}else if(guiScreen instanceof GuiOptions){
			if (!(guiScreen instanceof PC_GuiOptionsHook)) {
				GuiScreen parentScreen = PC_ReflectHelper.getValue(GuiOptions.class, guiScreen, 1, GuiScreen.class);
				GameSettings options = PC_ReflectHelper.getValue(GuiOptions.class, guiScreen, 2, GameSettings.class);
				mc.displayGuiScreen(new PC_GuiOptionsHook(parentScreen, options));
			}
		}
	}
	
	private static void startupHook(Minecraft mc){
		String useUserName = PC_GlobalVariables.useUserName;
		if(!useUserName.equals("")){
			PC_ClientUtils.mc().session.username = useUserName;
		}
		mc.ingameGUI = new PC_OverlayRenderer(mc);
	}
	
	private static void everyTickHook(Minecraft mc){
		MinecraftServer mcs = mc.getIntegratedServer();
		if (mcs != null) {
			if (!(PC_ReflectHelper.getValue(MinecraftServer.class, mcs, 1, ISaveFormat.class) instanceof PC_HackedSaveConverter)) {
				File file = PC_ReflectHelper.getValue(MinecraftServer.class, mcs, 3, File.class);
				PC_HackedSaveConverter saveConverter = new PC_HackedSaveConverter(file);
				PC_ReflectHelper.setValue(MinecraftServer.class, mcs, 1, saveConverter, ISaveFormat.class);
				ISaveHandler saveHandler = saveConverter.getSaveLoader(mcs.getFolderName(), true);
				for (World world : mcs.worldServers) {
					PC_ReflectHelper.setValue(World.class, world, 22, saveHandler, ISaveHandler.class);
				}
			}
		}
	}
	
	public void tickStart() {
		Minecraft mc = PC_ClientUtils.mc();
		if(mc.currentScreen!=lastGuiScreen){
			guiChangeHook(mc, mc.currentScreen);
			lastGuiScreen = mc.currentScreen;
		}
		if(!started){
			startupHook(mc);
			started = true;
		}
		everyTickHook(mc);
	}

}
