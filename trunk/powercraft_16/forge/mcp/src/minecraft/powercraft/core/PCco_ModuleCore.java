package powercraft.core;

import powercraft.api.PC_Api;
import powercraft.api.PC_FieldGenerator;
import powercraft.api.PC_Module;
import powercraft.api.energy.PC_ConduitEnergyItem;
import powercraft.api.multiblocks.PC_BlockMultiblock;
import powercraft.api.multiblocks.cable.redstone.PC_RedstoneIsolatedItem;
import powercraft.api.multiblocks.cable.redstone.PC_RedstoneUnisolatedItem;
import powercraft.api.multiblocks.covers.PC_CoverItem;
import powercraft.core.blocks.PC_BlockGenerator;
import powercraft.core.blocks.PC_BlockPuffer;
import powercraft.core.blocks.PC_BlockRoaster;
import powercraft.core.items.PC_ItemSaw;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * IDs:<br>
 * 3001 {@link PC_BlockGenerator}<br>
 * 3002 {@link PC_BlockPuffer}<br>
 * 3003 {@link PC_BlockRoaster}<br>
 */

@Mod(modid = PCco_ModuleCore.NAME, name = PCco_ModuleCore.NAME, version=PCco_ModuleCore.VERSION, dependencies=PCco_ModuleCore.DEPENDENCIES)
public class PCco_ModuleCore extends PC_Module {

	public static final String NAME = POWERCRAFT+"-Core";
	public static final String VERSION = "1.0.0";
	public static final String DEPENDENCIES = "required-after:"+PC_Api.NAME+"@"+PC_Api.VERSION;
	
	@Instance(NAME)
	public static PCco_ModuleCore instance;

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
	
	@PC_FieldGenerator
	public PC_CoverItem coverItem;
	
	@PC_FieldGenerator
	public PC_ItemSaw saw;
	
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
