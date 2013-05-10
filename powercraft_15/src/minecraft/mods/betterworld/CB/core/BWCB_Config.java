package mods.betterworld.CB.core;

import java.io.File;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;

import mods.betterworld.CB.BWCB;
import net.minecraftforge.common.Configuration;

public class BWCB_Config {
	
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
		
			BWCB.textureRes = config.get(Configuration.CATEGORY_GENERAL, "Texture Resolution", 128).getInt();
			// Block
			BWCB.blockStoneID = config.getBlock("Stone Blocks", 1000).getInt();
			BWCB.blockStoneRID = config.getBlock("Resistant Stone Blocks", 1001).getInt();
			BWCB.blockWoodID = config.getBlock("Wood Blocks", 1002).getInt();
			BWCB.blockWoodRID = config.getBlock("Resistant Wood Blocks", 1003).getInt();
			BWCB.blockGlassID = config.getBlock("Glass Blocks", 1004).getInt();
			BWCB.blockGlassRID = config.getBlock("ResisteBlocks", 1005).getInt();
			// StairBlocks
			BWCB.blockStairStoneID = config.getBlock("Stone Stairs", 1006).getInt();
			BWCB.blockStairStoneRID = config.getBlock("Resistant Stone Stairs", 1007).getInt();
			BWCB.blockStairWoodID = config.getBlock("Wood Stairs", 1008).getInt();
			BWCB.blockStairWoodRID = config.getBlock("Resistant Wood Stairs", 1009).getInt();
			BWCB.blockStairGlassID = config.getBlock("Glass Stairs", 1010).getInt();
			BWCB.blockStairGlassRID = config.getBlock("Resistant Glass Stairs", 1011).getInt();
			// BlockSlabs
			BWCB.blockSlabStoneID = config.getBlock("Stone Slabs", 1012).getInt();
			BWCB.blockDoubleSlabStoneID = config.getBlock("Stone double Slabs", 1013).getInt();
			BWCB.blockSlabStoneRID = config.getBlock("Resistant Stone Slabs", 1014).getInt();
			BWCB.blockDoubleSlabStoneRID = config.getBlock("Resistant Stone double Slabs", 1015).getInt();
			BWCB.blockSlabWoodID = config.getBlock("Wood Slabs", 1016).getInt();
			BWCB.blockDoubleSlabWoodID = config.getBlock("Wood double Slabs", 1017).getInt();
			BWCB.blockSlabWoodRID = config.getBlock("Resistant Wood Slabs", 1018).getInt();
			BWCB.blockDoubleSlabWoodRID = config.getBlock("Resistant Wood double Slabs", 1019).getInt();
			BWCB.blockSlabGlassID = config.getBlock("Glass Slabs", 1020).getInt();
			BWCB.blockDoubleSlabGlassID = config.getBlock("Glass double Slabs", 1021).getInt();
			BWCB.blockSlabGlassRID = config.getBlock("Resistant Glass Slabs", 1022).getInt();
			BWCB.blockDoubleSlabGlassRID = config.getBlock("Resistant Glass double Slabs", 1023).getInt();
			// Machines
			BWCB.blockMachineBrickID = config.getBlock("Brick Machine Base", 1024).getInt();
			BWCB.blockMachineObsidianID = config.getBlock("Obsidian Machine Base", 1025).getInt();
			
	
		} catch (Exception excep) {
			FMLLog.log(Level.SEVERE, excep,
					"The Mod BetterWorld CB has problems to load the Configfile");
		} finally {

			config.save();
		}
	}

}
