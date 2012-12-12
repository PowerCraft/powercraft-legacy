package powercraft.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Slot;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_Utils;

public class PCma_ContainerReplacer extends PC_GresBaseWithInventory
{
    protected List<Slot> lSlot;
    protected PCma_TileEntityReplacer teReplacer;

    public PCma_ContainerReplacer(EntityPlayer player, Object[] o)
    {
        super(player, o);
    }

    @Override
    protected void init(Object[] o)
    {
        teReplacer = PC_Utils.getTE(thePlayer.worldObj, (Integer)o[0], (Integer)o[1], (Integer)o[2]);
    }

    @Override
    protected List<Slot> getAllSlots(List<Slot> slots)
    {
        lSlot = new ArrayList<Slot>();
        lSlot.add(new Slot(teReplacer, 0, 0, 0));
        slots.addAll(lSlot);
        return slots;
    }
}
