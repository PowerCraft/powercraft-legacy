package powercraft.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import powercraft.management.PC_GlobalVariables;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.gres.PC_GresBaseWithInventory;
import powercraft.management.inventory.PC_Slot;
import powercraft.management.item.PC_ItemStack;
import powercraft.management.tileentity.PC_TileEntity;

public class PCco_ContainerCraftingTool extends PC_GresBaseWithInventory<PC_TileEntity>
{
    protected PC_Slot trash;
    protected List<PCco_SlotDirectCrafting> allMcSlots;
    protected HashMap<String, List<PCco_SlotDirectCrafting>> moduleList;

    public PCco_ContainerCraftingTool(EntityPlayer player, PC_TileEntity te, Object[]o)
    {
        super(player, te, o);
    }

    @Override
    protected PC_Slot[] getAllSlots()
    {
    	while(!PCco_CraftingToolLoader.isFinished()){
    		System.out.println("lädt...");
    	}
    	
        allMcSlots = new ArrayList<PCco_SlotDirectCrafting>();
        for(PC_ItemStack is:PCco_CraftingToolLoader.getAllMcSlots()){
        	allMcSlots.add(new PCco_SlotDirectCrafting(thePlayer, is.toItemStack(), 0));
        }
        
        moduleList = new HashMap<String, List<PCco_SlotDirectCrafting>>();
        for(Entry<String, List<PC_ItemStack>> isl:PCco_CraftingToolLoader.getModuleList().entrySet()){
        	List<PCco_SlotDirectCrafting> dirCraftList = new ArrayList<PCco_SlotDirectCrafting>();
        	moduleList.put(isl.getKey(), dirCraftList);
        	for(PC_ItemStack is:isl.getValue()){
        		dirCraftList.add(new PCco_SlotDirectCrafting(thePlayer, is.toItemStack(), 0));
        	}
        	
        }
        List<PC_Slot> slots = new ArrayList<PC_Slot>();
        slots.add(trash = new PCco_SlotTrash());

        Collection<List<PCco_SlotDirectCrafting>> cls = moduleList.values();

        for (List<PCco_SlotDirectCrafting> ls: cls)
        {
        	for(PCco_SlotDirectCrafting s:ls){
        		slots.add(s);
        	}
        }

        slots.addAll(allMcSlots);
        return slots.toArray(new PC_Slot[0]);
    }

    @Override
    public void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer entityPlayer)
    {
        if (GameInfo.isCreative(entityPlayer) || PC_GlobalVariables.config.getBoolean("cheats.survivalCheating"))
        {
            return;
        }

        super.retrySlotClick(par1, par2, par3, entityPlayer);
    }

    public ItemStack slotClickCreative(int id, int par2, int par3, EntityPlayer player) {
    	ItemStack is = null;
        InventoryPlayer inv = player.inventory;
        Slot slot;
        ItemStack itemStack;
        int index;
        ItemStack stack;

        if ((par3 == 0 || par3 == 1) && (par2 == 0 || par2 == 1))
        {
            if (id == -999)
            {
                if (inv.getItemStack() != null && id == -999)
                {
                    if (par2 == 0)
                    {
                    	player.dropPlayerItem(inv.getItemStack());
                        inv.setItemStack((ItemStack)null);
                    }

                    if (par2 == 1)
                    {
                    	player.dropPlayerItem(inv.getItemStack().splitStack(1));

                        if (inv.getItemStack().stackSize == 0)
                        {
                            inv.setItemStack((ItemStack)null);
                        }
                    }
                }
            }
            else if (par3 == 1)
            {
                slot = (Slot)this.inventorySlots.get(id);

                if (slot != null && slot.canTakeStack(player))
                {
                    itemStack = this.transferStackInSlot(player, id);

                    if (itemStack != null)
                    {
                        int var12 = itemStack.itemID;
                        is = itemStack.copy();

                        if (slot != null && slot.getStack() != null && slot.getStack().itemID == var12)
                        {
                            this.retrySlotClick(id, par2, true, player);
                        }
                    }
                }
            }
            else
            {
                if (id < 0)
                {
                    return null;
                }

                slot = (Slot)this.inventorySlots.get(id);

                if(slot instanceof PCco_SlotDirectCrafting){
                	itemStack = slot.getStack();
                	ItemStack invIS = inv.getItemStack();
                	if(invIS==null){
                		inv.setItemStack(slot.decrStackSize(1));
                	}else if(itemStack.isItemEqual(invIS)){
                		itemStack = slot.decrStackSize(1);
                		invIS.stackSize += 1;
                		if(invIS.stackSize>Math.min(inv.getInventoryStackLimit(), invIS.getMaxStackSize())){
                			invIS.stackSize = Math.min(inv.getInventoryStackLimit(), invIS.getMaxStackSize());
                		}
                	}else{
                		inv.setItemStack(null);
                	}
                }else if (slot != null){
                    itemStack = slot.getStack();
                    ItemStack var13 = inv.getItemStack();

                    if (itemStack != null)
                    {
                        is = itemStack.copy();
                    }

                    if (itemStack == null)
                    {
                        if (var13 != null && slot.isItemValid(var13))
                        {
                            index = par2 == 0 ? var13.stackSize : 1;

                            if (index > slot.getSlotStackLimit())
                            {
                                index = slot.getSlotStackLimit();
                            }

                            slot.putStack(var13.splitStack(index));

                            if (var13.stackSize == 0)
                            {
                                inv.setItemStack((ItemStack)null);
                            }
                        }
                    }
                    else if (slot.canTakeStack(player))
                    {
                        if (var13 == null)
                        {
                            index = par2 == 0 ? itemStack.stackSize : (itemStack.stackSize + 1) / 2;
                            stack = slot.decrStackSize(index);
                            inv.setItemStack(stack);

                            if (itemStack.stackSize == 0)
                            {
                                slot.putStack((ItemStack)null);
                            }

                            slot.onPickupFromSlot(player, inv.getItemStack());
                        }
                        else if (slot.isItemValid(var13))
                        {
                            if (itemStack.itemID == var13.itemID && itemStack.getItemDamage() == var13.getItemDamage() && ItemStack.areItemStackTagsEqual(itemStack, var13))
                            {
                                index = par2 == 0 ? var13.stackSize : 1;

                                if (index > slot.getSlotStackLimit() - itemStack.stackSize)
                                {
                                    index = slot.getSlotStackLimit() - itemStack.stackSize;
                                }

                                if (index > var13.getMaxStackSize() - itemStack.stackSize)
                                {
                                    index = var13.getMaxStackSize() - itemStack.stackSize;
                                }

                                var13.splitStack(index);

                                if (var13.stackSize == 0)
                                {
                                    inv.setItemStack((ItemStack)null);
                                }

                                itemStack.stackSize += index;
                            }
                            else if (var13.stackSize <= slot.getSlotStackLimit())
                            {
                                slot.putStack(var13);
                                inv.setItemStack(itemStack);
                            }
                        }
                        else if (itemStack.itemID == var13.itemID && var13.getMaxStackSize() > 1 && (!itemStack.getHasSubtypes() || itemStack.getItemDamage() == var13.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemStack, var13))
                        {
                            index = itemStack.stackSize;

                            if (index > 0 && index + var13.stackSize <= var13.getMaxStackSize())
                            {
                                var13.stackSize += index;
                                itemStack = slot.decrStackSize(index);

                                if (itemStack.stackSize == 0)
                                {
                                    slot.putStack((ItemStack)null);
                                }

                                slot.onPickupFromSlot(player, inv.getItemStack());
                            }
                        }
                    }

                    slot.onSlotChanged();
                }
            }
        }
        else if (par3 == 2 && par2 >= 0 && par2 < 9)
        {
            slot = (Slot)this.inventorySlots.get(id);

            if (slot.canTakeStack(player))
            {
                itemStack = inv.getStackInSlot(par2);
                boolean var9 = itemStack == null || slot.inventory == inv && slot.isItemValid(itemStack);
                index = -1;

                if (!var9)
                {
                    index = inv.getFirstEmptyStack();
                    var9 |= index > -1;
                }

                if (slot.getHasStack() && var9)
                {
                    stack = slot.getStack();
                    inv.setInventorySlotContents(par2, stack);

                    if ((slot.inventory != inv || !slot.isItemValid(itemStack)) && itemStack != null)
                    {
                        if (index > -1)
                        {
                            inv.addItemStackToInventory(itemStack);
                            slot.decrStackSize(stack.stackSize);
                            slot.putStack((ItemStack)null);
                            slot.onPickupFromSlot(player, stack);
                        }
                    }
                    else
                    {
                        slot.decrStackSize(stack.stackSize);
                        slot.putStack(itemStack);
                        slot.onPickupFromSlot(player, stack);
                    }
                }
                else if (!slot.getHasStack() && itemStack != null && slot.isItemValid(itemStack))
                {
                    inv.setInventorySlotContents(par2, (ItemStack)null);
                    slot.putStack(itemStack);
                }
            }
        }
        else if (par3 == 3 && player.capabilities.isCreativeMode && inv.getItemStack() == null && id >= 0)
        {
            slot = (Slot)this.inventorySlots.get(id);

            if (slot != null && slot.getHasStack())
            {
                itemStack = slot.getStack().copy();
                itemStack.stackSize = itemStack.getMaxStackSize();
                inv.setItemStack(itemStack);
            }
        }

        return is;
    }
    
	@Override
	public ItemStack slotClick(int id, int par2, int par3, EntityPlayer player) {
		ItemStack is;
		if(GameInfo.isCreative(player) || PC_GlobalVariables.config.getBoolean("cheats.survivalCheating")){
			is = slotClickCreative(id, par2, par3, player);
		}else{
			is = super.slotClick(id, par2, par3, player);
		}
		for(PCco_SlotDirectCrafting slot:allMcSlots){
			slot.updateAvailable();
		}
		for(List<PCco_SlotDirectCrafting> slots:moduleList.values()){
			for(PCco_SlotDirectCrafting slot:slots){
				slot.updateAvailable();
			}
		}
		return is;
	}
    
    
    
}
