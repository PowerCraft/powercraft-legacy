package powercraft.weasel;

import java.util.List;

import powercraft.api.PC_Struct2;
import powercraft.api.gres.PC_IGresClient;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.launcher.loader.PC_ClientModule;
import powercraft.launcher.loader.PC_ClientModule.PC_InitLanguage;
import powercraft.launcher.loader.PC_ClientModule.PC_LoadTextureFiles;
import powercraft.launcher.loader.PC_ClientModule.PC_RegisterGuis;

@PC_ClientModule
public class PCws_AppClient extends PCws_App {

	@PC_InitLanguage
	public List<LangEntry> initLanguage(List<LangEntry> lang) {
		lang.add(new LangEntry("pc.gui.weasel.device.name", "device name:"));
		lang.add(new LangEntry("pc.gui.weasel.device.rename", "rename device"));
		lang.add(new LangEntry("pc.gui.weasel.network.tab", "Network"));
		lang.add(new LangEntry("pc.gui.weasel.network.name", "network name:"));
		lang.add(new LangEntry("pc.gui.weasel.network.join", "join network"));
		lang.add(new LangEntry("pc.gui.weasel.network.rename", "rename network"));
		lang.add(new LangEntry("pc.gui.weasel.network.new", "new network"));
		
		lang.add(new LangEntry("pc.gui.weasel.core.program", "Program"));
		lang.add(new LangEntry("pc.gui.weasel.core.noError", "no error"));
		lang.add(new LangEntry("pc.gui.weasel.core.running", "running"));
		lang.add(new LangEntry("pc.gui.weasel.core.crashed", "crashed"));
		lang.add(new LangEntry("pc.gui.weasel.core.stoped", "stoped"));
		lang.add(new LangEntry("pc.gui.weasel.core.launchProgram", "Launch"));
		lang.add(new LangEntry("pc.gui.weasel.core.restartProgram", "Restart"));
		lang.add(new LangEntry("pc.gui.weasel.core.stopProgram", "Stop"));
		
		lang.add(new LangEntry("pc.gui.weasel.core.status", "Status"));
		lang.add(new LangEntry("pc.gui.weasel.core.runningStateLabel", "Program state:"));
		lang.add(new LangEntry("pc.gui.weasel.core.stackLabel", "Stack size:"));
		lang.add(new LangEntry("pc.gui.weasel.core.memoryLabel", "Memory size:"));
		lang.add(new LangEntry("pc.gui.weasel.core.statusLabel", "Status:"));
		lang.add(new LangEntry("pc.gui.weasel.core.programLength", "Program length:"));
		lang.add(new LangEntry("pc.gui.weasel.core.peripheralsLabel", "Peripherals:"));
		lang.add(new LangEntry("pc.gui.weasel.core.unitInstructions", "instructions"));
		lang.add(new LangEntry("pc.gui.weasel.core.unitObjects", "values"));
		
		lang.add(new LangEntry("pc.gui.weasel.terminal.terminal", "Terminal"));
		
		lang.add(new LangEntry("pc.gui.weasel.diskManager.color", "Color:"));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.label", "Disk label:"));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.separator", "Entry separator:"));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.resize", "Resize"));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.set", "Set"));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.edit", "Edit"));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.img.clear", "Clear"));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.img.fill", "Fill"));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.disk", "Disk"));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.format", "Format:"));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.formatText", "Text"));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.formatImage", "Image"));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.formatIntegerList", "Numbers"));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.formatStringList", "Strings"));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.formatVariableMap", "Data"));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.formatLibrary", "Library"));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.clickCompile", "Click \"Compile\" to make the library executable."));
		lang.add(new LangEntry("pc.gui.weasel.diskManager.compiled", "Library was successfully compiled."));

		lang.add(new LangEntry("pc.gui.weasel.diskDrive.tab", "Disk Drive"));
		
		lang.add(new LangEntry("pc.gui.weasel.diskManager.title", "Digital Workbench"));
		
		lang.add(new LangEntry("pc.weasel.activatorGetNetwork", "Network \"%s\" assigned to activation crystal."));
		lang.add(new LangEntry("pc.weasel.activatorSetNetwork", "Device connected to network \"%s\"."));
		
		return lang;
	}

	@PC_LoadTextureFiles
	public List<String> loadTextureFiles(List<String> textures) {;
		textures.add("block_chip.png");
		return textures;
	}
	
	@PC_RegisterGuis
	public List<PC_Struct2<String, Class<? extends PC_IGresClient>>> registerGuis(List<PC_Struct2<String, Class<? extends PC_IGresClient>>> guis) {
		guis.add(new PC_Struct2<String, Class<? extends PC_IGresClient>>("WeaselCore", PCws_GuiWeaselCore.class));
		guis.add(new PC_Struct2<String, Class<? extends PC_IGresClient>>("WeaselOnlyNet", PCws_GuiWeaselOnlyNet.class));
		guis.add(new PC_Struct2<String, Class<? extends PC_IGresClient>>("WeaselTerminal", PCws_GuiWeaselTerminal.class));
		guis.add(new PC_Struct2<String, Class<? extends PC_IGresClient>>("WeaselTouchscreen", PCws_GuiWeaselTouchscreen.class));
		guis.add(new PC_Struct2<String, Class<? extends PC_IGresClient>>("WeaselDiskManager", PCws_GuiWeaselDiskManager.class));
		guis.add(new PC_Struct2<String, Class<? extends PC_IGresClient>>("WeaselDiskDrive", PCws_GuiWeaselDiskDrive.class));
		return guis;
	}

}
