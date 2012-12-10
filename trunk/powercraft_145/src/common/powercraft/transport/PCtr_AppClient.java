package powercraft.transport;

import java.util.List;

import powercraft.management.PC_IClientModule;
import powercraft.management.PC_LangEntry;
import powercraft.management.PC_Utils;

public class PCtr_AppClient extends PCtr_App implements PC_IClientModule {



    @Override
	public List<String> loadTextureFiles(List<String> textures)
    {
		textures.add(PC_Utils.getTerrainFile(this));
		return textures;
    }

	@Override
	public List<PC_LangEntry> initLanguage(List<PC_LangEntry> lang) {
		lang.addAll(
        "pc.gui.separationBelt.group", "Ignore subtypes of",
        "pc.gui.separationBelt.groupLogs", "Logs",
        "pc.gui.separationBelt.groupPlanks", "Planks",
        "pc.gui.separationBelt.groupAll", "All",
        "pc.gui.ejector.modeEjectTitle", "Ejection mode:",
        "pc.gui.ejector.modeStacks", "Whole stacks",
        "pc.gui.ejector.modeItems", "Single items",
        "pc.gui.ejector.modeAll", "All contents at once",
        "pc.gui.ejector.modeSelectTitle", "Method of selection:",
        "pc.gui.ejector.modeSelectFirst", "First slot",
        "pc.gui.ejector.modeSelectLast", "Last slot",
        "pc.gui.ejector.modeSelectRandom", "Random slot"
                         );
        return null;
	}

	@Override
	public List<String> addSplashes(List<String> list) {
		// TODO Auto-generated method stub
		return null;
	}
}
