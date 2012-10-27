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
	
	private static String[] splashes = {
		// our features
		"Weasel is fast!",
		"Weasel powered!",
		"Weasel rocks!",
		"Weasel FTW!",
		"Touch it!",
		"Sniffing diamonds!",
		"Power of the storm!",
		"Hacked water!",
		"Hacked splashes!",
		"Miner! Oh boy!",
		"Using Weasel",
		"GRES",
		"Transmutation!",
		// our credits
		"Modded by MightyPork!",
		"Modded by MightyPork!",
		"Modded by MightyPork!",
		"Modded by MightyPork!",
		"Modded by MightyPork!",
		"Modded by MightyPork!",
		"Modded by MightyPork!",
		"Modded by MightyPork!",
		"Modded by MightyPork!",
		"Modded by MightyPork!",
		"Modded by XOR19!",
		"Modded by XOR19!",
		"Modded by XOR19!",
		"Modded by XOR19!",
		"Modded by XOR19!",
		"Modded by XOR19!",
		"Modded by Rapus!",
		"Modded by Rapus!",
		"Modded by Rapus!",
		"Modded by Rapus!",
		"Modded by Rapus!",
		"Reviewed by RxD!",
		"Reviewed by RxD!",
		"Reviewed by RxD!",
		"Reviewed by RxD!",
		"Modded by masters!",
		// the mod name
		"PowerCraft "+mod_PowerCraftCore.getInstance().getVersion(),
		"PowerCraft "+mod_PowerCraftCore.getInstance().getVersion(),
		"PowerCraft "+mod_PowerCraftCore.getInstance().getVersion(),
		// making fun of crashes
		"Null Pointers included!",
		"ArrayIndexOutOfBoundsException",
		"Null Pointer loves you!",
		"Work in progress!",
		"Unstable!",
		"Buggy code!",
		//random shouts
		"Break it down!",
		"Addictive!",
		"Earth is flat!",
		"Faster than Atari!",
		"DAFUQ??",
		"LWJGL",
		"Don't press the button!",
		"Press the button!",
		"Ssssssssssssssss!",
		"C'mon!",
		"Redstone Wizzard!",
		// advice
		"Keep your mods up-to-date!",
		"Read the changelog!",
		"Read the log files!",
		// also try
		"Discoworld!",
		"Also try ICE AGE mod!",
		"Also try Backpack mod!",
		// pieces of code
		"while(true){}",
		"GOSUB"
	};
	
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
		if (rand.nextInt(4) == 0) {
			try {
				PC_Utils.setPrivateValue(GuiMainMenu.class, gui, 2, splashes[rand.nextInt(splashes.length)]);
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
