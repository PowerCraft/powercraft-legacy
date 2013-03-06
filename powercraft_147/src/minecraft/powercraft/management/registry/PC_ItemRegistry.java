package powercraft.management.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import powercraft.launcher.PC_Property;
import powercraft.launcher.loader.PC_ModuleObject;
import powercraft.management.PC_GlobalVariables;
import powercraft.management.item.PC_Item;
import powercraft.management.item.PC_ItemArmor;
import powercraft.management.reflect.PC_ReflectHelper;
import powercraft.management.registry.PC_LangRegistry.LangEntry;

public final class PC_ItemRegistry {

	private static TreeMap<String, PC_Item> items = new TreeMap<String, PC_Item>();
	private static TreeMap<String, PC_ItemArmor> itemArmors = new TreeMap<String, PC_ItemArmor>();
	
	public static <T extends Item> T register(PC_ModuleObject module, Class<T> c){
		if(PC_Item.class.isAssignableFrom(c)){
			return (T)registerItem(module, (Class<? extends PC_Item>)c);
		}else if(PC_ItemArmor.class.isAssignableFrom(c)){
			return (T)registerItemArmor(module, (Class<? extends PC_ItemArmor>)c);
		}else{
			throw new IllegalArgumentException("Expect class of PC_Item or PC_ItemArmor");
		}
	}
	
	public static PC_Item registerItem(PC_ModuleObject module, Class<? extends PC_Item> itemClass){
		PC_Property config = module.getConfig().getProperty(itemClass.getSimpleName(), null, null);
		try {
			if(!config.getBoolean("enabled", true)){
				return null;
			}
			
			int id = config.getInt("defaultID", -1);
			if(id==-1){
				id = getFreeItemID();
				config.setInt("defaultID", id);
			}
			if (!isItemIDFree(id)) {
				id = getFreeItemID();
			}
			PC_Item item = PC_ReflectHelper.create(itemClass, id);
			PC_MSGRegistry.registerMSGObject(item);
			items.put(itemClass.getSimpleName(), item);
			item.setItemName(itemClass.getSimpleName());
			item.setModule(module);
			item.setTextureFile(PC_TextureRegistry.getTextureDirectory(module)+"tiles.png");

			item.msg(PC_MSGRegistry.MSG_LOAD_FROM_CONFIG, config);

			List<LangEntry> l = (List<LangEntry>) item
					.msg(PC_MSGRegistry.MSG_DEFAULT_NAME,
							new ArrayList<LangEntry>());
			if (l != null) {
				PC_LangRegistry.registerLanguage(module, l.toArray(new LangEntry[0]));
			}
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static PC_ItemArmor registerItemArmor(PC_ModuleObject module, Class<? extends PC_ItemArmor> itemArmorClass){
		PC_Property config = module.getConfig().getProperty(itemArmorClass.getSimpleName(), null, null);
		try {
			if(!config.getBoolean("enabled", true)){
				return null;
			}
			int id = config.getInt("defaultID", -1);
			if(id==-1){
				id = getFreeItemID();
				config.setInt("defaultID", id);
			}
			if (!isItemIDFree(id)) {
				id = getFreeItemID();
			}
			PC_ItemArmor itemArmor = PC_ReflectHelper.create(itemArmorClass, id);
			PC_MSGRegistry.registerMSGObject(itemArmor);
			itemArmors.put(itemArmorClass.getSimpleName(),
					itemArmor);
			itemArmor.setItemName(itemArmorClass.getSimpleName());
			itemArmor.setModule(module);
			itemArmor.setTextureFile(PC_TextureRegistry.getTextureDirectory(module)+"tiles.png");

			itemArmor.msg(PC_MSGRegistry.MSG_LOAD_FROM_CONFIG, config);

			List<LangEntry> l = (List<LangEntry>) itemArmor
					.msg(PC_MSGRegistry.MSG_DEFAULT_NAME,
							new ArrayList<LangEntry>());
			if (l != null) {
				PC_LangRegistry.registerLanguage(module, l.toArray(new LangEntry[0]));
			}
			return itemArmor;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int getFreeItemID() {
		if (PC_GlobalVariables.itemStartIndex > Block.blocksList.length) {
			for (int i = PC_GlobalVariables.itemStartIndex; i < Item.itemsList.length; i++) {
				if (Item.itemsList[i] == null)
					return i;
			}
			for (int i = Block.blocksList.length; i < PC_GlobalVariables.itemStartIndex
					&& i < Item.itemsList.length; i++) {
				if (Item.itemsList[i] == null)
					return i;
			}
		} else {
			for (int i = Block.blocksList.length; i < Item.itemsList.length; i++) {
				if (Item.itemsList[i] == null)
					return i;
			}
		}
		return -1;
	}

	public static boolean isItemIDFree(int id) {
		if (id <= 0)
			return false;
		return Item.itemsList[id] == null;
	}
	
	public static PC_Item getPCItemByName(String name) {
		if (items.containsKey(name)) {
			return items.get(name);
		}
		return null;
	}
	
	public static int getPCItemIDByName(String name) {
		if (items.containsKey(name)) {
			return items.get(name).itemID;
		}
		return 0;
	}
	
	public static TreeMap<String, PC_Item> getPCItems() {
		return new TreeMap<String, PC_Item>(items);
	}
	
	public static PC_ItemArmor getPCItemArmorByName(String name) {
		if (itemArmors.containsKey(name)) {
			return itemArmors.get(name);
		}
		return null;
	}
	
	public static int getPCItemArmorIDByName(String name) {
		if (itemArmors.containsKey(name)) {
			return itemArmors.get(name).itemID;
		}
		return 0;
	}
	
	public static TreeMap<String, PC_ItemArmor> getPCItemArmors() {
		return new TreeMap<String, PC_ItemArmor>(itemArmors);
	}
	
}
