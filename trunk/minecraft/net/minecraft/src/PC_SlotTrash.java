package net.minecraft.src;

public class PC_SlotTrash extends Slot
{
    public PC_SlotTrash() {
		super(null,0,0,0);
	}

    public void func_48433_a(ItemStack par1ItemStack, ItemStack par2ItemStack){}

    protected void func_48435_a(ItemStack itemstack, int i){}

    protected void func_48434_c(ItemStack itemstack){}

    public void onPickupFromSlot(ItemStack par1ItemStack){}

    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return true;
    }

    public ItemStack getStack()
    {
        return null;
    }

    /**
     * Returns if this slot contains a stack.
     */
    public boolean getHasStack()
    {
        return false;
    }

    /**
     * Helper method to put a stack in the slot.
     */
    public void putStack(ItemStack par1ItemStack)
    {
    }

    /**
     * Called when the stack in a Slot changes
     */
    public void onSlotChanged()
    {
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as getInventoryStackLimit(), but 1 in the case
     * of armor slots)
     */
    public int getSlotStackLimit()
    {
        return 128;
    }

    /**
     * Returns the icon index on items.png that is used as background image of the slot.
     */
    public int getBackgroundIconIndex()
    {
        return -1;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int par1)
    {
        return null;
    }
}
