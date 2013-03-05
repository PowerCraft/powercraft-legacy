package powercraft.deco;

import java.util.List;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import powercraft.launcher.PC_ClientModule;
import powercraft.launcher.PC_ClientModule.PC_LoadTextureFiles;
import powercraft.management.PC_IClientModule;
import powercraft.management.PC_Struct2;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_TextureRegistry;

@PC_ClientModule
public class PCde_AppClient extends PCde_App {

	@PC_LoadTextureFiles
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add("tiles.png");
		textures.add("block_deco.png");
		return textures;
	}

}
