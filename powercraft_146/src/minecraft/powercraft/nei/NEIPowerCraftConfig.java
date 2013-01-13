package powercraft.nei;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import powercraft.management.PC_Block;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_Logger;
import powercraft.management.PC_Shining;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.ModuleLoader;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.mod_PowerCraft;
import powercraft.management.PC_Utils.ModuleInfo;
import codechicken.nei.ItemList;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.api.ItemInfo;

public class NEIPowerCraftConfig implements IConfigureNEI, PC_IMSG {

	public NEIPowerCraftConfig(){
		 ModuleInfo.registerMSGObject(this);
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
		List<Object> objs = ModuleInfo.getRegisterdObjects();
		
		for(Object obj:objs){
			
			if(obj instanceof PC_IMSG){
				Object msg = ((PC_IMSG) obj).msg(PC_Utils.MSG_DONT_SHOW_IN_CRAFTING_TOOL);
				int id=0;
				if (obj instanceof Item){
					id = ((Item)obj).shiftedIndex;
				}else if (obj instanceof Block){
		        	id = ((Block)obj).blockID;
		        	if(obj.getClass().isAnnotationPresent(PC_Shining.class)){
		        		System.out.println("A shinig thing");
		        		Object[] o = ValueWriting.getFieldsWithAnnotation(obj.getClass(), PC_Shining.OFF.class, obj);
		        		for(int i=0; i<o.length; i++){
		        			if(o[i]==obj){
		        				System.out.println("is not shining");
		        				msg = true;
		        			}
		        		}
		        	}
		        }
				if(msg instanceof Boolean && (Boolean)msg){
					API.hideItem(id);
				}else{
					ItemInfo.excludeIds.remove(Integer.valueOf(id));
				}
			}
			
		}
		
		System.out.println("Reload NEI items");
		
		ItemList.loadItems();
		
	}
	
	@Override
	public Object msg(int msg, Object... obj) {
		if(msg==PC_Utils.MSG_LOAD_WORLD){
			registerNEIItems();
		}
		return null;
	}

}
