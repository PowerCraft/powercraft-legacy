package powercraft.api.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import powercraft.api.annotation.PC_Config;
import powercraft.api.item.PC_Item;
import powercraft.api.item.PC_ItemArmor;
import powercraft.api.reflect.PC_FieldWithAnnotation;
import powercraft.api.reflect.PC_IFieldAnnotationIterator;
import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.registry.PC_LangRegistry.LangEntry;
import powercraft.api.utils.PC_GlobalVariables;
import powercraft.launcher.PC_Property;
import powercraft.launcher.loader.PC_ModuleObject;

public final class PC_ItemRegistry {
	
	private static TreeMap<String, PC_Item> items = new TreeMap<String, PC_Item>();
	private static TreeMap<String, PC_ItemArmor> itemArmors = new TreeMap<String, PC_ItemArmor>();
	
	public static <T extends Item> T register(PC_ModuleObject module, Class<T> c) {
		if (PC_Item.class.isAssignableFrom(c)) {
			return (T) registerItem(module, (Class<? extends PC_Item>) c);
		} else if (PC_ItemArmor.class.isAssignableFrom(c)) {
			return (T) registerItemArmor(module, (Class<? extends PC_ItemArmor>) c);
		} else {
			throw new IllegalArgumentException("Expect class of PC_Item or PC_ItemArmor");
		}
	}
	
	public static PC_Item registerItem(PC_ModuleObject module, Class<? extends PC_Item> itemClass) {
		final PC_Property config = module.getConfig().getProperty(itemClass.getSimpleName(), null, null);
		try {
			if (!config.getBoolean("enabled", true)) {
				return null;
			}
			
			int id = config.getInt("defaultID", -1);
			if (id == -1) {
				id = getFreeItemID();
				config.setInt("defaultID", id);
			}
			if (!isItemIDFree(id)) {
				id = getFreeItemID();
			}
			PC_Item item = PC_ReflectHelper.create(itemClass, id);
			PC_MSGRegistry.registerMSGObject(item);
			items.put(itemClass.getSimpleName(), item);
			item.setUnlocalizedName(itemClass.getSimpleName());
			item.setModule(module);
			
			PC_ReflectHelper.getAllFieldsWithAnnotation(itemClass, item, PC_Config.class, new PC_IFieldAnnotationIterator<PC_Config>() {
				
				@Override
				public boolean onFieldWithAnnotation(PC_FieldWithAnnotation<PC_Config> fieldWithAnnotation) {
					Class<?> c = fieldWithAnnotation.getFieldClass();
					String name = fieldWithAnnotation.getAnnotation().name();
					if (name.equals("")) {
						name = fieldWithAnnotation.getFieldName();
					}
					String[] comment = fieldWithAnnotation.getAnnotation().comment();
					if (c == String.class) {
						String data = (String) fieldWithAnnotation.getValue();
						data = config.getString(name, data, comment);
						fieldWithAnnotation.setValue(data);
					} else if (c == Integer.class || c == int.class) {
						int data = (Integer) fieldWithAnnotation.getValue();
						data = config.getInt(name, data, comment);
						fieldWithAnnotation.setValue(data);
					} else if (c == Float.class || c == float.class) {
						float data = (Float) fieldWithAnnotation.getValue();
						data = config.getFloat(name, data, comment);
						fieldWithAnnotation.setValue(data);
					} else if (c == Boolean.class || c == boolean.class) {
						boolean data = (Boolean) fieldWithAnnotation.getValue();
						data = config.getBoolean(name, data, comment);
						fieldWithAnnotation.setValue(data);
					}
					return false;
				}
			});
			
			item.msg(PC_MSGRegistry.MSG_LOAD_FROM_CONFIG, config);
			
			List<LangEntry> l = (List<LangEntry>) item.msg(PC_MSGRegistry.MSG_DEFAULT_NAME, new ArrayList<LangEntry>());
			if (l != null) {
				PC_LangRegistry.registerLanguage(module, l.toArray(new LangEntry[0]));
			}
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static PC_ItemArmor registerItemArmor(PC_ModuleObject module, Class<? extends PC_ItemArmor> itemArmorClass) {
		final PC_Property config = module.getConfig().getProperty(itemArmorClass.getSimpleName(), null, null);
		try {
			if (!config.getBoolean("enabled", true)) {
				return null;
			}
			int id = config.getInt("defaultID", -1);
			if (id == -1) {
				id = getFreeItemID();
				config.setInt("defaultID", id);
			}
			if (!isItemIDFree(id)) {
				id = getFreeItemID();
			}
			PC_ItemArmor itemArmor = PC_ReflectHelper.create(itemArmorClass, id);
			PC_MSGRegistry.registerMSGObject(itemArmor);
			itemArmors.put(itemArmorClass.getSimpleName(), itemArmor);
			itemArmor.setUnlocalizedName(itemArmorClass.getSimpleName());
			itemArmor.setModule(module);
			
			itemArmor.msg(PC_MSGRegistry.MSG_LOAD_FROM_CONFIG, config);
			
			PC_ReflectHelper.getAllFieldsWithAnnotation(itemArmorClass, itemArmor, PC_Config.class, new PC_IFieldAnnotationIterator<PC_Config>() {
				
				@Override
				public boolean onFieldWithAnnotation(PC_FieldWithAnnotation<PC_Config> fieldWithAnnotation) {
					Class<?> c = fieldWithAnnotation.getFieldClass();
					String name = fieldWithAnnotation.getAnnotation().name();
					if (name.equals("")) {
						name = fieldWithAnnotation.getFieldName();
					}
					String[] comment = fieldWithAnnotation.getAnnotation().comment();
					if (c == String.class) {
						String data = (String) fieldWithAnnotation.getValue();
						data = config.getString(name, data, comment);
						fieldWithAnnotation.setValue(data);
					} else if (c == Integer.class || c == int.class) {
						int data = (Integer) fieldWithAnnotation.getValue();
						data = config.getInt(name, data, comment);
						fieldWithAnnotation.setValue(data);
					} else if (c == Float.class || c == float.class) {
						float data = (Float) fieldWithAnnotation.getValue();
						data = config.getFloat(name, data, comment);
						fieldWithAnnotation.setValue(data);
					} else if (c == Boolean.class || c == boolean.class) {
						boolean data = (Boolean) fieldWithAnnotation.getValue();
						data = config.getBoolean(name, data, comment);
						fieldWithAnnotation.setValue(data);
					}
					return false;
				}
			});
			
			List<LangEntry> l = (List<LangEntry>) itemArmor.msg(PC_MSGRegistry.MSG_DEFAULT_NAME, new ArrayList<LangEntry>());
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
			for (int i = Block.blocksList.length; i < PC_GlobalVariables.itemStartIndex && i < Item.itemsList.length; i++) {
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
