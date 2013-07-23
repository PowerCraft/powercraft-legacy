package powercraft.api.registries;

import powercraft.api.PC_Module;

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
	
}
