package powercraft.api.registries;

import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import powercraft.api.PC_Logger;
import powercraft.api.PC_Module;
import powercraft.api.PC_Security;
import powercraft.api.items.PC_ItemInfo;
import cpw.mods.fml.common.registry.GameRegistry;

public class PC_ItemRegistry {

	public static Item registerItem(PC_Module module, Class<? extends Item> clazz) {

		if(!PC_Security.allowedCallerNoException(PC_Module.class)){
			PC_Logger.warning("PC_ItemRegistry.registerItem shouldn't be called. Use instead @PC_FieldGenerator");
		}
		PC_ModuleRegistry.setActiveModule(module);
		Configuration config = module.getConfig();
		PC_ItemInfo itemInfo = clazz.getAnnotation(PC_ItemInfo.class);
		int itemID = config.getItem(itemInfo.itemid(), itemInfo.defaultid()).getInt();
		if (itemID == -1) {
			PC_Logger.info("Item %s disabled", itemInfo.name());
			return null;
		}
		try {
			Item item = clazz.getConstructor(int.class).newInstance(itemID);
			item.setUnlocalizedName(clazz.getSimpleName());
			PC_LanguageRegistry.registerLanguage(item.getUnlocalizedName()+".name", itemInfo.name());
			GameRegistry.registerItem(item, itemInfo.itemid());
			PC_ModuleRegistry.releaseActiveModule();
			return item;
		} catch (Exception e) {
			e.printStackTrace();
			PC_Logger.severe("Failed to generate item %s", itemInfo.name());
		}
		PC_ModuleRegistry.releaseActiveModule();
		return null;
	}
	
}
