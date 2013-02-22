package powercraft.core;

import java.util.List;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import powercraft.management.PC_IClientModule;
import powercraft.management.PC_Struct2;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_TextureRegistry;

public class PCco_AppClient extends PCco_App implements PC_IClientModule {

	@Override
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
	
	@Override
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add(PC_TextureRegistry.getTerrainFile(this));
		return textures;
	}

	
	@Override
	public List<String> addSplashes(List<String> list) {
		list.add("Sniffing diamonds!");
		return list;
	}
	
	@Override
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("CraftingTool", PCco_GuiCraftingTool.class));
		guis.add(new PC_Struct2<String, Class>("OreSnifferResultScreen", PCco_GuiOreSnifferResultScreen.class));
		guis.add(new PC_Struct2<String, Class>("SpawnerEditor", PCco_GuiSpawnerEditor.class));
		return guis;
	}

	@Override
	public List<PC_Struct2<Class<? extends Entity>, Render>> registerEntityRender(
			List<PC_Struct2<Class<? extends Entity>, Render>> list) {
		return null;
	}
	
}
