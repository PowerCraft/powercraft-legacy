package net.minecraft.src;

class SlotBrewingStandIngredient extends Slot
{
    final ContainerBrewingStand brewingStand;

    public SlotBrewingStandIngredient(ContainerBrewingStand par1ContainerBrewingStand, IInventory par2IInventory, int par3, int par4, int par5)
    {
        super(par2IInventory, par3, par4, par5);
        this.brewingStand = par1ContainerBrewingStand;
    }

    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return par1ItemStack != null ? Item.itemsList[par1ItemStack.itemID].isPotionIngredient() : false;
    }

    public int getSlotStackLimit()
    {
        return 64;
    }
}
