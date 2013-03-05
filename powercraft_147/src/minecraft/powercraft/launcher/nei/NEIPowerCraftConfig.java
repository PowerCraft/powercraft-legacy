package powercraft.launcher.nei;

import powercraft.launcher.mod_PowerCraft;
import codechicken.nei.api.IConfigureNEI;

public class NEIPowerCraftConfig implements IConfigureNEI {
	
	private Object neiHandler;
	
	public NEIPowerCraftConfig(){
		Class<?> c;
		try {
			c = Class.forName("powercraft.management.nei.PC_NEIPowerCraftConfig");
			neiHandler = c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void loadConfig() {
		try {
			neiHandler.getClass().getDeclaredMethod("loadConfig").invoke(neiHandler);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	@Override
	public String getName() {
		return mod_PowerCraft.getInstance().getModMetadata().name;
	}

	@Override
	public String getVersion() {
		return mod_PowerCraft.getInstance().getModMetadata().version;
	}

}
