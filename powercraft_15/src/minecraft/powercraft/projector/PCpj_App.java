package powercraft.projector;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import powercraft.api.annotation.PC_FieldObject;
import powercraft.api.block.PC_Block;
import powercraft.launcher.loader.PC_Module;
import powercraft.launcher.loader.PC_ModuleObject;
import powercraft.launcher.loader.PC_Module.PC_Instance;

@PC_Module(name="Projector", version="1.0.0")
public class PCpj_App {
	
	@PC_FieldObject(clazz=PCpj_BlockProjector.class)
	public static PC_Block projector;

	@PC_Instance
	public static PC_ModuleObject instance;
	
	public void destryFrameBuffer(PCpj_TileEntityProjector tileEntityProjector) {}
	
	public void createFrameBuffer(PCpj_TileEntityProjector tileEntityProjector) {}
	
	public static void sDestryFrameBuffer(PCpj_TileEntityProjector tileEntityProjector) {
		((PCpj_App)instance.getModule()).destryFrameBuffer(tileEntityProjector);
	}
	
	public static void sCreateFrameBuffer(PCpj_TileEntityProjector tileEntityProjector) {
		((PCpj_App)instance.getModule()).createFrameBuffer(tileEntityProjector);
	}
	
}
