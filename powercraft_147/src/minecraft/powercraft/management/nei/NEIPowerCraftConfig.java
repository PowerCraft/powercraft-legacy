package powercraft.management.nei;

import java.util.List;
import java.util.TreeMap;

import powercraft.management.PC_Block;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_Item;
import powercraft.management.PC_ItemArmor;
import powercraft.management.PC_Logger;
import powercraft.management.mod_PowerCraft;
import powercraft.management.annotation.PC_Shining;
import powercraft.management.reflect.PC_ReflectHelper;
import powercraft.management.registry.PC_BlockRegistry;
import powercraft.management.registry.PC_ItemRegistry;
import powercraft.management.registry.PC_MSGRegistry;
import codechicken.nei.ItemList;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.api.ItemInfo;

public class NEIPowerCraftConfig implements IConfigureNEI, PC_IMSG {

	public NEIPowerCraftConfig(){
		 PC_MSGRegistry.registerMSGObject(this);
	}
	
	@Override
	public void loadConfig() {
		PC_Logger.enterSection("Loading NEI configuration");
		
		PC_ShapedRecipeHandler shapedRecipeHandler = new PC_ShapedRecipeHandler();
		API.registerRecipeHandler(shapedRecipeHandler);
		API.registerUsageHandler(shapedRecipeHandler);
		
		PC_ShapelessRecipeHandler shapelessRecipeHandler = new PC_ShapelessRecipeHandler();
		API.registerRecipeHandler(shapelessRecipeHandler);
		API.registerUsageHandler(shapelessRecipeHandler);
		
		API.registerNEIGuiHandler(new PC_NEIGuiHandler());
		
		registerNEIItems();
		
		PC_Logger.exitSection();
	}

	@Override
	public String getName() {
		return mod_PowerCraft.getInstance().getModMetadata().name;
	}

	@Override
	public String getVersion() {
		return mod_PowerCraft.getInstance().getModMetadata().version;
	}

	private void registerNEIItems(){
		
		TreeMap<String, PC_Block> blocks = PC_BlockRegistry.getPCBlocks();
		TreeMap<String, PC_Item> items = PC_ItemRegistry.getPCItems();
		TreeMap<String, PC_ItemArmor> itemArmors = PC_ItemRegistry.getPCItemArmors();
		
		for(PC_Block block:blocks.values()){
			Object o = block.msg(PC_MSGRegistry.MSG_DONT_SHOW_IN_CRAFTING_TOOL);
	        if(block.getClass().isAnnotationPresent(PC_Shining.class)){
	        	List<Object> fields = PC_ReflectHelper.getFieldsWithAnnotation(block.getClass(), block, PC_Shining.OFF.class);
	        	for(Object obj:fields){
	        		if(obj==block){
	        			o = true;
	        		}
	        	}
	        }
			if(o instanceof Boolean && (Boolean)o){
				API.hideItem(block.blockID);
			}else{
				ItemInfo.excludeIds.remove(Integer.valueOf(block.blockID));
			}
		}
		
		for(PC_Item item:items.values()){
			Object o = item.msg(PC_MSGRegistry.MSG_DONT_SHOW_IN_CRAFTING_TOOL);
			if(o instanceof Boolean && (Boolean)o){
				API.hideItem(item.itemID);
			}else{
				ItemInfo.excludeIds.remove(Integer.valueOf(item.itemID));
			}
		}
		
		for(PC_ItemArmor itemArmor:itemArmors.values()){
			Object o = itemArmor.msg(PC_MSGRegistry.MSG_DONT_SHOW_IN_CRAFTING_TOOL);
			if(o instanceof Boolean && (Boolean)o){
				API.hideItem(itemArmor.itemID);
			}else{
				ItemInfo.excludeIds.remove(Integer.valueOf(itemArmor.itemID));
			}
		}
		
		
		System.out.println("Reload NEI items");
		
		ItemList.loadItems();
		
	}
	
	@Override
	public Object msg(int msg, Object... obj) {
		if(msg==PC_MSGRegistry.MSG_LOAD_WORLD){
			registerNEIItems();
		}
		return null;
	}

}
