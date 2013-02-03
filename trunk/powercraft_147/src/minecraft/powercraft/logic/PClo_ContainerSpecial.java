package powercraft.logic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;

public class PClo_ContainerSpecial extends PC_GresBaseWithInventory<PClo_TileEntitySpecial>
{
    protected List<Slot> lSlot;

    public PClo_ContainerSpecial(EntityPlayer player, PC_TileEntity te, Object[] o)
    {
        super(player, (PClo_TileEntitySpecial)te, o);
    }

    @Override
    protected void init(Object[] o){}

    @Override
    protected List<Slot> getAllSlots(List<Slot> slots)
    {
        lSlot = new ArrayList<Slot>();

        for (int i = 0; i < tileEntity.getSizeInventory(); i++)
        {
            lSlot.add(new Slot(tileEntity, i, 0, 0));
        }

        slots.addAll(lSlot);
        return slots;
    }
}
