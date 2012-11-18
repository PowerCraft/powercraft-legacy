package powercraft.transport;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Slot;
import powercraft.core.PC_GresBaseWithInventory;
import powercraft.core.PC_Utils;

public class PCtr_ContainerSeparationBelt extends PC_GresBaseWithInventory
{
    protected PCtr_TileEntitySeparationBelt tes;
    protected List<Slot> lSlot;

    public PCtr_ContainerSeparationBelt(EntityPlayer player, Object[]o)
    {
        super(player, o);
    }

    @Override
    protected void init(Object[] o)
    {
        tes = (PCtr_TileEntitySeparationBelt)PC_Utils.getTE(thePlayer.worldObj, (Integer)o[0], (Integer)o[1], (Integer)o[2]);
    }

    @Override
    protected List<Slot> getAllSlots(List<Slot> slots)
    {
        lSlot = new ArrayList<Slot>();

        for (int i = 0; i < tes.getSizeInventory(); i++)
        {
            lSlot.add(new Slot(tes, i, 0, 0));
        }

        slots.addAll(lSlot);
        return slots;
    }
}
