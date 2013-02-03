package powercraft.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;

public class PCma_ContainerReplacer extends PC_GresBaseWithInventory<PCma_TileEntityReplacer>
{
    protected List<Slot> lSlot;

    public PCma_ContainerReplacer(EntityPlayer player, PC_TileEntity te, Object[] o)
    {
        super(player, (PCma_TileEntityReplacer)te, o);
    }

    @Override
    protected void init(Object[] o){}

    @Override
    protected List<Slot> getAllSlots(List<Slot> slots)
    {
        lSlot = new ArrayList<Slot>();
        lSlot.add(new Slot(tileEntity, 0, 0, 0));
        slots.addAll(lSlot);
        return slots;
    }
}
