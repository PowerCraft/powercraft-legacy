/**
 * 
 */
package powercraft.moduletemplate;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import powercraft.api.PC_FieldGenerator;
import powercraft.api.PC_Module;
import powercraft.moduletemplate.blocks.PCmt_BlockTemplate;
import powercraft.transport.PCtr_ModuleTransport;
import powercraft.transport.blocks.PCtr_BlockClassicElevator;
import powercraft.transport.blocks.PCtr_BlockClassicElevatorDown;
import powercraft.transport.blocks.PCtr_BlockPlate;

/**
 * IDs:<br>
 * 3009 {@link PCmt_BlockTemplate}<br>
 */
@Mod(modid = "PowerCraft-StructurePattern", name = "PowerCraft-StructurePattern", version="1.0.0", dependencies="required-after:PowerCraft-Api")
public class PCmt_ModuleModuleTemplate extends PC_Module
{
	
	@Instance("PowerCraft-StructurePattern")
	public static PCmt_ModuleModuleTemplate instance;

	@PC_FieldGenerator	
	public static PCmt_BlockTemplate blockPattern;
	
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
