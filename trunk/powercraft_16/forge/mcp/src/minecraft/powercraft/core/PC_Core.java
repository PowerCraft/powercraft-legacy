package powercraft.core;

import powercraft.api.PC_FiledGenerator;
import powercraft.api.PC_Module;
import powercraft.api.energy.PC_ConduitEnergyItem;
import powercraft.api.multiblocks.PC_BlockMultiblock;
import powercraft.api.multiblocks.redstone.PC_RedstoneUnisolatedItem;
import powercraft.core.blocks.PC_BlockGenerator;
import powercraft.core.blocks.PC_BlockPuffer;
import powercraft.core.blocks.PC_BlockRoaster;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "PowerCraft-Core", name = "PowerCraft-Core", version="1.0.0", dependencies="required-after:PowerCraft-Api")
public class PC_Core extends PC_Module {

	@Instance("PowerCraft-Core")
	public static PC_Core instance;

	@PC_FiledGenerator
	public PC_BlockMultiblock multiblock;
	
	@PC_FiledGenerator
	public PC_ConduitEnergyItem energyConduit;
	
	@PC_FiledGenerator
	public PC_BlockGenerator generator;
	
	@PC_FiledGenerator
	public PC_BlockPuffer puffer;
	
	@PC_FiledGenerator
	public PC_BlockRoaster roaster;
	
	@PC_FiledGenerator
	public PC_RedstoneUnisolatedItem unisolatedRedstone;
	
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
