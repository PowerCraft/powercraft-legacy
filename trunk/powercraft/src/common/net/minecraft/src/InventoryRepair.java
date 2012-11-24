package net.minecraft.src;

class InventoryRepair extends InventoryBasic
{
    final ContainerRepair theContainer;

    InventoryRepair(ContainerRepair par1ContainerRepair, String par2Str, int par3)
    {
        super(par2Str, par3);
        this.theContainer = par1ContainerRepair;
    }

    public void onInventoryChanged()
    {
        super.onInventoryChanged();
        this.theContainer.onCraftMatrixChanged(this);
    }
}
