package powercraft.core;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class PCco_MainMenuHacks implements ITickHandler {
	
	private static GuiMainMenu lastHacked = null;
	private static boolean updateWindowShowed = false;
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
				PC_Utils.setPrivateValue(GuiMainMenu.class, gui, 2, PC_Module.getRandomSplash(rand));
			} catch (Throwable t) {}
		}
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		Minecraft mc = PC_ClientUtils.mc();
		GuiScreen gs = mc.currentScreen;
		if(gs instanceof GuiMainMenu){
			if(mod_PowerCraftCore.hackSplashes)
				hackSplashes((GuiMainMenu)gs);
			if(PC_Module.isUpdateAvailable() && mod_PowerCraftCore.showUpdateWindow && !updateWindowShowed){
				PC_Utils.openGres("UpdateNotification", null, gs);
				updateWindowShowed = true;
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
		return "PCco_MainMenuHacks";
	}

}
