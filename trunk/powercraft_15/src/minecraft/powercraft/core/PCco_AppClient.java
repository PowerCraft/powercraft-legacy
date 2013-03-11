package powercraft.core;

import java.util.List;

import powercraft.launcher.loader.PC_ClientModule;
import powercraft.launcher.loader.PC_ClientModule.PC_AddSplashes;
import powercraft.launcher.loader.PC_ClientModule.PC_InitLanguage;
import powercraft.launcher.loader.PC_ClientModule.PC_LoadTextureFiles;
import powercraft.launcher.loader.PC_Module.PC_RegisterGuis;
import powercraft.api.PC_Struct2;
import powercraft.api.registry.PC_LangRegistry.LangEntry;

@PC_ClientModule
public class PCco_AppClient extends PCco_App {

	@PC_InitLanguage
	public List<LangEntry> initLanguage(List<LangEntry> lang) {
		lang.add(new LangEntry("pc.gui.ok", "OK"));
		lang.add(new LangEntry("pc.gui.cancel", "Cancel"));
		lang.add(new LangEntry("pc.gui.close", "Close"));
		lang.add(new LangEntry( "pc.gui.back", "Back"));
		lang.add(new LangEntry("pc.gui.craftingTool.title", "Crafting Tool"));
		lang.add(new LangEntry("pc.gui.craftingTool.trashAll", "Trash All"));
		lang.add(new LangEntry("pc.gui.craftingTool.search", "Search"));
		lang.add(new LangEntry("pc.gui.craftingTool.sort", "Sort"));
		lang.add(new LangEntry("pc.gui.update.title", "Mod Update Notification"));
		lang.add(new LangEntry("pc.gui.update.newVersionAvailable", "Update available!"));
		lang.add(new LangEntry("pc.gui.update.readMore", "Read more..."));
		lang.add(new LangEntry("pc.gui.update.version", "Using %1$s (%2$s), Available %3$s (%4$s)"));
		lang.add(new LangEntry("pc.gui.update.doNotShowAgain", "Don't show again"));
		lang.add(new LangEntry("pc.sniffer.desc", "Portable radar device"));
		lang.add(new LangEntry("pc.sniffer.distance", "Sniffing depth (blocks):"));
		return lang;
	}
	
	@PC_LoadTextureFiles
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add("tiles.png");
		return textures;
	}

	
	@PC_AddSplashes
	public List<String> addSplashes(List<String> list) {
		list.add("Sniffing diamonds!");
		return list;
	}
	
	@PC_RegisterGuis
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("CraftingTool", PCco_GuiCraftingTool.class));
		guis.add(new PC_Struct2<String, Class>("OreSnifferResultScreen", PCco_GuiOreSnifferResultScreen.class));
		guis.add(new PC_Struct2<String, Class>("SpawnerEditor", PCco_GuiSpawnerEditor.class));
		return guis;
	}
	
}
