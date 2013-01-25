package powercraft.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import powercraft.management.PC_IItemInfo;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;

public class PCco_CraftingToolLoader extends Thread implements PC_IMSG{

    private volatile static List<PC_ItemStack> allMcSlots;
    private volatile static HashMap<String, List<PC_ItemStack>> moduleList;
    private volatile static boolean finished=false;
    
    public PCco_CraftingToolLoader() {
        allMcSlots=null;
        moduleList=null;
        finished=false;
	}
    
	@Override
	public void run() {
		allMcSlots = new ArrayList<PC_ItemStack>();
        moduleList = new HashMap<String, List<PC_ItemStack>>();

        for (Item i: Item.itemsList)
        {
            if (i != null)
            {
                List<ItemStack> a = new ArrayList<ItemStack>();
                
                if (i instanceof PC_IItemInfo)
                {
                	PC_IModule module = ((PC_IItemInfo)i).getModule();
                    List<ItemStack> l = ((PC_IItemInfo)i).getItemStacks(new ArrayList<ItemStack>());
                    if(i instanceof PC_IMSG){
                    	Object o = ((PC_IMSG) i).msg(PC_Utils.MSG_DONT_SHOW_IN_CRAFTING_TOOL);
                    	if(o instanceof Boolean && (Boolean)o){
                    		continue;
                    	}
                    }
                    List<PC_ItemStack> ls;

                    if(module!=null){
                    
	                    if (moduleList.containsKey(module.getName()))
	                    {
	                        ls = moduleList.get(module.getName());
	                    }
	                    else
	                    {
	                        moduleList.put(module.getName(), ls = new ArrayList<PC_ItemStack>());
	                    }
	
	                    for (ItemStack is: l)
	                    {
	                    	if(is.itemID>=0){
	                    		ls.add(new PC_ItemStack(is));
	                    	}
	                    }

                    }
                    
                    continue;
                }

                if (i instanceof ItemBlock)
                {
                    int id = ((ItemBlock)i).getBlockID();
                    Block b = Block.blocksList[id];

                    if (b != null && b instanceof PC_IItemInfo)
                    {
                    	PC_IModule module = ((PC_IItemInfo)b).getModule();
                        List<ItemStack> l = ((PC_IItemInfo)b).getItemStacks(new ArrayList<ItemStack>());
                        if(b instanceof PC_IMSG){
                        	Object o = ((PC_IMSG) b).msg(PC_Utils.MSG_DONT_SHOW_IN_CRAFTING_TOOL);
                        	if(o instanceof Boolean && (Boolean)o){
                        		continue;
                        	}
                        }
                        List<PC_ItemStack> ls;

                        if (moduleList.containsKey(module.getName()))
                        {
                            ls = moduleList.get(module.getName());
                        }
                        else
                        {
                            moduleList.put(module.getName(), ls = new ArrayList<PC_ItemStack>());
                        }

                        for (ItemStack is: l)
                        {
                            ls.add(new PC_ItemStack(is));
                        }

                        continue;
                    }
                }

                if (i.getHasSubtypes())
                {
                    for (int j = 0; j < 16; j++)
                    {
                        ItemStack is = new ItemStack(i, 1, j);

                        if (GameInfo.getRecipesForProduct(is).size() > 0)
                        {
                            allMcSlots.add(new PC_ItemStack(is));
                        }
                    }

                    continue;
                }

                ItemStack is = new ItemStack(i);

                if (GameInfo.getRecipesForProduct(is).size() > 0)
                {
                    allMcSlots.add(new PC_ItemStack(is));
                }
            }
        }
        finished=true;
	}

	public static List<PC_ItemStack> getAllMcSlots() {
		return allMcSlots;
	}

	public static HashMap<String, List<PC_ItemStack>> getModuleList() {
		return moduleList;
	}

	public static boolean isFinished() {
		return finished;
	}

	@Override
	public Object msg(int msg, Object... obj) {
		switch(msg){
			case PC_Utils.MSG_LOAD_WORLD:{
				new PCco_CraftingToolLoader().start();
				return null;
			}
		}
		return null;
	}

}
