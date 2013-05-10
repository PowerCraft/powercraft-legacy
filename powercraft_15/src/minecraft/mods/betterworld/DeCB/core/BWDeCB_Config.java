package mods.betterworld.DeCB.core;

import java.io.File;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;

import mods.betterworld.CB.BWCB;
import mods.betterworld.DeCB.BWDeCB;
import net.minecraftforge.common.Configuration;

public class BWDeCB_Config {
	
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
		
			BWDeCB.textureRes = config.get(Configuration.CATEGORY_GENERAL, "Texture Resolution", 128).getInt();
			// Fence & Wall
			BWDeCB.blockFenceWoodID = config.getBlock("Wood Fence", 1100).getInt();
			BWDeCB.blockFenceWoodGateID = config.getBlock("Gate Wood", 1101).getInt();
			BWDeCB.blockWallStoneID = config.getBlock("Wall Stone", 1102).getInt();
			BWDeCB.blockColumnSquareID = config.getBlock("Column Square", 1103).getInt();
			BWDeCB.blockColumnRoundID = config.getBlock("Column Round", 1104).getInt();
			
		} catch (Exception excep) {
			FMLLog.log(Level.SEVERE, excep,
					"The Mod BetterWorld CB has problems to load the Configfile");
		} finally {

			config.save();
		}
	}

}
