package powercraft.tutorial;

import powercraft.api.PC_FiledGenerator;
import powercraft.api.PC_Module;
import powercraft.api.energy.PC_ConduitEnergyItem;
import powercraft.api.multiblocks.PC_BlockMultiblock;
import powercraft.api.multiblocks.cable.redstone.PC_RedstoneIsolatedItem;
import powercraft.api.multiblocks.cable.redstone.PC_RedstoneUnisolatedItem;
import powercraft.core.blocks.PC_BlockGenerator;
import powercraft.core.blocks.PC_BlockPuffer;
import powercraft.core.blocks.PC_BlockRoaster;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "PowerCraft-Tutorial", name = "PowerCraft-Tutorial", version="1.0.0", dependencies="required-after:PowerCraft-Api")
public class PC_Tutorial extends PC_Module {

	@Instance("PowerCraft-Tutorial")
	public static PC_Tutorial instance;

	@PC_FiledGenerator
	public static PC_BlockTutorial tutorial;
	
	@Override
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		defaultPreInit(event);
	}

	@Override
	@EventHandler
	public void init(FMLInitializationEvent event) {
		defaultInit(event);
	}

	@Override
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}

}
