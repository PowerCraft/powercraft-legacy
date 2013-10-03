package powercraft.tutorial;

import powercraft.api.PC_FieldGenerator;
import powercraft.api.PC_Module;
import powercraft.tutorial.blocks.PC_BlockTutorial;
import powercraft.tutorial.items.PC_ItemTutorial;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * 
 * id = 2000 {@link PC_BlockTutorial}
 * id = 3412 {@link PC_ItemTutorial}
 * 
 * @author XOR
 *
 */

@Mod(modid = "PowerCraft-Tutorial", name = "PowerCraft-Tutorial", version="1.0.0", dependencies="required-after:PowerCraft-Api")
public class PC_Tutorial extends PC_Module {

	@Instance("PowerCraft-Tutorial")
	public static PC_Tutorial instance;

	@PC_FieldGenerator
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
