package powercraft.machines;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class PCma_SlotAutomaticWorkbenchInventory extends Slot
{
    private Container parentContainer;
    private boolean inrecipe = false;

    public PCma_SlotAutomaticWorkbenchInventory(IInventory iinventory, Container parent, boolean inRecipe, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        this.parentContainer = parent;
        this.inrecipe = inRecipe;
    }

    @Override
    public void onSlotChanged()
    {
        inventory.onInventoryChanged();

        if (inrecipe)
        {
            parentContainer.onCraftMatrixChanged(super.inventory);
        }
    }
}
