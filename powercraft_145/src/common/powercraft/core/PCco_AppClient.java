package powercraft.core;

import java.util.List;

import powercraft.management.PC_IClientModule;
import powercraft.management.PC_LangEntry;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils;

public class PCco_AppClient extends PCco_App implements PC_IClientModule {

	@Override
	public List<PC_LangEntry> initLanguage(List<PC_LangEntry> lang) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add(PC_Utils.getTerrainFile(this));
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
	
}