package powercraft.api.registries;

import java.util.List;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import powercraft.apiOld.PC_Module;

public class PC_ModuleRegistry {

	private static PC_Module activeModule;
	
	protected static void setActiveModule(PC_Module activeModule){
		PC_ModuleRegistry.activeModule = activeModule;
	}
	
	protected static void releaseActiveModule() {
		PC_ModuleRegistry.activeModule = null;
	}
	
	public static PC_Module getActiveModule(){
		return activeModule;
	}

	public static ModContainer getMod(String mod) {
		List<ModContainer> activeMods = Loader.instance().getActiveModList();
		for(ModContainer activeMod:activeMods){
			if(activeMod.getModId().equals(mod)){
				return activeMod;
			}
		}
		return null;
	}
	
	public static PC_Module getModule(String module) {
		ModContainer mod = getMod("PowerCraft-"+module);
		if(mod==null)
			return null;
		return (PC_Module) mod.getMod();
	}
	
}
