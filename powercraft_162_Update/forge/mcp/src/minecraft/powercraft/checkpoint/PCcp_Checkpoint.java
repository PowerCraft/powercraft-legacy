package powercraft.checkpoint;

import powercraft.api.PC_FieldGenerator;
import powercraft.api.PC_Module;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "PowerCraft-Tutorial", name = "PowerCraft-Tutorial", version="1.0.0", dependencies="required-after:PowerCraft-Api")
public class PCcp_Checkpoint extends PC_Module {

	@Instance("PowerCraft-Tutorial")
	public static PCcp_Checkpoint instance;

	@PC_FieldGenerator
	public static PCcp_BlockCheckpoint tutorial;
	
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
