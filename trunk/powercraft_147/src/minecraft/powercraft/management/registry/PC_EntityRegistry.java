package powercraft.management.registry;

import net.minecraft.entity.Entity;
import powercraft.management.mod_PowerCraft;
import cpw.mods.fml.common.registry.EntityRegistry;

public class PC_EntityRegistry {

	public static void register(Class<? extends Entity> c){
		
	}
	
	public static void registerEntity(Class<? extends Entity> c,
			int entityID) {
		EntityRegistry.registerModEntity(c, c.getName(), entityID,
				mod_PowerCraft.getInstance(), 50, 5, false);
	}
	
}
