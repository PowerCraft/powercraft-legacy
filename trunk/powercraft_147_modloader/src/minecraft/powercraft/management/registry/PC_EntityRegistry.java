package powercraft.management.registry;

import net.minecraft.src.Entity;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_PowerCraft;
import powercraft.management.PC_IModule;
import powercraft.management.PC_Property;

public class PC_EntityRegistry {

	private static int nextID = 200;
	
	public static void register(PC_IModule module, Class<? extends Entity> c){
		final PC_Property config = PC_ModuleRegistry.getConfig(module).getProperty(c.getSimpleName(), null, null);
		
		if(!config.getBoolean("enabled", true)){
			return;
		}
		
		int id = config.getInt("defaultID", -1);
		if(id==-1){
			id = nextID++;
			config.setInt("defaultID", id);
		}
		
		ModLoader.registerEntityID(c, c.getName(), id);
		ModLoader.addEntityTracker(mod_PowerCraft.getInstance(), c, id, 50, 5, false);
	}
	
}
