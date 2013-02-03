package powercraft.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_Slot;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;

public class PCma_ContainerRoaster extends PC_GresBaseWithInventory<PCma_TileEntityRoaster>
{
    protected List<Slot> lSlot;

    public PCma_ContainerRoaster(EntityPlayer player, PC_TileEntity te, Object[] o)
    {
        super(player, (PCma_TileEntityRoaster)te, o);
    }

    @Override
    protected void init(Object[] o){}

    @Override
    protected List<Slot> getAllSlots(List<Slot> slots)
    {
        lSlot = new ArrayList<Slot>();

        for (int i = 0; i < 9; i++)
        {
            if (i < tileEntity.getSizeInventory())
            {
                lSlot.add(new PC_Slot(tileEntity, i));
            }
        }

        slots.addAll(lSlot);
        return slots;
    }
}
