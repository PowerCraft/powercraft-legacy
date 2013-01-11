package codechicken.core.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;

public class InventoryRange
{
    public InventoryRange(IInventory inv, ForgeDirection side)
    {
        this.inv = inv;
        fslot = 0;
        lslot = inv.getSizeInventory();
        if(inv instanceof ISidedInventory)
        {
            ISidedInventory sidedInv = (ISidedInventory)inv;
            fslot = sidedInv.getStartInventorySide(side);
            lslot = fslot+sidedInv.getSizeInventorySide(side);
        }
    }
    
    public InventoryRange(IInventory inv, int fslot, int lslot)
    {
        this.inv = inv;
        this.fslot = fslot;
        this.lslot = lslot;
    }

    public IInventory inv;
    public int fslot;
    public int lslot;
}
