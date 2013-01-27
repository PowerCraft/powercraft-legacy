package powercraft.management;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class PC_GresBaseWithInventory extends Container
{
    public EntityPlayer thePlayer;

    private static final int playerSlots = 9 * 4;

    public Slot[][] inventoryPlayerUpper = new Slot[9][3];

    public Slot[][] inventoryPlayerLower = new Slot[9][1];

    public PC_GresBaseWithInventory(EntityPlayer player, Object[] o)
    {
        thePlayer = player;

        if (thePlayer != null)
        {
            for (int i = 0; i < 9; i++)
            {
                inventoryPlayerLower[i][0] = new Slot(player.inventory, i, -3000, 0);
                addSlotToContainer(inventoryPlayerLower[i][0]);
            }

            for (int i = 0; i < 9; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    inventoryPlayerUpper[i][j] = new Slot(player.inventory, i + j * 9 + 9, -3000, 0);
                    addSlotToContainer(inventoryPlayerUpper[i][j]);
                }
            }
        }

        init(o);
        List<Slot> sl = getAllSlots(new ArrayList<Slot>());

        if (sl != null)
            for (Slot s: sl)
            {
                addSlotToContainer(s);
            }
    }

    protected abstract void init(Object[] o);
    protected abstract List<Slot> getAllSlots(List<Slot> slots);

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    protected boolean canShiftTransfer()
    {
        return false;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        if (slotIndex < playerSlots && !canShiftTransfer())
        {
            return null;
        }

        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex < playerSlots)
            {
                if (!mergeItemStack(itemstack1, playerSlots, inventorySlots.size(), false))
                {
                    return null;
                }
                else
                {
                    slot.onPickupFromSlot(player, itemstack);
                }
            }
            else if (!mergeItemStack(itemstack1, 0, playerSlots, false))
            {
                return null;
            }
            else
            {
                slot.onPickupFromSlot(player, itemstack);
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
                slot.onSlotChanged();
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    private int getLimit(Slot slot, int a, boolean flag)
    {
        if (flag)
        {
            return a;
        }

        return Math.min(a, slot.getSlotStackLimit());
    }

    @Override
    protected boolean mergeItemStack(ItemStack itemstack, int i, int j, boolean flag)
    {
        boolean flag1 = false;
        int k = i;

        if (flag)
        {
            k = j - 1;
        }

        if (itemstack.isStackable())
        {
            while (itemstack.stackSize > 0 && (!flag && k < j || flag && k >= i))
            {
                Slot slot = (Slot) inventorySlots.get(k);
                ItemStack itemstack1 = slot.getStack();

                if (itemstack1 != null && slot.isItemValid(itemstack) && (flag || itemstack1.stackSize < slot.getSlotStackLimit())
                        && itemstack1.itemID == itemstack.itemID
                        && (!itemstack.getHasSubtypes() || itemstack.getItemDamage() == itemstack1.getItemDamage()))
                {
                    int i1 = itemstack1.stackSize + itemstack.stackSize;

                    if (i1 <= getLimit(slot, itemstack.getMaxStackSize(), flag))
                    {
                        itemstack.stackSize = 0;
                        itemstack1.stackSize = i1;
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                    else if (itemstack1.stackSize < getLimit(slot, itemstack.getMaxStackSize(), flag))
                    {
                        itemstack.stackSize -= getLimit(slot, itemstack.getMaxStackSize(), flag) - itemstack1.stackSize;
                        itemstack1.stackSize = getLimit(slot, itemstack.getMaxStackSize(), flag);
                        slot.onSlotChanged();
                        flag1 = true;
                    }
                }

                if (flag)
                {
                    k--;
                }
                else
                {
                    k++;
                }
            }
        }

        if (itemstack.stackSize > 0)
        {
            int l;

            if (flag)
            {
                l = j - 1;
            }
            else
            {
                l = i;
            }

            do
            {
                if ((flag || l >= j) && (!flag || l < i))
                {
                    break;
                }

                Slot slot = (Slot) inventorySlots.get(l);
                ItemStack itemstack2 = slot.getStack();

                if (itemstack2 == null && slot.isItemValid(itemstack))
                {
                    ItemStack toStore = itemstack.copy();
                    toStore.stackSize = getLimit(slot, toStore.stackSize, flag);
                    if(toStore.stackSize>toStore.getMaxStackSize())
                    	toStore.stackSize=toStore.getMaxStackSize();
                    itemstack.stackSize -= toStore.stackSize;
                    slot.putStack(toStore);
                    slot.onSlotChanged();

                    if (itemstack.stackSize <= 0)
                    {
                        flag1 = true;
                        itemstack.stackSize = 0;
                        break;
                    }
                }

                if (flag)
                {
                    l--;
                }
                else
                {
                    l++;
                }
            }
            while (true);
        }

        return flag1;
    }
    
    @Override
    public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
    {
        ItemStack var5 = null;
        InventoryPlayer var6 = par4EntityPlayer.inventory;
        Slot var7;
        ItemStack var8;
        int var10;
        ItemStack var11;

        if ((par3 == 0 || par3 == 1) && (par2 == 0 || par2 == 1))
        {
            if (par1 == -999)
            {
                if (var6.getItemStack() != null && par1 == -999)
                {
                    if (par2 == 0)
                    {
                        par4EntityPlayer.dropPlayerItem(var6.getItemStack());
                        var6.setItemStack((ItemStack)null);
                    }

                    if (par2 == 1)
                    {
                        par4EntityPlayer.dropPlayerItem(var6.getItemStack().splitStack(1));

                        if (var6.getItemStack().stackSize == 0)
                        {
                            var6.setItemStack((ItemStack)null);
                        }
                    }
                }
            }
            else if (par3 == 1)
            {
                var7 = (Slot)this.inventorySlots.get(par1);
                
                if (var7 != null && var7.canTakeStack(par4EntityPlayer))
                {
                    var8 = this.transferStackInSlot(par4EntityPlayer, par1);

                    if (var8 != null)
                    {
                        int var12 = var8.itemID;
                        var5 = var8.copy();

                        if (var7 != null && var7.getStack() != null && var7.getStack().itemID == var12)
                        {
                            this.retrySlotClick(par1, par2, true, par4EntityPlayer);
                        }
                    }
                }
            }
            else
            {
                if (par1 < 0)
                {
                    return null;
                }

                var7 = (Slot)this.inventorySlots.get(par1);

                if(var7 instanceof PC_Slot){
                	if(((PC_Slot) var7).isHandlingSlotClick()){
                		return ((PC_Slot) var7).slotClick(par2, par3, par4EntityPlayer);
                	}
                }
                
                if (var7 != null)
                {
                    var8 = var7.getStack();
                    ItemStack var13 = var6.getItemStack();

                    if (var8 != null)
                    {
                        var5 = var8.copy();
                    }

                    if (var8 == null)
                    {
                        if (var13 != null && var7.isItemValid(var13))
                        {
                        	var10 = par2 == 0 ? var13.stackSize : 1;
                            if (var10 > var7.getSlotStackLimit())
                            {
                                var10 = var7.getSlotStackLimit();
                            }

                            var7.putStack(var13.splitStack(var10));

                            if (var13.stackSize == 0)
                            {
                                var6.setItemStack((ItemStack)null);
                            }
                        }
                    }
                    else if (var7.canTakeStack(par4EntityPlayer))
                    {
                        if (var13 == null)
                        {
                            var10 = par2 == 0 ? var8.stackSize : (var8.stackSize + 1) / 2;
                            var11 = var7.decrStackSize(var10);
                            var6.setItemStack(var11);

                            if (var8.stackSize == 0)
                            {
                                var7.putStack((ItemStack)null);
                            }

                            var7.onPickupFromSlot(par4EntityPlayer, var6.getItemStack());
                        }
                        else if (var7.isItemValid(var13))
                        {
                            if (var8.itemID == var13.itemID && var8.getItemDamage() == var13.getItemDamage() && ItemStack.areItemStackTagsEqual(var8, var13))
                            {
                                var10 = par2 == 0 ? var13.stackSize : 1;
                                if (var10 > var7.getSlotStackLimit() - var8.stackSize)
                                {
                                    var10 = var7.getSlotStackLimit() - var8.stackSize;
                                }

                                if (var10 > var13.getMaxStackSize() - var8.stackSize)
                                {
                                    var10 = var13.getMaxStackSize() - var8.stackSize;
                                }
                                
                                var13.splitStack(var10);

                                if (var13.stackSize == 0)
                                {
                                    var6.setItemStack((ItemStack)null);
                                }

                                var8.stackSize += var10;
                            }
                            else if (var13.stackSize <= var7.getSlotStackLimit())
                            {
                                var7.putStack(var13);
                                var6.setItemStack(var8);
                            }
                        }
                        else if (var8.itemID == var13.itemID && var13.getMaxStackSize() > 1 && (!var8.getHasSubtypes() || var8.getItemDamage() == var13.getItemDamage()) && ItemStack.areItemStackTagsEqual(var8, var13))
                        {
                            var10 = var8.stackSize;

                            if (var10 > 0 && var10 + var13.stackSize <= var13.getMaxStackSize())
                            {
                                var13.stackSize += var10;
                                var8 = var7.decrStackSize(var10);

                                if (var8.stackSize == 0)
                                {
                                    var7.putStack((ItemStack)null);
                                }

                                var7.onPickupFromSlot(par4EntityPlayer, var6.getItemStack());
                            }
                        }
                    }

                    var7.onSlotChanged();
                }
            }
        }
        else if (par3 == 2 && par2 >= 0 && par2 < 9)
        {
            var7 = (Slot)this.inventorySlots.get(par1);
            
            if (var7.canTakeStack(par4EntityPlayer))
            {
                var8 = var6.getStackInSlot(par2);
                boolean var9 = var8 == null || var7.inventory == var6 && var7.isItemValid(var8);
                var10 = -1;

                if (!var9)
                {
                    var10 = var6.getFirstEmptyStack();
                    var9 |= var10 > -1;
                }

                if (var7.getHasStack() && var9)
                {
                    var11 = var7.getStack();
                    var6.setInventorySlotContents(par2, var11);

                    if ((var7.inventory != var6 || !var7.isItemValid(var8)) && var8 != null)
                    {
                        if (var10 > -1)
                        {
                            var6.addItemStackToInventory(var8);
                            var7.decrStackSize(var11.stackSize);
                            var7.putStack((ItemStack)null);
                            var7.onPickupFromSlot(par4EntityPlayer, var11);
                        }
                    }
                    else
                    {
                        var7.decrStackSize(var11.stackSize);
                        var7.putStack(var8);
                        var7.onPickupFromSlot(par4EntityPlayer, var11);
                    }
                }
                else if (!var7.getHasStack() && var8 != null && var7.isItemValid(var8))
                {
                    var6.setInventorySlotContents(par2, (ItemStack)null);
                    var7.putStack(var8);
                }
            }
        }
        else if (par3 == 3 && par4EntityPlayer.capabilities.isCreativeMode && var6.getItemStack() == null && par1 >= 0)
        {
            var7 = (Slot)this.inventorySlots.get(par1);
            
            if (var7 != null && var7.getHasStack())
            {
                var8 = var7.getStack().copy();
                var8.stackSize = var8.getMaxStackSize();
                var6.setItemStack(var8);
            }
        }

        return var5;
    }
    
}
