package powercraft.deco;

import java.util.List;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import powercraft.management.PC_IClientModule;
import powercraft.management.PC_Struct2;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_TextureRegistry;

public class PCde_AppClient extends PCde_App implements PC_IClientModule {

	@Override
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add(PC_TextureRegistry.getTerrainFile(this));
		textures.add(PC_TextureRegistry.getTextureDirectory(this)+"block_deco.png");
		return textures;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		return null;
	}

	@Override
	public List<LangEntry> initLanguage(List<LangEntry> lang) {
		return null;
	}

	@Override
	public List<String> addSplashes(List<String> list) {
		return null;
	}

	@Override
	public List<PC_Struct2<Class<? extends Entity>, Render>> registerEntityRender(
			List<PC_Struct2<Class<? extends Entity>, Render>> list) {
		return null;
	}

}
