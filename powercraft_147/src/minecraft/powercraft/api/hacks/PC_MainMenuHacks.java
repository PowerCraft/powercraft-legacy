package powercraft.api.hacks;

import java.io.File;
import java.util.EnumSet;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import powercraft.launcher.PC_Logger;
import powercraft.api.PC_ClientUtils;
import powercraft.api.PC_GlobalVariables;
import powercraft.api.PC_OverlayRenderer;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.registry.PC_GresRegistry;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class PC_MainMenuHacks implements ITickHandler {

	private static GuiMainMenu lastHacked = null;
	private static boolean usernameHacked = false;
	private static boolean ingameGuiHacked = false;
	private static Random rand = new Random();
	
	/**
	 * Hack splash text of a main screen. Every 4th time a screen is displayed,
	 * the PC text will be used.<br>
	 * It also shows update notifications when new version is released, every
	 * 2nd time.
	 * 
	 * @param gui
	 */
	public static void hackSplashes(GuiMainMenu gui) {
		if (gui == lastHacked) return;
		lastHacked = gui;
		PC_Logger.finest("Hacking main menu splashes");
		if (rand.nextInt(2) == 0) {
			try {
				PC_ReflectHelper.setValue(GuiMainMenu.class, gui, 2, getRandomSplash(), String.class);
			} catch (Throwable t) {}
		}
	}
	
	
	
	private static String getRandomSplash() {
		return PC_GlobalVariables.splashes.get(rand.nextInt(PC_GlobalVariables.splashes.size()));
	}



	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		Minecraft mc = PC_ClientUtils.mc();
		GuiScreen gs = mc.currentScreen;
		if(gs instanceof GuiMainMenu){
			if(PC_GlobalVariables.hackSplashes)
				hackSplashes((GuiMainMenu)gs);
		}
		if(!(usernameHacked)){
			String useUserName = PC_GlobalVariables.useUserName;
			if(!useUserName.equals("")){
				PC_ClientUtils.mc().session.username = useUserName;
			}
			usernameHacked = true;
		}
		if(!ingameGuiHacked){
			mc.ingameGUI = new PC_OverlayRenderer(mc);
			ingameGuiHacked = true;
		}
		MinecraftServer mcs = mc.getIntegratedServer();
		if(mcs!=null){
			if(!(PC_ReflectHelper.getValue(MinecraftServer.class, mcs, 2, ISaveFormat.class) instanceof PC_HackedSaveConverter)){
				File file = PC_ReflectHelper.getValue(MinecraftServer.class, mcs, 4, File.class);
				PC_HackedSaveConverter saveConverter = new PC_HackedSaveConverter(file); 
				PC_ReflectHelper.setValue(MinecraftServer.class, mcs, 2, saveConverter, ISaveFormat.class);
				ISaveHandler saveHandler = saveConverter.getSaveLoader(mcs.getFolderName(), true);
				for(World world:mcs.worldServers){
					PC_ReflectHelper.setValue(World.class, world, 25, saveHandler, ISaveHandler.class);
				}
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "PC_MainMenuHacks";
	}

}
