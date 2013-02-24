package powercraft.management.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import powercraft.management.PC_Block;
import powercraft.management.PC_IModule;
import powercraft.management.PC_ITileEntityRenderer;
import powercraft.management.PC_ItemBlock;
import powercraft.management.PC_Property;
import powercraft.management.mod_PowerCraft;
import powercraft.management.annotation.PC_BlockInfo;
import powercraft.management.annotation.PC_Shining;
import powercraft.management.reflect.PC_FieldWithAnnotation;
import powercraft.management.reflect.PC_IFieldAnnotationIterator;
import powercraft.management.reflect.PC_ReflectHelper;
import powercraft.management.registry.PC_LangRegistry.LangEntry;
import cpw.mods.fml.common.registry.EntityRegistry;

public class PC_EntityRegistry {
	
	public static void register(PC_IModule module, Class<? extends Entity> c, int entityID){
		final PC_Property config = PC_ModuleRegistry.getConfig(module).getProperty(c.getSimpleName(), null, null);
		
		if(!config.getBoolean("enabled", true)){
			return;
		}
		
		int id = config.getInt("defaultID", entityID);
		
		EntityRegistry.registerModEntity(c, c.getName(), id, mod_PowerCraft.getInstance(), 50, 5, false);
	}
	
}
