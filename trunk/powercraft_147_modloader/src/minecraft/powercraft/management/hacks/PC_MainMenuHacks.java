package powercraft.management.hacks;

import java.io.File;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.SaveHandler;
import net.minecraft.src.World;
import net.minecraft.src.mod_PowerCraft;
import powercraft.management.PC_ClientUtils;
import powercraft.management.PC_GlobalVariables;
import powercraft.management.PC_Logger;
import powercraft.management.PC_OverlayRenderer;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.Gres;
import powercraft.management.PC_Utils.ValueWriting;

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
				ValueWriting.setPrivateValue(GuiMainMenu.class, gui, 2, getRandomSplash());
			} catch (Throwable t) {}
		}
	}
	
	
	
	private static String getRandomSplash() {
		return PC_GlobalVariables.splashes.get(rand.nextInt(PC_GlobalVariables.splashes.size()));
	}

	public void tickStart() {
		Minecraft mc = PC_ClientUtils.mc();
		GuiScreen gs = mc.currentScreen;
		if(gs instanceof GuiMainMenu){
			gs.drawString(PC_ClientUtils.mc().fontRenderer, "PowerCraft " + mod_PowerCraft.getInstance().getVersion(), 2 , 
					gs.height - 10 - PC_ClientUtils.mc().fontRenderer.FONT_HEIGHT, 0xffffffff);
		}
		if(gs!=lastHacked){
			if(gs instanceof GuiMainMenu){
				if(PC_GlobalVariables.hackSplashes)
					hackSplashes((GuiMainMenu)gs);
				if(PC_GlobalVariables.showUpdateWindow && !updateWindowShowed){
					Gres.openGres("UpdateNotification", null, null, gs);
					updateWindowShowed = true;
				}
				
			}else if(gs instanceof GuiContainer){
				ValueWriting.setPrivateValue(GuiContainer.class, gs, 0, new PC_RenderItemHack());
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
			ingameGuiHacked = true;
		}
		MinecraftServer mcs = mc.getIntegratedServer();
		if(mcs!=null){
			if(!(ValueWriting.getPrivateValue(MinecraftServer.class, mcs, 2) instanceof PC_HackedSaveConverter)){
				File file = (File)ValueWriting.getPrivateValue(MinecraftServer.class, mcs, 4);
				PC_HackedSaveConverter saveConverter = new PC_HackedSaveConverter(file); 
				ValueWriting.setPrivateValue(MinecraftServer.class, mcs, 2, saveConverter);
				ISaveHandler saveHandler = saveConverter.getSaveLoader(mcs.getFolderName(), true);
				for(World world:mcs.worldServers){
					ValueWriting.setPrivateValue(World.class, world, 23, saveHandler);
				}
			}
		}
	}

}
