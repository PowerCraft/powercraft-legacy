package powercraft.management.registry;

import net.minecraft.src.Entity;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_PowerCraft;
import powercraft.launcher.PC_Property;
import powercraft.management.PC_IModule;

public class PC_EntityRegistry {
	
	public static void register(PC_IModule module, Class<? extends Entity> c, int entityID){
		final PC_Property config = PC_ModuleRegistry.getConfig(module).getProperty(c.getSimpleName(), null, null);
		
		if(!config.getBoolean("enabled", true)){
			return;
		}
		
		int id = config.getInt("defaultID", entityID);
		
		ModLoader.registerEntityID(c, c.getName(), id);
		ModLoader.addEntityTracker(mod_PowerCraft.getInstance(), c, id, 50, 5, false);
	}
	
	
	
}
