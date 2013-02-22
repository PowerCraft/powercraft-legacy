package powercraft.transport;

import java.util.List;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import powercraft.management.PC_IClientModule;
import powercraft.management.PC_Struct2;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import powercraft.management.registry.PC_TextureRegistry;

public class PCtr_AppClient extends PCtr_App implements PC_IClientModule {

    @Override
	public List<String> loadTextureFiles(List<String> textures)
    {
		textures.add(PC_TextureRegistry.getTerrainFile(this));
		textures.add(PC_TextureRegistry.getTextureDirectory(this)+"slimeboots.png");
		return textures;
    }

	@Override
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

	@Override
	public List<String> addSplashes(List<String> list) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<PC_Struct2<String, Class>> registerGuis(List<PC_Struct2<String, Class>> guis) {
		guis.add(new PC_Struct2<String, Class>("SeperationBelt", PCtr_GuiSeparationBelt.class));
		guis.add(new PC_Struct2<String, Class>("EjectionBelt", PCtr_GuiEjectionBelt.class));
		guis.add(new PC_Struct2<String, Class>("Splitter", PCtr_GuiSplitter.class));
		return guis;
	}

	@Override
	public List<PC_Struct2<Class<? extends Entity>, Render>> registerEntityRender(
			List<PC_Struct2<Class<? extends Entity>, Render>> list) {
		return null;
	}
	
}
