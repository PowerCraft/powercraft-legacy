package net.minecraft.src;

public class InventoryCraftResult implements IInventory
{
    private ItemStack[] stackResult = new ItemStack[1];

    public int getSizeInventory()
    {
        return 1;
    }

    public ItemStack getStackInSlot(int par1)
    {
        return this.stackResult[0];
    }

    public String getInvName()
    {
        return "Result";
    }

    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.stackResult[0] != null)
        {
            ItemStack var3 = this.stackResult[0];
            this.stackResult[0] = null;
            return var3;
        }
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.stackResult[0] != null)
        {
            ItemStack var2 = this.stackResult[0];
            this.stackResult[0] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.stackResult[0] = par2ItemStack;
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void onInventoryChanged() {}

    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    public void openChest() {}

    public void closeChest() {}
}
