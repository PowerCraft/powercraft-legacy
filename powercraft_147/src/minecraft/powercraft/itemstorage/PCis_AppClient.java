package powercraft.itemstorage;

import java.util.List;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import powercraft.launcher.PC_ClientModule;
import powercraft.launcher.PC_ClientModule.PC_InitLanguage;
import powercraft.launcher.PC_ClientModule.PC_LoadTextureFiles;
import powercraft.launcher.PC_Module.PC_RegisterGuis;
import powercraft.management.PC_IClientModule;
import powercraft.management.PC_Struct2;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_TextureRegistry;

@PC_ClientModule
public class PCis_AppClient extends PCis_App {

	@PC_InitLanguage
	public List<LangEntry> initLanguage(List<LangEntry> lang) {
		lang.add(new LangEntry("pc.gui.compressor.name", "name:"));
		lang.add(new LangEntry("pc.gui.compressor.takeStacks", "Fill Stacks"));
		lang.add(new LangEntry("pc.gui.compressor.putStacks", "Fill Inv Stacks"));
		return lang;
	}

	@PC_LoadTextureFiles
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add("tiles.png");
		return textures;
	}

	@PC_RegisterGuis
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("Compressor", PCis_GuiCompressor.class));
		return guis;
	}
}
