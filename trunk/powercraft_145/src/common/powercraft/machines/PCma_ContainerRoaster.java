package powercraft.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Slot;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_SlotSelective;
import powercraft.management.PC_Utils;

public class PCma_ContainerRoaster extends PC_GresBaseWithInventory
{
    protected PCma_TileEntityRoaster roaster;
    protected List<Slot> lSlot;

    public PCma_ContainerRoaster(EntityPlayer player, Object[] o)
    {
        super(player, o);
    }

    @Override
    protected void init(Object[] o)
    {
        roaster = PC_Utils.getTE(thePlayer.worldObj, (Integer)o[0], (Integer)o[1], (Integer)o[2]);
    }

    @Override
    protected List<Slot> getAllSlots(List<Slot> slots)
    {
        lSlot = new ArrayList<Slot>();

        for (int i = 0; i < 9; i++)
        {
            if (i < roaster.getSizeInventory())
            {
                lSlot.add(new PC_SlotSelective(roaster, i, 0, 0));
            }
        }

        slots.addAll(lSlot);
        return slots;
    }
}
