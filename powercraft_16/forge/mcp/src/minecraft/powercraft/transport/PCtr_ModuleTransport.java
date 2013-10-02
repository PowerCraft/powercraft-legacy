package powercraft.transport;

import powercraft.api.PC_FieldGenerator;
import powercraft.api.PC_Module;
import powercraft.transport.blocks.blocks.PCtr_BlockClassicElevator;
import powercraft.transport.blocks.blocks.PCtr_BlockClassicElevatorDown;
import powercraft.transport.blocks.blocks.PCtr_BlockPlate;
import powercraft.transport.blocks.blocks.PCtr_BlockPrimitivePlate;
import powercraft.transport.blocks.blocks.PCtr_BlockUpgradeableBelt;
import powercraft.transport.helper.PC_EntityDictionary;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "PowerCraft-Transport", name = "PowerCraft-Transport", version="1.0.0", dependencies="required-after:PowerCraft-Api")
public class PCtr_ModuleTransport extends PC_Module
{
	
	@Instance("PowerCraft-Transport")
	public static PCtr_ModuleTransport instance;

	@PC_FieldGenerator
	public static PCtr_BlockPrimitivePlate primitivePlate;
	
	@PC_FieldGenerator	
	public static PCtr_BlockClassicElevator elevatorup;
	
	@PC_FieldGenerator
	public static PCtr_BlockClassicElevatorDown elevatordown;
	
	@PC_FieldGenerator
	public static PCtr_BlockUpgradeableBelt upgradeablebelt;
	
	@PC_FieldGenerator
	public static PCtr_BlockPlate plate;
	
	public static PC_EntityDictionary trDict;
	@Override
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		defaultPreInit(event);
	}

	@Override
	@EventHandler
	public void init(FMLInitializationEvent event) {
		defaultInit(event);
		trDict = new PC_EntityDictionary();
	}

	@Override
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}
