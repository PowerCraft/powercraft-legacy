package powercraft.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;

public class PCma_ContainerAutomaticWorkbench extends PC_GresBaseWithInventory<PCma_TileEntityAutomaticWorkbench>
{
    private IInventory craftResult;
    protected List<Slot> lSlot;

    public PCma_ContainerAutomaticWorkbench(EntityPlayer player, PC_TileEntity te, Object[] o)
    {
        super(player, (PCma_TileEntityAutomaticWorkbench)te, o);
    }

    @Override
    protected void init(Object[] o)
    {
        craftResult = new InventoryCraftResult();
    }

    @Override
    public void onCraftMatrixChanged(IInventory iinventory)
    {
        craftResult.setInventorySlotContents(0, tileEntity.getRecipeProduct());
    }

    @Override
    public List<Slot> getAllSlots(List<Slot> list)
    {
        lSlot = new ArrayList<Slot>();
        int cnt = 0;
        lSlot.add(new PCma_SlotAutomaticWorkbenchResult(tileEntity, craftResult, this, 0, 0, 0));

        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                lSlot.add(new PCma_SlotAutomaticWorkbenchInventory(tileEntity, this, false, cnt++, 0, 0));
            }
        }

        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                lSlot.add(new PCma_SlotAutomaticWorkbenchInventory(tileEntity, this, true, cnt++, 0, 0));
            }
        }

        list.addAll(lSlot);
        return list;
    }
}
