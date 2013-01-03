package powercraft.net;

import java.util.List;

import powercraft.management.PC_IClientModule;
import powercraft.management.PC_LangEntry;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils.ModuleInfo;

public class PCnt_AppClient extends PCnt_App implements PC_IClientModule {

	@Override
	public List<PC_LangEntry> initLanguage(List<PC_LangEntry> lang) {
		lang.add(new PC_LangEntry("pc.gui.sensor.range", "Detection distance:"));
		lang.add(new PC_LangEntry("pc.sensor.range.1", "Range: %s block"));
		lang.add(new PC_LangEntry("pc.sensor.range.2-4", "Range: %s block"));
		lang.add(new PC_LangEntry("pc.sensor.range.5+", "Range: %s block"));
		lang.add(new PC_LangEntry("pc.gui.radio.channel", "Channel:"));
		lang.add(new PC_LangEntry("pc.gui.radio.showLabel", "Show label"));
		lang.add(new PC_LangEntry("pc.gui.radio.errChannel", "Invalid channel name."));
		lang.add(new PC_LangEntry("pc.gui.radio.renderSmall", "Tiny"));
		lang.add(new PC_LangEntry("pc.radioRemote.connected", "Portable transmitter connected to channel \"%s\"."));
		lang.add(new PC_LangEntry("pc.radioRemote.desc", "Channel: %s"));
		lang.add(new PC_LangEntry("pc.radio.activatorSetChannel", "Radio connected to channel \"%s\"."));
		lang.add(new PC_LangEntry("pc.radio.activatorGetChannel", "Channel \"%s\" assigned to activation crystal."));
		return lang;
	}

	@Override
	public List<String> loadTextureFiles(List<String> textures) {
		textures.add(ModuleInfo.getTerrainFile(this));
		return textures;
	}

	@Override
	public List<String> addSplashes(List<String> list) {
		return null;
	}

	@Override
	public List<PC_Struct2<String, Class>> registerGuis(
			List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("Sensor", PCnt_GuiSensor.class));
		guis.add(new PC_Struct2<String, Class>("Radio", PCnt_GuiRadio.class));
		return guis;
	}
	
}
