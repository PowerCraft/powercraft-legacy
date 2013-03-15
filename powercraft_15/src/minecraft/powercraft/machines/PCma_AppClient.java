package powercraft.machines;

import java.util.List;

import powercraft.api.PC_Struct2;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.launcher.loader.PC_ClientModule;
import powercraft.launcher.loader.PC_ClientModule.PC_InitLanguage;
import powercraft.launcher.loader.PC_ClientModule.PC_LoadTextureFiles;
import powercraft.launcher.loader.PC_ClientModule.PC_RegisterGuis;

@PC_ClientModule
public class PCma_AppClient extends PCma_App {

	@PC_InitLanguage
	public List<LangEntry> initLanguage(List<LangEntry> lang) {
		lang.add(new LangEntry("pc.gui.automaticWorkbench.redstoneActivated", "Redstone triggered"));
		lang.add(new LangEntry("pc.roaster.insertFuel", "fuel"));
		lang.add(new LangEntry("pc.gui.blockReplacer.title", "Block Replacer"));
		lang.add(new LangEntry("pc.gui.blockReplacer.errWrongValue", "Expects a value between -16 and 16."));
		lang.add(new LangEntry("pc.gui.blockReplacer.err3zeros", "Expects at least 1 value unequal 0."));
		lang.add(new LangEntry("pc.gui.blockReplacer.particleFrame", "Particles"));
		lang.add(new LangEntry("pc.gui.xpbank.storagePoints", "Stored XP:"));
		lang.add(new LangEntry("pc.gui.xpbank.currentPlayerLevel", "Your level:"));
		lang.add(new LangEntry("pc.gui.xpbank.xpUnit", "points"));
		lang.add(new LangEntry("pc.gui.xpbank.xpLevels", "levels"));
		lang.add(new LangEntry("pc.gui.xpbank.withdraw", "Withdraw:"));
		lang.add(new LangEntry("pc.gui.xpbank.deposit", "Deposit:"));
		lang.add(new LangEntry("pc.gui.xpbank.oneLevel", "1 level"));
		lang.add(new LangEntry("pc.gui.xpbank.all", "all"));
		lang.add(new LangEntry("pc.gui.transmutabox.timeCritical", "Time critical?"));
		return lang;
	}

	@PC_LoadTextureFiles
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add("tiles.png");
		textures.add("fisher.png");
		return textures;
	}

	@PC_RegisterGuis
	public List<PC_Struct2<String, Class<? extends PC_IGresClient>>> registerGuis(
			List<PC_Struct2<String, Class<? extends PC_IGresClient>>> guis) {
		guis.add(new PC_Struct2<String, Class<? extends PC_IGresClient>>("AutomaticWorkbench", PCma_GuiAutomaticWorkbench.class));
		guis.add(new PC_Struct2<String, Class<? extends PC_IGresClient>>("BlockBuilder", PCma_GuiBlockBuilder.class));
		guis.add(new PC_Struct2<String, Class<? extends PC_IGresClient>>("Replacer", PCma_GuiReplacer.class));
		guis.add(new PC_Struct2<String, Class<? extends PC_IGresClient>>("Roaster", PCma_GuiRoaster.class));
		guis.add(new PC_Struct2<String, Class<? extends PC_IGresClient>>("Transmutabox", PCma_GuiTransmutabox.class));
		guis.add(new PC_Struct2<String, Class<? extends PC_IGresClient>>("XPBank", PCma_GuiXPBank.class));
		return guis;
	}

}
