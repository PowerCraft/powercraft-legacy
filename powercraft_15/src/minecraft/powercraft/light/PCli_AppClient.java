package powercraft.light;

import java.util.List;

import powercraft.api.PC_Struct2;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.launcher.loader.PC_ClientModule;
import powercraft.launcher.loader.PC_ClientModule.PC_InitLanguage;
import powercraft.launcher.loader.PC_ClientModule.PC_LoadTextureFiles;
import powercraft.launcher.loader.PC_ClientModule.PC_RegisterGuis;

@PC_ClientModule
public class PCli_AppClient extends PCli_App {

	@PC_LoadTextureFiles
    public List<String> loadTextureFiles(List<String> textures)
    {
        textures.add("block_light.png");
        textures.add("mirror.png");
        return textures;
    }
	
	@PC_InitLanguage
	public List<LangEntry> initLanguage(List<LangEntry> lang) {
		lang.add(new LangEntry("pc.gui.light.isHuge", "is Huge"));
		lang.add(new LangEntry("pc.gui.light.isStable", "is Stable"));
		lang.add(new LangEntry("pc.damage.laser", "%s wanted to know his reflexivity"));
		return lang;
	}

	@PC_RegisterGuis
	public List<PC_Struct2<String, Class<? extends PC_IGresClient>>> registerGuis(List<PC_Struct2<String, Class<? extends PC_IGresClient>>> guis) {
		guis.add(new PC_Struct2<String, Class<? extends PC_IGresClient>>("Light", PCli_GuiLight.class));
		return guis;
	}
	
}
