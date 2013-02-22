package powercraft.machines;

import java.util.List;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import powercraft.management.PC_IClientModule;
import powercraft.management.PC_Struct2;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_TextureRegistry;

public class PCma_AppClient extends PCma_App implements PC_IClientModule {

	@Override
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

	@Override
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add(PC_TextureRegistry.getTerrainFile(this));
		textures.add(PC_TextureRegistry.getTextureDirectory(this) + "fisher.png");
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

	@Override
	public List<PC_Struct2<Class<? extends Entity>, Render>> registerEntityRender(
			List<PC_Struct2<Class<? extends Entity>, Render>> list) {
		return null;
	}

}
