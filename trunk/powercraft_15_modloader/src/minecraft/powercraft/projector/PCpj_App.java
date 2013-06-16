package powercraft.projector;

import powercraft.api.annotation.PC_FieldObject;
import powercraft.api.block.PC_Block;
import powercraft.launcher.loader.PC_Module;
import powercraft.launcher.loader.PC_Module.PC_Instance;
import powercraft.launcher.loader.PC_ModuleObject;

@PC_Module(name="Projector", version="1.0.0")
public class PCpj_App {
	
	@PC_FieldObject(clazz=PCpj_BlockProjector.class)
	public static PC_Block projector;

	@PC_Instance
	public static PC_ModuleObject instance;
	
}
