package codechicken.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryCopy implements IInventory
{
    public InventoryCopy(IInventory inv)
    {
        items = new ItemStack[inv.getSizeInventory()];
        accessible = new boolean[inv.getSizeInventory()];
        
        for(int i = 0; i < items.length; i++)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if(stack != null)
                items[i] = stack.copy();
        }
    }
    
    public InventoryCopy open(InventoryRange access)
    {
        for(int slot = access.fslot; slot < access.lslot; slot++)
            accessible[slot] = true;
        return this;
    }
    
    public boolean[] accessible;
    public ItemStack[] items;
    
    @Override
    public int getSizeInventory()
    {
        return items.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return items[slot];
    }

    public ItemStack decrStackSize(int slot, int amount)
    {
        return InventoryUtils.decrStackSize(this, slot, amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        return InventoryUtils.getStackInSlotOnClosing(this, slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        items[slot] = stack;
        onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return "copy";
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return true;
    }

    public void openChest()
    {
    }

    @Override
    public void closeChest()
    {
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }
    

    @Override
    public void onInventoryChanged()
    {
    }
}
