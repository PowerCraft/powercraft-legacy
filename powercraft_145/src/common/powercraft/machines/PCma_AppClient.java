package powercraft.machines;

import java.util.List;

import powercraft.management.PC_IClientModule;
import powercraft.management.PC_LangEntry;
import powercraft.management.PC_Utils;

public class PCma_AppClient extends PCma_App implements PC_IClientModule {

	@Override
	public List<PC_LangEntry> initLanguage(List<PC_LangEntry> lang) {
        PC_Utils.registerLanguage(this,
                "pc.gui.automaticWorkbench.redstoneActivated", "Redstone triggered",
                "pc.roaster.insertFuel", "fuel",
                "pc.gui.blockReplacer.title", "Block Replacer",
                "pc.gui.blockReplacer.errWrongValue", "Expects a value between -16 and 16.",
                "pc.gui.blockReplacer.err3zeros", "Expects at least 1 value unequal 0.",
                "pc.gui.blockReplacer.particleFrame", "Particles",
                "pc.gui.xpbank.storagePoints", "Stored XP:",
                "pc.gui.xpbank.currentPlayerLevel", "Your level:",
                "pc.gui.xpbank.xpUnit", "points",
                "pc.gui.xpbank.xpLevels", "levels",
                "pc.gui.xpbank.withdraw", "Withdraw:",
                "pc.gui.xpbank.deposit", "Deposit:",
                "pc.gui.xpbank.oneLevel", "1 level",
                "pc.gui.xpbank.all", "all"
                                 );
		return null;
	}

	@Override
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add(PC_Utils.getTerrainFile(this));
		return textures;
	}

}
