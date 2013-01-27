package powercraft.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_Slot;
import powercraft.management.PC_SlotNoPickup;
import powercraft.management.PC_Utils.GameInfo;

public class PCma_ContainerTransmutabox extends PC_GresBaseWithInventory
{
    protected PCma_TileEntityTransmutabox te;
    protected List<Slot> lSlot;

    public PCma_ContainerTransmutabox(EntityPlayer player, Object[] o)
    {
        super(player, o);
    }

    @Override
    protected void init(Object[] o)
    {
        te = GameInfo.getTE(thePlayer.worldObj, (Integer)o[0], (Integer)o[1], (Integer)o[2]);
    }

    @Override
    protected boolean canShiftTransfer()
    {
        return true;
    }
    
    @Override
    protected List<Slot> getAllSlots(List<Slot> slots)
    {
        lSlot = new ArrayList<Slot>();

        for (int i = 0; i < te.getSizeInventory(); i++)
        {
        	if(i==9||i==10){
        		lSlot.add(new PC_SlotNoPickup(te, i));
        	}else{
        		lSlot.add(new PC_Slot(te, i));
        	}
        }

        slots.addAll(lSlot);
        return slots;
    }
}
