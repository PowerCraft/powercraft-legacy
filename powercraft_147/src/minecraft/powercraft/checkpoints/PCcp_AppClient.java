package powercraft.checkpoints;

import java.util.List;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import powercraft.management.PC_IClientModule;
import powercraft.management.PC_Struct2;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_TextureRegistry;

public class PCcp_AppClient extends PCcp_App implements PC_IClientModule {

	@Override
	public List<LangEntry> initLanguage(List<LangEntry> lang) {
		lang.add(new LangEntry("pc.checkpoint.setSpawn", "Set spawnpoint to %s"));
		lang.add(new LangEntry("pc.gui.checkpoint.title", "Checkpoint"));
		lang.add(new LangEntry("pc.gui.checkpoint.walkingtiggerd", "Walkingtiggerd"));
		return lang;
	}

	@Override
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add(PC_TextureRegistry.getTerrainFile(this));
		return textures;
	}

	@Override
	public List<String> addSplashes(List<String> list) {
		return null;
	}

	@Override
	public List<PC_Struct2<Class<? extends Entity>, Render>> registerEntityRender(List<PC_Struct2<Class<? extends Entity>, Render>> list) {
		return null;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("Checkpoint", PCcp_GuiCheckpoint.class));
		return guis;
	}
	
}
