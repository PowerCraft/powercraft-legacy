package powercraft.management.nei;

import java.util.List;
import java.util.TreeMap;

import powercraft.management.PC_IMSG;
import powercraft.management.PC_Logger;
import powercraft.management.mod_PowerCraft;
import powercraft.management.annotation.PC_Shining;
import powercraft.management.block.PC_Block;
import powercraft.management.item.PC_Item;
import powercraft.management.item.PC_ItemArmor;
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
			boolean show = block.showInCraftingTool();
	        if(block.getClass().isAnnotationPresent(PC_Shining.class)){
	        	List<Object> fields = PC_ReflectHelper.getFieldsWithAnnotation(block.getClass(), block, PC_Shining.OFF.class);
	        	for(Object obj:fields){
	        		if(obj==block){
	        			show = true;
	        		}
	        	}
	        }
			if(show){
				ItemInfo.excludeIds.remove(Integer.valueOf(block.blockID));
			}else{
				API.hideItem(block.blockID);
			}
		}
		
		for(PC_Item item:items.values()){
			if(item.showInCraftingTool()){
				ItemInfo.excludeIds.remove(Integer.valueOf(item.itemID));
			}else{
				API.hideItem(item.itemID);
			}
		}
		
		for(PC_ItemArmor itemArmor:itemArmors.values()){
			if(itemArmor.showInCraftingTool()){
				ItemInfo.excludeIds.remove(Integer.valueOf(itemArmor.itemID));
			}else{
				API.hideItem(itemArmor.itemID);
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
