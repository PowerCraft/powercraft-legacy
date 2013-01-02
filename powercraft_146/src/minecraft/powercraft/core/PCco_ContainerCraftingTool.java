package powercraft.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_IItemInfo;
import powercraft.management.PC_IMSG;
import powercraft.management.PC_IModule;
import powercraft.management.PC_ItemStack;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.GameInfo;

public class PCco_ContainerCraftingTool extends PC_GresBaseWithInventory
{
    protected Slot trash;
    protected List<PCco_SlotDirectCrafting> allMcSlots;
    protected HashMap<String, List<PCco_SlotDirectCrafting>> moduleList;

    public PCco_ContainerCraftingTool(EntityPlayer player, Object[]o)
    {
        super(player, o);
    }

    @Override
    protected void init(Object[] o) {}

    @Override
    protected List<Slot> getAllSlots(List<Slot> slots)
    {
    	while(!PCco_CraftingToolLoader.isFinished()){
    		System.out.println("lädt...");
    	}
    	
        allMcSlots = new ArrayList<PCco_SlotDirectCrafting>();
        for(PC_ItemStack is:PCco_CraftingToolLoader.getAllMcSlots()){
        	allMcSlots.add(new PCco_SlotDirectCrafting(thePlayer, is.toItemStack(), 0, -444, -444));
        }
        
        moduleList = new HashMap<String, List<PCco_SlotDirectCrafting>>();
        for(Entry<String, List<PC_ItemStack>> isl:PCco_CraftingToolLoader.getModuleList().entrySet()){
        	List<PCco_SlotDirectCrafting> dirCraftList = new ArrayList<PCco_SlotDirectCrafting>();
        	moduleList.put(isl.getKey(), dirCraftList);
        	for(PC_ItemStack is:isl.getValue()){
        		dirCraftList.add(new PCco_SlotDirectCrafting(thePlayer, is.toItemStack(), 0, -444, -444));
        	}
        	
        }
        slots.add(trash = new PCco_SlotTrash());

        Collection<List<PCco_SlotDirectCrafting>> cls = moduleList.values();

        for (List<PCco_SlotDirectCrafting> ls: cls)
        {
        	for(PCco_SlotDirectCrafting s:ls){
        		slots.add(s);
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
