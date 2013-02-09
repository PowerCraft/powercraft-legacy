package powercraft.machines;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import powercraft.management.PC_TileEntity;
import powercraft.management.gres.PC_GresBaseWithInventory;
import powercraft.management.inventory.PC_Slot;

public class PCma_ContainerCraftingFurnace extends PC_GresBaseWithInventory<PCma_TileEntityCraftingFurnace> {

	private IInventory craftResult;
	
	public PCma_ContainerCraftingFurnace(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, (PCma_TileEntityCraftingFurnace)te, o);
	}

    @Override
    protected void init(Object[] o)
    {
        craftResult = new InventoryCraftResult();
    }

    /*@Override
    public void onCraftMatrixChanged(IInventory iinventory)
    {
        craftResult.setInventorySlotContents(0, tileEntity.getRecipeProduct());
    }

    @Override
    public PC_Slot[] getAllSlots()
    {
        int cnt = 0;
        invSlots = new PC_Slot[19];
        
        invSlots[0] = new PCma_SlotAutomaticWorkbenchResult(tileEntity, craftResult, this, 0);
        
        
        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
            	invSlots[cnt+1] = new PCma_SlotAutomaticWorkbenchInventory(tileEntity, this, false, cnt);
            	cnt++;
            }
        }

        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
            	invSlots[cnt+1] = new PCma_SlotAutomaticWorkbenchInventory(tileEntity, this, true, cnt);
            	cnt++;
            }
        }

        return invSlots;
    }*/
	
}
