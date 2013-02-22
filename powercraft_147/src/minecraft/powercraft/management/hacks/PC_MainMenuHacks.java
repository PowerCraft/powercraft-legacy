package powercraft.management.hacks;

import java.io.File;
import java.util.EnumSet;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import powercraft.management.PC_ClientUtils;
import powercraft.management.PC_GlobalVariables;
import powercraft.management.PC_Logger;
import powercraft.management.PC_OverlayRenderer;
import powercraft.management.reflect.PC_ReflectHelper;
import powercraft.management.registry.PC_GresRegistry;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class PC_MainMenuHacks implements ITickHandler {

	private static GuiMainMenu lastHacked = null;
	private static boolean updateWindowShowed = false;
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
				PC_ReflectHelper.setValue(GuiMainMenu.class, gui, 2, getRandomSplash());
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
			if(PC_GlobalVariables.showUpdateWindow && !updateWindowShowed){
				PC_GresRegistry.openGres("UpdateNotification", null, null, gs);
				updateWindowShowed = true;
			}
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
			if(!(PC_ReflectHelper.getValue(MinecraftServer.class, mcs, 2) instanceof PC_HackedSaveConverter)){
				File file = (File)PC_ReflectHelper.getValue(MinecraftServer.class, mcs, 4);
				PC_HackedSaveConverter saveConverter = new PC_HackedSaveConverter(file); 
				PC_ReflectHelper.setValue(MinecraftServer.class, mcs, 2, saveConverter);
				ISaveHandler saveHandler = saveConverter.getSaveLoader(mcs.getFolderName(), true);
				for(World world:mcs.worldServers){
					PC_ReflectHelper.setValue(World.class, world, 25, saveHandler);
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
