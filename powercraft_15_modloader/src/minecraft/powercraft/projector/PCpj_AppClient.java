package powercraft.projector;

import powercraft.api.utils.PC_ClientUtils;
import powercraft.launcher.loader.PC_ClientModule;
import powercraft.launcher.loader.PC_Module.PC_Init;

@PC_ClientModule
public class PCpj_AppClient extends PCpj_App {
	
	@PC_Init
	public void init(){
		PC_ClientUtils.mc().entityRenderer = new PCpj_EntityRenderer(PC_ClientUtils.mc());
	}
	
}
