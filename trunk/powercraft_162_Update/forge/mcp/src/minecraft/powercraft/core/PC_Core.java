package powercraft.core;

import powercraft.api.PC_FieldGenerator;
import powercraft.api.PC_Module;
import powercraft.api.energy.PC_ConduitEnergyItem;
import powercraft.api.multiblock.PC_BlockMultiblock;
import powercraft.api.multiblock.cable.redstone.PC_RedstoneIsolatedItem;
import powercraft.api.multiblock.cable.redstone.PC_RedstoneUnisolatedItem;
import powercraft.core.block.PC_BlockGenerator;
import powercraft.core.block.PC_BlockPuffer;
import powercraft.core.block.PC_BlockRoaster;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "PowerCraft-Core", name = "PowerCraft-Core", version="1.0.0", dependencies="required-after:PowerCraft-Api@1.0.0")
public class PC_Core extends PC_Module {

	@Instance("PowerCraft-Core")
	public static PC_Core instance;

	@PC_FieldGenerator
	public PC_BlockMultiblock multiblock;
	
	@PC_FieldGenerator
	public PC_ConduitEnergyItem energyConduit;
	
	@PC_FieldGenerator
	public PC_BlockGenerator generator;
	
	@PC_FieldGenerator
	public PC_BlockPuffer puffer;
	
	@PC_FieldGenerator
	public PC_BlockRoaster roaster;
	
	@PC_FieldGenerator
	public PC_RedstoneUnisolatedItem unisolatedRedstone;
	
	@PC_FieldGenerator
	public PC_RedstoneIsolatedItem isolatedRedstone;
	
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
