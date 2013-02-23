package powercraft.management.hacks;

import java.io.File;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.RenderManager;
import net.minecraft.src.World;
import powercraft.management.PC_ClientUtils;
import powercraft.management.PC_GlobalVariables;
import powercraft.management.PC_Logger;
import powercraft.management.PC_OverlayRenderer;
import powercraft.management.reflect.PC_ReflectHelper;
import powercraft.management.registry.PC_GresRegistry;

public class PC_MainMenuHacks {

	private static GuiScreen lastHacked = null;
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
		PC_Logger.finest("Hacking main menu splashes");
		if (rand.nextInt(2) == 0) {
			try {
				PC_ReflectHelper.setValue(GuiMainMenu.class, gui, 2, getRandomSplash());
			} catch (Throwable t) {}
		}
		lastHacked = gui;
	}
	
	
	
	private static String getRandomSplash() {
		return PC_GlobalVariables.splashes.get(rand.nextInt(PC_GlobalVariables.splashes.size()));
	}

	public void tickStart() {
		Minecraft mc = PC_ClientUtils.mc();
		GuiScreen gs = mc.currentScreen;
		if(gs instanceof GuiMainMenu){
			if(PC_GlobalVariables.showUpdateWindow && !updateWindowShowed){
				PC_GresRegistry.openGres("UpdateNotification", null, null, gs);
				updateWindowShowed = true;
			}
		}
		if(gs!=lastHacked){
			if(gs instanceof GuiMainMenu){
				if(!(gs instanceof PC_GuiMainMenuHack)){
					mc.displayGuiScreen(new PC_GuiMainMenuHack());
					gs = mc.currentScreen;
				}
				if(PC_GlobalVariables.hackSplashes)
					hackSplashes((GuiMainMenu)gs);
				
			}else if(gs instanceof GuiOptions){
				if(!(gs instanceof PC_GuiOptionsHack)){
					GuiScreen parentScreen = (GuiScreen)PC_ReflectHelper.getValue(GuiOptions.class, gs, 1);
					GameSettings options = (GameSettings)PC_ReflectHelper.getValue(GuiOptions.class, gs, 2);
					mc.displayGuiScreen(new PC_GuiOptionsHack(parentScreen, options));
					gs = mc.currentScreen;
				}
			}else if(gs instanceof GuiContainer){
				PC_ReflectHelper.setValue(GuiContainer.class, gs, 0, new PC_RenderItemHack());
			}
		}
		lastHacked = gs;
		if(!(usernameHacked)){
			String useUserName = PC_GlobalVariables.useUserName;
			if(!useUserName.equals("")){
				PC_ClientUtils.mc().session.username = useUserName;
			}
			usernameHacked = true;
		}
		if(!ingameGuiHacked){
			mc.ingameGUI = new PC_OverlayRenderer(mc);
			PC_ReflectHelper.setValue(GuiIngame.class, mc.ingameGUI, 0, new PC_RenderItemHack());
			RenderManager.instance.itemRenderer = new PC_ItemRendererHack(PC_ClientUtils.mc());
			PC_ClientUtils.mc().entityRenderer.itemRenderer = new PC_ItemRendererHack(PC_ClientUtils.mc());
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
					PC_ReflectHelper.setValue(World.class, world, 23, saveHandler);
				}
			}
		}
	}

}
