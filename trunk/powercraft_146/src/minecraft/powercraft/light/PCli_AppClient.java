package powercraft.light;

import java.util.List;

import powercraft.management.PC_IClientModule;
import powercraft.management.PC_LangEntry;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleInfo;

public class PCli_AppClient extends PCli_App implements PC_IClientModule {

	@Override
    public List<String> loadTextureFiles(List<String> textures)
    {
        textures.add(ModuleInfo.getTerrainFile(this));
        textures.add(ModuleInfo.getTextureDirectory(this) + "block_light.png");
        textures.add(ModuleInfo.getTextureDirectory(this) + "mirror.png");
        return textures;
    }
	
	@Override
	public List<PC_LangEntry> initLanguage(List<PC_LangEntry> lang) {
		lang.add(new PC_LangEntry("pc.gui.light.isHuge", "is Huge"));
		lang.add(new PC_LangEntry("pc.gui.light.isStable", "is Stable"));
		lang.add(new PC_LangEntry("pc.damage.laser", "%s wanted to know his reflexivity"));
		return lang;
	}

	@Override
	public List<String> addSplashes(List<String> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("Light", PCli_GuiLight.class));
		return guis;
	}
	
}
