package net.minecraft.src;

class InventoryRepair extends InventoryBasic
{
    final ContainerRepair field_82346_a;

    InventoryRepair(ContainerRepair par1ContainerRepair, String par2Str, int par3)
    {
        super(par2Str, par3);
        this.field_82346_a = par1ContainerRepair;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
        super.onInventoryChanged();
        this.field_82346_a.onCraftMatrixChanged(this);
    }
}
