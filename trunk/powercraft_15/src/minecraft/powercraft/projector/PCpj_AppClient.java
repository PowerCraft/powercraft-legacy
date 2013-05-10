package powercraft.projector;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;

import powercraft.api.utils.PC_ClientUtils;
import powercraft.launcher.loader.PC_ClientModule;
import powercraft.launcher.loader.PC_Module.PC_Init;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@PC_ClientModule
public class PCpj_AppClient extends PCpj_App {
	
	@PC_Init
	public void init(){
		PC_ClientUtils.mc().entityRenderer = new PCpj_EntityRenderer(PC_ClientUtils.mc());
	}
	
}
