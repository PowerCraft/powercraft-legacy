package powercraft.api.hooks;

import java.io.File;
import java.util.EnumSet;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.renderer.PC_OverlayRenderer;
import powercraft.api.utils.PC_ClientUtils;
import powercraft.api.utils.PC_GlobalVariables;
import powercraft.launcher.PC_Logger;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class PC_ClientHooks implements ITickHandler {

	private static boolean started;
	private static GuiScreen lastGuiScreen;
	private static Random rand = new Random();
	
	public static void registerClientHooks(){
		registerMinecraftSaverHook();
	}
	
	private static void registerMinecraftSaverHook(){
		Minecraft mc = PC_ClientUtils.mc();
		PC_ReflectHelper.setValue(Minecraft.class, mc, 44, new PC_SaveConverterHook(new File(mc.mcDataDir, "saves")), ISaveFormat.class);
	}

	private static String getRandomSplash() {
		return PC_GlobalVariables.splashes.get(rand.nextInt(PC_GlobalVariables.splashes.size()));
	}
	
	private static void guiChangeHook(Minecraft mc, GuiScreen guiScreen){
		if(guiScreen instanceof GuiMainMenu){
			if (PC_GlobalVariables.hackSplashes){
				PC_Logger.finest("Hacking main menu splashes");
				if (rand.nextInt(2) == 0) {
					try {
						PC_ReflectHelper.setValue(GuiMainMenu.class, guiScreen, 2, getRandomSplash(), String.class);
					} catch (Throwable t) {}
				}
			}
		}
	}
	
	private static void startupHook(Minecraft mc){
		String useUserName = PC_GlobalVariables.useUserName;
		if(!useUserName.equals("")){
			PC_ReflectHelper.setValue(EntityPlayer.class, PC_ClientUtils.mc().thePlayer, 10, useUserName);
		}
		mc.ingameGUI = new PC_OverlayRenderer(mc);
	}
	
	private static void everyTickHook(Minecraft mc){
		MinecraftServer mcs = mc.getIntegratedServer();
		if (mcs != null) {
			if (!(PC_ReflectHelper.getValue(MinecraftServer.class, mcs, 1, ISaveFormat.class) instanceof PC_SaveConverterHook)) {
				File file = PC_ReflectHelper.getValue(MinecraftServer.class, mcs, 3, File.class);
				PC_SaveConverterHook saveConverter = new PC_SaveConverterHook(file);
				PC_ReflectHelper.setValue(MinecraftServer.class, mcs, 1, saveConverter, ISaveFormat.class);
				ISaveHandler saveHandler = saveConverter.getSaveLoader(mcs.getFolderName(), true);
				for (World world : mcs.worldServers) {
					PC_ReflectHelper.setValue(World.class, world, 24, saveHandler, ISaveHandler.class);
				}
			}
		}
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
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

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "PC_Hooks";
	}
	
}
