package powercraft.light;

import java.util.List;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import powercraft.management.PC_IClientModule;
import powercraft.management.PC_Struct2;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_TextureRegistry;

public class PCli_AppClient extends PCli_App implements PC_IClientModule {

	@Override
    public List<String> loadTextureFiles(List<String> textures)
    {
        textures.add(PC_TextureRegistry.getTerrainFile(this));
        textures.add(PC_TextureRegistry.getTextureDirectory(this) + "block_light.png");
        textures.add(PC_TextureRegistry.getTextureDirectory(this) + "mirror.png");
        return textures;
    }
	
	@Override
	public List<LangEntry> initLanguage(List<LangEntry> lang) {
		lang.add(new LangEntry("pc.gui.light.isHuge", "is Huge"));
		lang.add(new LangEntry("pc.gui.light.isStable", "is Stable"));
		lang.add(new LangEntry("pc.damage.laser", "%s wanted to know his reflexivity"));
		return lang;
	}

	@Override
	public List<String> addSplashes(List<String> list) {
		return null;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("Light", PCli_GuiLight.class));
		return guis;
	}

	@Override
	public List<PC_Struct2<Class<? extends Entity>, Render>> registerEntityRender(
			List<PC_Struct2<Class<? extends Entity>, Render>> list) {
		return null;
	}
	
}
