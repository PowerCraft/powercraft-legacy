package net.minecraft.src;


import java.util.Random;


/**
 * Class which helps to hack splash texts in the main menu.
 * 
 * @author MightyPork
 */
public class PCco_SplashHelper {

	private static GuiMainMenu lastHacked = null;

	//@formatter:off
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
		"PowerCraft "+mod_PCcore.VERSION,
		"PowerCraft "+mod_PCcore.VERSION,
		"PowerCraft "+mod_PCcore.VERSION,
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
	//@formatter:on

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
		if (mod_PCcore.updateAvailable && rand.nextInt(2) == 0) {
			try {
				ModLoader.setPrivateValue(GuiMainMenu.class, gui, 2, PC_Lang.tr("pc.splash.newPowerCraftAvailable"));
			} catch (Throwable t) {}
		} else if (rand.nextInt(4) == 0) {
			try {
				ModLoader.setPrivateValue(GuiMainMenu.class, gui, 2, splashes[rand.nextInt(splashes.length)]);
			} catch (Throwable t) {}
		}
	}

}
