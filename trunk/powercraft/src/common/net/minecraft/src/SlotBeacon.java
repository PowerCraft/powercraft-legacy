package net.minecraft.src;

class SlotBeacon extends Slot
{
    final ContainerBeacon beacon;

    public SlotBeacon(ContainerBeacon par1ContainerBeacon, IInventory par2IInventory, int par3, int par4, int par5)
    {
        super(par2IInventory, par3, par4, par5);
        this.beacon = par1ContainerBeacon;
    }

    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return par1ItemStack == null ? false : par1ItemStack.itemID == Item.emerald.shiftedIndex || par1ItemStack.itemID == Item.diamond.shiftedIndex || par1ItemStack.itemID == Item.ingotGold.shiftedIndex || par1ItemStack.itemID == Item.ingotIron.shiftedIndex;
    }

    public int getSlotStackLimit()
    {
        return 1;
    }
}
