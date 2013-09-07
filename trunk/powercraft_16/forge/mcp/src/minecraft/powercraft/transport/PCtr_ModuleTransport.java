package powercraft.transport;

import powercraft.api.PC_FieldGenerator;
import powercraft.api.PC_Module;
import powercraft.transport.blocks.PCtr_BlockPrimitivePlate;
import cpw.mods.fml.common.Mod;
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
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		defaultPreInit(event);

	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		defaultInit(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
}
