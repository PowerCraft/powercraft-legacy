package powercraft.checkpoints;

import java.util.List;

import powercraft.api.PC_Struct2;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.launcher.loader.PC_ClientModule;
import powercraft.launcher.loader.PC_ClientModule.PC_InitLanguage;
import powercraft.launcher.loader.PC_ClientModule.PC_LoadTextureFiles;
import powercraft.launcher.loader.PC_ClientModule.PC_RegisterGuis;

@PC_ClientModule
public class PCcp_AppClient extends PCcp_App {

	@PC_InitLanguage
	public List<LangEntry> initLanguage(List<LangEntry> lang) {
		lang.add(new LangEntry("pc.checkpoint.setSpawn", "Set spawnpoint to %s"));
		lang.add(new LangEntry("pc.gui.checkpoint.title", "Checkpoint"));
		lang.add(new LangEntry("pc.gui.checkpoint.walkingtiggerd", "Walkingtiggerd"));
		return lang;
	}

	@PC_LoadTextureFiles
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add("tiles.png");
		return textures;
	}

	@PC_RegisterGuis
	public List<PC_Struct2<String, Class<? extends PC_IGresClient>>> registerGuis(List<PC_Struct2<String, Class<? extends PC_IGresClient>>> guis) {
		guis.add(new PC_Struct2<String, Class<? extends PC_IGresClient>>("Checkpoint", PCcp_GuiCheckpoint.class));
		return guis;
	}
	
}
