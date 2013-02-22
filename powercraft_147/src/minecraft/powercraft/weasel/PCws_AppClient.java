package powercraft.weasel;

import java.util.List;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import powercraft.management.PC_IClientModule;
import powercraft.management.PC_Struct2;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_TextureRegistry;

public class PCws_AppClient extends PCws_App implements PC_IClientModule {

	@Override
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

	@Override
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add(PC_TextureRegistry.getTerrainFile(this));
		textures.add(PC_TextureRegistry.getTextureDirectory(this) + "block_chip.png");
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
		guis.add(new PC_Struct2<String, Class>("WeaselTouchscreen", PCws_GuiWeaselTouchscreen.class));
		guis.add(new PC_Struct2<String, Class>("WeaselDiskManager", PCws_GuiWeaselDiskManager.class));
		guis.add(new PC_Struct2<String, Class>("WeaselDiskDrive", PCws_GuiWeaselDiskDrive.class));
		return guis;
	}

	@Override
	public List<PC_Struct2<Class<? extends Entity>, Render>> registerEntityRender(
			List<PC_Struct2<Class<? extends Entity>, Render>> list) {
		return null;
	}

}
