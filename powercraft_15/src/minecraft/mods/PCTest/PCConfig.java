package mods.PCTest;

import java.io.File;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;

import mods.betterworld.CB.BWCB;
import mods.betterworld.DeCB.BWDeCB;
import net.minecraftforge.common.Configuration;

public class PCConfig {
	
	public static void init(File configFile) {
		Configuration config = new Configuration(configFile);

		// Config Start

		try {
			config.load();
			// Get the Resolution HiRes true/false
			// BetterWorldCoreBlocks.texturesHiRes =
			// config.getOrCreateBooleanProperty("HiRes Textures",
			// Configuration.CATEGORY_GENERAL, true).getBoolean(true);
	//		BetterWorldCoreBlocks.texturesRes(config.get(
	//				Configuration.CATEGORY_GENERAL, "Textures", "HiRes").value);
		
			PCTest.textureRes = config.get(Configuration.CATEGORY_GENERAL, "Texture Resolution", 128).getInt();
			// Fence & Wall
			PCTest.blockWireID = config.getBlock("RedStoneWire", 1400).getInt();
			PCTest.itemBlockWireID = config.getItem("RedWireItem", 3000).getInt();
			
		} catch (Exception excep) {
			FMLLog.log(Level.SEVERE, excep,
					"The Mod BetterWorld CB has problems to load the Configfile");
		} finally {

			config.save();
		}
	}

}