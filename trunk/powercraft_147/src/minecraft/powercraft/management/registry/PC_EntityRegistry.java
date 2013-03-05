package powercraft.management.registry;

import net.minecraft.entity.Entity;
import powercraft.launcher.PC_ModuleObject;
import powercraft.launcher.PC_Property;
import powercraft.launcher.mod_PowerCraft;
import cpw.mods.fml.common.registry.EntityRegistry;

public class PC_EntityRegistry {
	
	public static void register(PC_ModuleObject module, Class<? extends Entity> c, int entityID){
		final PC_Property config = module.getConfig().getProperty(c.getSimpleName(), null, null);
		
		if(!config.getBoolean("enabled", true)){
			return;
		}
		
		int id = config.getInt("defaultID", entityID);
		
		EntityRegistry.registerModEntity(c, c.getName(), id, mod_PowerCraft.getInstance(), 50, 5, false);
	}
	
}
