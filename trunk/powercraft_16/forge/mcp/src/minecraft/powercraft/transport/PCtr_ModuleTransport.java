package powercraft.transport;

import powercraft.api.PC_Api;
import powercraft.api.PC_FieldGenerator;
import powercraft.api.PC_Module;
import powercraft.transport.blocks.PCtr_BlockClassicElevator;
import powercraft.transport.blocks.PCtr_BlockClassicElevatorDown;
import powercraft.transport.blocks.PCtr_BlockPlate;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = PCtr_ModuleTransport.NAME, name = PCtr_ModuleTransport.NAME, version=PCtr_ModuleTransport.VERSION, dependencies=PCtr_ModuleTransport.DEPENDENCIES)
public class PCtr_ModuleTransport extends PC_Module
{
	
	public static final String NAME = POWERCRAFT+"-Transport";
	public static final String VERSION = "1.0.0";
	public static final String DEPENDENCIES = "required-after:"+PC_Api.NAME;
	
	@Instance(NAME)
	public static PCtr_ModuleTransport instance;

	@PC_FieldGenerator	
	public static PCtr_BlockClassicElevator elevatorup;
	
	@PC_FieldGenerator
	public static PCtr_BlockClassicElevatorDown elevatordown;
	
	@PC_FieldGenerator
	public static PCtr_BlockPlate plate;
	
	
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
