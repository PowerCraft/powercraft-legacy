package net.minecraft.src;

public interface IInventory
{
    int getSizeInventory();

    ItemStack getStackInSlot(int var1);

    ItemStack decrStackSize(int var1, int var2);

    ItemStack getStackInSlotOnClosing(int var1);

    void setInventorySlotContents(int var1, ItemStack var2);

    String getInvName();

    int getInventoryStackLimit();

    void onInventoryChanged();

    boolean isUseableByPlayer(EntityPlayer var1);

    void openChest();

    void closeChest();
}
