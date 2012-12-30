package powercraft.weasel;

import java.util.List;

import powercraft.management.PC_IClientModule;
import powercraft.management.PC_LangEntry;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleInfo;

public class PCws_AppClient extends PCws_App implements PC_IClientModule {

	@Override
	public List<PC_LangEntry> initLanguage(List<PC_LangEntry> lang) {
		lang.add(new PC_LangEntry("pc.gui.weasel.divice.name", "divice name:"));
		lang.add(new PC_LangEntry("pc.gui.weasel.divice.rename", "rename divice"));
		lang.add(new PC_LangEntry("pc.gui.weasel.network.tab", "Network"));
		lang.add(new PC_LangEntry("pc.gui.weasel.network.name", "network name:"));
		lang.add(new PC_LangEntry("pc.gui.weasel.network.join", "join network"));
		lang.add(new PC_LangEntry("pc.gui.weasel.network.rename", "rename network"));
		lang.add(new PC_LangEntry("pc.gui.weasel.network.new", "new network"));
		
		lang.add(new PC_LangEntry("pc.gui.weasel.core.program", "Program"));
		lang.add(new PC_LangEntry("pc.gui.weasel.core.noError", "no error"));
		lang.add(new PC_LangEntry("pc.gui.weasel.core.running", "running"));
		lang.add(new PC_LangEntry("pc.gui.weasel.core.crashed", "crashed"));
		lang.add(new PC_LangEntry("pc.gui.weasel.core.stoped", "stoped"));
		lang.add(new PC_LangEntry("pc.gui.weasel.core.launchProgram", "Launch"));
		lang.add(new PC_LangEntry("pc.gui.weasel.core.restartProgram", "Restart"));
		lang.add(new PC_LangEntry("pc.gui.weasel.core.stopProgram", "Stop"));
		
		lang.add(new PC_LangEntry("pc.gui.weasel.core.status", "Status"));
		lang.add(new PC_LangEntry("pc.gui.weasel.core.runningStateLabel", "Program state:"));
		lang.add(new PC_LangEntry("pc.gui.weasel.core.stackLabel", "Stack size:"));
		lang.add(new PC_LangEntry("pc.gui.weasel.core.memoryLabel", "Memory size:"));
		lang.add(new PC_LangEntry("pc.gui.weasel.core.statusLabel", "Status:"));
		lang.add(new PC_LangEntry("pc.gui.weasel.core.programLength", "Program length:"));
		lang.add(new PC_LangEntry("pc.gui.weasel.core.peripheralsLabel", "Peripherals:"));
		lang.add(new PC_LangEntry("pc.gui.weasel.core.unitInstructions", "instructions"));
		lang.add(new PC_LangEntry("pc.gui.weasel.core.unitObjects", "values"));
		
		lang.add(new PC_LangEntry("pc.gui.weasel.terminal.terminal", "Terminal"));
		
		return lang;
	}

	@Override
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add(ModuleInfo.getTerrainFile(this));
		textures.add(ModuleInfo.getTextureDirectory(this) + "block_chip.png");
		return textures;
	}
	
	@Override
	public List<String> addSplashes(List<String> list) {
		return null;
	}
	
	@Override
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("WeaselCore", PCws_GuiWeaselCore.class));
		guis.add(new PC_Struct2<String, Class>("WeaselOnlyNet", PCws_GuiWeaselOnlyNet.class));
		guis.add(new PC_Struct2<String, Class>("WeaselTerminal", PCws_GuiWeaselTerminal.class));
		return guis;
	}

}
