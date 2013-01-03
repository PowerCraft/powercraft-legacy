package powercraft.machines;

import java.util.List;

import powercraft.management.PC_IClientModule;
import powercraft.management.PC_LangEntry;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleInfo;

public class PCma_AppClient extends PCma_App implements PC_IClientModule {

	@Override
	public List<PC_LangEntry> initLanguage(List<PC_LangEntry> lang) {
		lang.add(new PC_LangEntry("pc.gui.automaticWorkbench.redstoneActivated", "Redstone triggered"));
		lang.add(new PC_LangEntry("pc.roaster.insertFuel", "fuel"));
		lang.add(new PC_LangEntry("pc.gui.blockReplacer.title", "Block Replacer"));
		lang.add(new PC_LangEntry("pc.gui.blockReplacer.errWrongValue", "Expects a value between -16 and 16."));
		lang.add(new PC_LangEntry("pc.gui.blockReplacer.err3zeros", "Expects at least 1 value unequal 0."));
		lang.add(new PC_LangEntry("pc.gui.blockReplacer.particleFrame", "Particles"));
		lang.add(new PC_LangEntry("pc.gui.xpbank.storagePoints", "Stored XP:"));
		lang.add(new PC_LangEntry("pc.gui.xpbank.currentPlayerLevel", "Your level:"));
		lang.add(new PC_LangEntry("pc.gui.xpbank.xpUnit", "points"));
		lang.add(new PC_LangEntry("pc.gui.xpbank.xpLevels", "levels"));
		lang.add(new PC_LangEntry("pc.gui.xpbank.withdraw", "Withdraw:"));
		lang.add(new PC_LangEntry("pc.gui.xpbank.deposit", "Deposit:"));
		lang.add(new PC_LangEntry("pc.gui.xpbank.oneLevel", "1 level"));
		lang.add(new PC_LangEntry("pc.gui.xpbank.all", "all"));
		lang.add(new PC_LangEntry("pc.gui.transmutabox.timeCritical", "Time critical?"));
		return lang;
	}

	@Override
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add(ModuleInfo.getTerrainFile(this));
		textures.add(ModuleInfo.getTextureDirectory(this) + "fisher.png");
		return textures;
	}

	@Override
	public List<String> addSplashes(List<String> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("AutomaticWorkbench", PCma_GuiAutomaticWorkbench.class));
		guis.add(new PC_Struct2<String, Class>("BlockBuilder", PCma_GuiBlockBuilder.class));
		guis.add(new PC_Struct2<String, Class>("Replacer", PCma_GuiReplacer.class));
		guis.add(new PC_Struct2<String, Class>("Roaster", PCma_GuiRoaster.class));
		guis.add(new PC_Struct2<String, Class>("Transmutabox", PCma_GuiTransmutabox.class));
		guis.add(new PC_Struct2<String, Class>("XPBank", PCma_GuiXPBank.class));
		return guis;
	}

}
