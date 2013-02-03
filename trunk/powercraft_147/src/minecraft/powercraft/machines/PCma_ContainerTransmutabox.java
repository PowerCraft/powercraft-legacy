package powercraft.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_Slot;
import powercraft.management.PC_SlotNoPickup;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;

public class PCma_ContainerTransmutabox extends PC_GresBaseWithInventory<PCma_TileEntityTransmutabox>
{
    protected List<Slot> lSlot;

    public PCma_ContainerTransmutabox(EntityPlayer player, PC_TileEntity te, Object[] o)
    {
        super(player, (PCma_TileEntityTransmutabox)te, o);
    }

    @Override
    protected void init(Object[] o){}

    @Override
    protected boolean canShiftTransfer()
    {
        return true;
    }
    
    @Override
    protected List<Slot> getAllSlots(List<Slot> slots)
    {
        lSlot = new ArrayList<Slot>();

        for (int i = 0; i < tileEntity.getSizeInventory(); i++)
        {
        	if(i==9||i==10){
        		lSlot.add(new PC_SlotNoPickup(tileEntity, i));
        	}else{
        		lSlot.add(new PC_Slot(tileEntity, i));
        	}
        }

        slots.addAll(lSlot);
        return slots;
    }
}
