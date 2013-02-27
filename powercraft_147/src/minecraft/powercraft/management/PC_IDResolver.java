package powercraft.management;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.annotation.PC_Shining;
import powercraft.management.block.PC_Block;
import powercraft.management.item.PC_Item;
import powercraft.management.item.PC_ItemArmor;
import powercraft.management.reflect.PC_ReflectHelper;
import powercraft.management.registry.PC_BlockRegistry;
import powercraft.management.registry.PC_ItemRegistry;
import powercraft.management.registry.PC_ModuleRegistry;
import powercraft.management.registry.PC_RecipeRegistry;

public class PC_IDResolver {

	public static boolean isEnabled(){
		return PC_GlobalVariables.idResolve;
	}
	
	public static NBTTagCompound makeIDTagCompound() {
		NBTTagCompound nbttag = new NBTTagCompound("PowerCraftIDs");

		TreeMap<String, PC_Block> blocks = PC_BlockRegistry.getPCBlocks();
		TreeMap<String, PC_Item> items = PC_ItemRegistry.getPCItems();
		TreeMap<String, PC_ItemArmor> itemArmors = PC_ItemRegistry.getPCItemArmors();
		
		for (String key : blocks.keySet()) {
			nbttag.setInteger(key, blocks.get(key).blockID);
		}

		for (String key : items.keySet()) {
			nbttag.setInteger(key, items.get(key).itemID);
		}
		
		for (String key : itemArmors.keySet()) {
			nbttag.setInteger(key, itemArmors.get(key).itemID);
		}
		
		return nbttag;
	}
	
	public static void loadIDFromTagCompound(NBTTagCompound nbttag, boolean defaultOthers) {
		if (!PC_GlobalVariables.idResolve)
			return;
		
		PC_RecipeRegistry.unloadSmeltRecipes();
		PC_OreDictionary.unloadOres();
		
		TreeMap<String, PC_Block> blocks = PC_BlockRegistry.getPCBlocks();
		TreeMap<String, PC_Item> items = PC_ItemRegistry.getPCItems();
		TreeMap<String, PC_ItemArmor> itemArmors = PC_ItemRegistry.getPCItemArmors();
		
		for (PC_Block block : blocks.values()) {
			block.setID(-1);
		}

		for (PC_Item item : items.values()) {
			item.setID(-1);
		}
		
		for (PC_ItemArmor imteArmor : itemArmors.values()) {
			imteArmor.setID(-1);
		}
		
		for (Entry<String, PC_Block> e : blocks.entrySet()) {
			if (nbttag.hasKey(e.getKey())) {
				e.getValue().setID(nbttag.getInteger(e.getKey()));
			}
		}
		
		for (Entry<String, PC_Item> e : items.entrySet()) {
			if (nbttag.hasKey(e.getKey())) {
				e.getValue().setID(nbttag.getInteger(e.getKey()));
			}
		}
		
		for (Entry<String, PC_ItemArmor> e : itemArmors.entrySet()) {
			if (nbttag.hasKey(e.getKey())) {
				e.getValue().setID(nbttag.getInteger(e.getKey()));
			}
		}
		
		if(defaultOthers){
		
			for (Entry<String, PC_Block> e : blocks.entrySet()) {
				if (!nbttag.hasKey(e.getKey())) {
					e.getValue().setID(defaultID(e.getValue()));
				}
			}
			
			for (Entry<String, PC_Item> e : items.entrySet()) {
				if (!nbttag.hasKey(e.getKey())) {
					e.getValue().setID(defaultID(e.getValue()));
				}
			}
			
			for (Entry<String, PC_ItemArmor> e : itemArmors.entrySet()) {
				if (!nbttag.hasKey(e.getKey())) {
					e.getValue().setID(defaultID(e.getValue()));
				}
			}
		
		}
		
		PC_OreDictionary.loadOres();
		PC_RecipeRegistry.loadSmeltRecipes();
		
	}
	
	public static void savePCObjectsIDs(File worldDirectory) {
		NBTTagCompound nbttag = makeIDTagCompound();

		try {
			File file = new File(worldDirectory, "powercraft.dat");
			CompressedStreamTools.writeCompressed(nbttag,
					new FileOutputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean loadPCObjectsIDs(File worldDirectory) {
		File file = new File(worldDirectory, "powercraft.dat");
		if (!file.exists())
			return false;
		try {
			loadIDFromTagCompound(CompressedStreamTools
					.readCompressed(new FileInputStream(file)), true);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static void resetPCObjectsIDs() {
		if (!PC_GlobalVariables.idResolve)
			return;
		
		PC_RecipeRegistry.unloadSmeltRecipes();
		PC_OreDictionary.unloadOres();
		
		TreeMap<String, PC_Block> blocks = PC_BlockRegistry.getPCBlocks();
		TreeMap<String, PC_Item> items = PC_ItemRegistry.getPCItems();
		TreeMap<String, PC_ItemArmor> itemArmors = PC_ItemRegistry.getPCItemArmors();
		
		for (PC_Block block : blocks.values()) {
			block.setID(-1);
		}

		for (PC_Item item : items.values()) {
			item.setID(-1);
		}
		
		for (PC_ItemArmor imteArmor : itemArmors.values()) {
			imteArmor.setID(-1);
		}
		
		for (Entry<String, PC_Block> e : blocks.entrySet()) {
			e.getValue().setID(defaultID(e.getValue()));
		}
		
		for (Entry<String, PC_Item> e : items.entrySet()) {
			e.getValue().setID(defaultID(e.getValue()));
		}
		
		for (Entry<String, PC_ItemArmor> e : itemArmors.entrySet()) {
			e.getValue().setID(defaultID(e.getValue()));
		}
		
		PC_OreDictionary.loadOres();
		PC_RecipeRegistry.loadSmeltRecipes();
		
	}
	
	public static int defaultID(PC_Block block) {
		PC_IModule module = block.getModule();
		Class<?> c = block.getClass();
		int id = -1;
		if(PC_ReflectHelper.getAnnotation(c, PC_Shining.class)!=null){
			List<Object> on = PC_ReflectHelper.getFieldsWithAnnotation(c, block,
							PC_Shining.ON.class);
			List<Object> off = PC_ReflectHelper.getFieldsWithAnnotation(c, block,
							PC_Shining.OFF.class);
			if (on != null && on.size() > 0 && on.get(0) == block) {
				id = PC_ModuleRegistry.getConfig(module).getInt(
						c.getSimpleName() + ".defaultID.on");
			} else {
				id = PC_ModuleRegistry.getConfig(module).getInt(
						c.getSimpleName() + ".defaultID.off");
			}
		}else{
			id = PC_ModuleRegistry.getConfig(module).getInt(c.getSimpleName() + ".defaultID");
		}
		if (!PC_BlockRegistry.isBlockIDFree(id))
			id = PC_BlockRegistry.getFreeBlockID();
		return id;
	}
	
	public static int defaultID(PC_Item item) {
		PC_IModule module = item.getModule();
		int id = PC_ModuleRegistry.getConfig(module).getInt(item.getClass().getSimpleName() + ".defaultID");
		if (!PC_ItemRegistry.isItemIDFree(id))
			id = PC_ItemRegistry.getFreeItemID();
		return id;
	}
	
	public static int defaultID(PC_ItemArmor itemArmor) {
		PC_IModule module = itemArmor.getModule();
		int id = PC_ModuleRegistry.getConfig(module).getInt(itemArmor.getClass().getSimpleName() + ".defaultID");
		if (!PC_ItemRegistry.isItemIDFree(id))
			id = PC_ItemRegistry.getFreeItemID();
		return id;
	}
	
}
