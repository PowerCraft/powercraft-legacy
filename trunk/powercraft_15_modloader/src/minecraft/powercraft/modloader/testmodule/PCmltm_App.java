package powercraft.modloader.testmodule;

import powercraft.api.annotation.PC_FieldObject;
import powercraft.api.block.PC_Block;
import powercraft.launcher.loader.PC_ModLoader;
import powercraft.launcher.loader.PC_Module;

@PC_Module(name = "ModLoaderTestModule", version = "0.0.0", modLoader = PC_ModLoader.RISUGAMIS_MODLOADER)
public class PCmltm_App {
	
	@PC_FieldObject(clazz = PCmltm_BlockTestRotate.class)
	public static PC_Block testRotate;
	
}
