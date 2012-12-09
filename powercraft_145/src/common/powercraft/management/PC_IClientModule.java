package powercraft.management;

import java.util.List;

public interface PC_IClientModule extends PC_IModule {
	
	public List<PC_LangEntry> initLanguage(List<PC_LangEntry> lang);
	
	public List<String> loadTextureFiles(List<String> textures);
	
	public List<String> addSplashes(List<String> list);
	
}
