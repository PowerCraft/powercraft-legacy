package powercraft.net;

import java.util.List;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import powercraft.launcher.PC_ClientModule;
import powercraft.launcher.PC_ClientModule.PC_InitLanguage;
import powercraft.launcher.PC_ClientModule.PC_LoadTextureFiles;
import powercraft.launcher.PC_Module.PC_RegisterGuis;
import powercraft.management.PC_IClientModule;
import powercraft.management.PC_Struct2;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_TextureRegistry;

@PC_ClientModule
public class PCnt_AppClient extends PCnt_App {

	@PC_InitLanguage
	public List<LangEntry> initLanguage(List<LangEntry> lang) {
		lang.add(new LangEntry("pc.gui.sensor.range", "Detection distance:"));
		lang.add(new LangEntry("pc.sensor.range.1", "Range: %s block"));
		lang.add(new LangEntry("pc.sensor.range.2-4", "Range: %s block"));
		lang.add(new LangEntry("pc.sensor.range.5+", "Range: %s block"));
		lang.add(new LangEntry("pc.gui.radio.channel", "Channel:"));
		lang.add(new LangEntry("pc.gui.radio.showLabel", "Show label"));
		lang.add(new LangEntry("pc.gui.radio.errChannel", "Invalid channel name."));
		lang.add(new LangEntry("pc.gui.radio.renderSmall", "Tiny"));
		lang.add(new LangEntry("pc.radioRemote.connected", "Portable transmitter connected to channel \"%s\"."));
		lang.add(new LangEntry("pc.radioRemote.desc", "Channel: %s"));
		lang.add(new LangEntry("pc.radio.activatorSetChannel", "Radio connected to channel \"%s\"."));
		lang.add(new LangEntry("pc.radio.activatorGetChannel", "Channel \"%s\" assigned to activation crystal."));
		return lang;
	}

	@PC_LoadTextureFiles
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add("tiles.png");
		return textures;
	}

	@PC_RegisterGuis
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("Sensor", PCnt_GuiSensor.class));
		guis.add(new PC_Struct2<String, Class>("Radio", PCnt_GuiRadio.class));
		return guis;
	}
	
}
