package powercraft.transport;

import java.util.List;

import powercraft.launcher.loader.PC_ClientModule;
import powercraft.launcher.loader.PC_ClientModule.PC_InitLanguage;
import powercraft.launcher.loader.PC_ClientModule.PC_LoadTextureFiles;
import powercraft.launcher.loader.PC_Module.PC_RegisterGuis;
import powercraft.management.PC_Struct2;
import powercraft.management.registry.PC_LangRegistry.LangEntry;

@PC_ClientModule
public class PCtr_AppClient extends PCtr_App {

    @PC_LoadTextureFiles
	public List<String> loadTextureFiles(List<String> textures)
    {
		textures.add("tiles.png");
		textures.add("slimeboots.png");
		return textures;
    }

	@PC_InitLanguage
	public List<LangEntry> initLanguage(List<LangEntry> lang) {
		lang.add(new LangEntry("pc.gui.separationBelt.group", "Ignore subtypes of"));
		lang.add(new LangEntry("pc.gui.separationBelt.groupLogs", "Logs"));
		lang.add(new LangEntry("pc.gui.separationBelt.groupPlanks", "Planks"));
		lang.add(new LangEntry("pc.gui.separationBelt.groupAll", "All"));
		lang.add(new LangEntry("pc.gui.ejector.modeEjectTitle", "Ejection mode:"));
		lang.add(new LangEntry("pc.gui.ejector.modeStacks", "Whole stacks"));
		lang.add(new LangEntry("pc.gui.ejector.modeItems", "Single items"));
		lang.add(new LangEntry("pc.gui.ejector.modeAll", "All contents at once"));
		lang.add(new LangEntry("pc.gui.ejector.modeSelectTitle", "Method of selection:"));
		lang.add(new LangEntry("pc.gui.ejector.modeSelectFirst", "First slot"));
		lang.add(new LangEntry("pc.gui.ejector.modeSelectLast", "Last slot"));
		lang.add(new LangEntry("pc.gui.ejector.modeSelectRandom", "Random slot"));
        return lang;
	}
	
	@PC_RegisterGuis
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("SeperationBelt", PCtr_GuiSeparationBelt.class));
		guis.add(new PC_Struct2<String, Class>("EjectionBelt", PCtr_GuiEjectionBelt.class));
		guis.add(new PC_Struct2<String, Class>("Splitter", PCtr_GuiSplitter.class));
		return guis;
	}
	
}
