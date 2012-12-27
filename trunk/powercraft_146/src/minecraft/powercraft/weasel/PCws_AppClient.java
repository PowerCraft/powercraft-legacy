package powercraft.weasel;

import java.util.List;

import powercraft.management.PC_IClientModule;
import powercraft.management.PC_LangEntry;
import powercraft.management.PC_Struct2;

public class PCws_AppClient extends PCws_App implements PC_IClientModule {

	@Override
	public List<PC_LangEntry> initLanguage(List<PC_LangEntry> lang) {
		return null;
	}

	@Override
	public List<String> loadTextureFiles(List<String> textures) {
		return null;
	}

	@Override
	public List<String> addSplashes(List<String> list) {
		return null;
	}
	
	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		return null;
	}

}
