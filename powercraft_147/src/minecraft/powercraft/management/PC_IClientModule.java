package powercraft.management;

import java.util.List;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import powercraft.management.registry.PC_LangRegistry.LangEntry;

public interface PC_IClientModule extends PC_IModule {
	
	public List<LangEntry> initLanguage(List<LangEntry> lang);
	
	public List<String> loadTextureFiles(List<String> textures);
	
	public List<String> addSplashes(List<String> list);
	
	public List<PC_Struct2<Class<? extends Entity>, Render>> registerEntityRender(List<PC_Struct2<Class<? extends Entity>, Render>> list);
	
}
