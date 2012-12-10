package powercraft.logic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Slot;
import powercraft.core.PC_GresBaseWithInventory;
import powercraft.core.PC_Utils;

public class PClo_ContainerSpecial extends PC_GresBaseWithInventory
{
    protected PClo_TileEntitySpecial te;
    protected List<Slot> lSlot;

    public PClo_ContainerSpecial(EntityPlayer player, Object[] o)
    {
        super(player, o);
    }

    @Override
    protected void init(Object[] o)
    {
        te = PC_Utils.getTE(thePlayer.worldObj, (Integer)o[0], (Integer)o[1], (Integer)o[2]);
    }

    @Override
    protected List<Slot> getAllSlots(List<Slot> slots)
    {
        lSlot = new ArrayList<Slot>();

        for (int i = 0; i < te.getSizeInventory(); i++)
        {
            lSlot.add(new Slot(te, i, 0, 0));
        }

        slots.addAll(lSlot);
        return slots;
    }
}
