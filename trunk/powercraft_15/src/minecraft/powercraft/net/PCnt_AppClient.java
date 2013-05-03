package powercraft.net;

import java.util.List;

import powercraft.api.gres.PC_IGresClient;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.utils.PC_Struct2;
import powercraft.launcher.loader.PC_ClientModule;
import powercraft.launcher.loader.PC_ClientModule.PC_InitLanguage;
import powercraft.launcher.loader.PC_ClientModule.PC_RegisterGuis;

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

	@PC_RegisterGuis
	public List<PC_Struct2<String, Class<? extends PC_IGresClient>>> registerGuis(
			List<PC_Struct2<String, Class<? extends PC_IGresClient>>> guis) {
		guis.add(new PC_Struct2<String, Class<? extends PC_IGresClient>>("Sensor", PCnt_GuiSensor.class));
		guis.add(new PC_Struct2<String, Class<? extends PC_IGresClient>>("Radio", PCnt_GuiRadio.class));
		return guis;
	}
	
}
