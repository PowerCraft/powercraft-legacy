package powercraft.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_IItemInfo;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;

public class PCco_ContainerCraftingTool extends PC_GresBaseWithInventory
{
    protected Slot trash;
    protected List<PCco_SlotDirectCrafting> allMcSlots;
    protected HashMap<String, List<PC_Struct2<PC_IModule, PCco_SlotDirectCrafting>>> moduleList;

    public PCco_ContainerCraftingTool(EntityPlayer player, Object[]o)
    {
        super(player, o);
    }

    @Override
    protected void init(Object[] o) {}

    @Override
    protected List<Slot> getAllSlots(List<Slot> slots)
    {
        allMcSlots = new ArrayList<PCco_SlotDirectCrafting>();
        moduleList = new HashMap<String, List<PC_Struct2<PC_IModule, PCco_SlotDirectCrafting>>>();
        slots.add(trash = new PCco_SlotTrash());

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
                    List<PC_Struct2<PC_IModule, PCco_SlotDirectCrafting>> ls;

                    if(module!=null){
                    
	                    if (moduleList.containsKey(module.getName()))
	                    {
	                        ls = moduleList.get(module.getName());
	                    }
	                    else
	                    {
	                        moduleList.put(module.getName(), ls = new ArrayList<PC_Struct2<PC_IModule, PCco_SlotDirectCrafting>>());
	                    }
	
	                    for (ItemStack is: l)
	                    {
	                        ls.add(new PC_Struct2<PC_IModule, PCco_SlotDirectCrafting>(module, new PCco_SlotDirectCrafting(thePlayer, is, 0, -444, -444)));
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
                        List<PC_Struct2<PC_IModule, PCco_SlotDirectCrafting>> ls;

                        if (moduleList.containsKey(module.getName()))
                        {
                            ls = moduleList.get(module.getName());
                        }
                        else
                        {
                            moduleList.put(module.getName(), ls = new ArrayList<PC_Struct2<PC_IModule, PCco_SlotDirectCrafting>>());
                        }

                        for (ItemStack is: l)
                        {
                            ls.add(new PC_Struct2<PC_IModule, PCco_SlotDirectCrafting>(module, new PCco_SlotDirectCrafting(thePlayer, is, 0, -444, -444)));
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
                            allMcSlots.add(new PCco_SlotDirectCrafting(thePlayer, is, 0, -444, -444));
                        }
                    }

                    continue;
                }

                ItemStack is = new ItemStack(i);

                if (GameInfo.getRecipesForProduct(is).size() > 0)
                {
                    allMcSlots.add(new PCco_SlotDirectCrafting(thePlayer, is, 0, -444, -444));
                }
            }
        }

        Collection<List<PC_Struct2<PC_IModule, PCco_SlotDirectCrafting>>> cls = moduleList.values();

        for (List<PC_Struct2<PC_IModule, PCco_SlotDirectCrafting>> ls: cls)
        {
        	for(PC_Struct2<PC_IModule, PCco_SlotDirectCrafting>s:ls){
        		slots.add(s.b);
        	}
        }

        slots.addAll(allMcSlots);
        return slots;
    }

    @Override
    public void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer entityPlayer)
    {
        if (GameInfo.isCreative(entityPlayer) || PCco_SlotDirectCrafting.survivalCheating)
        {
            return;
        }

        super.retrySlotClick(par1, par2, par3, entityPlayer);
    }
}
