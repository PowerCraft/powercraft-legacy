package powercraft.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.gres.PC_GresBaseWithInventory;
import powercraft.management.inventory.PC_Slot;
import powercraft.management.inventory.PC_SlotNoPickup;

public class PCma_ContainerTransmutabox extends PC_GresBaseWithInventory<PCma_TileEntityTransmutabox>
{
    public PCma_ContainerTransmutabox(EntityPlayer player, PC_TileEntity te, Object[] o)
    {
        super(player, (PCma_TileEntityTransmutabox)te, o);
    }

    @Override
    protected boolean canShiftTransfer()
    {
        return true;
    }
    
    @Override
    protected PC_Slot[] getAllSlots()
    {

        invSlots = new PC_Slot[tileEntity.getSizeInventory()];
        
        for (int i = 0; i < invSlots.length; i++)
        {
        	if(i==9||i==10){
        		invSlots[i] = new PC_SlotNoPickup(tileEntity, i);
        	}else{
        		invSlots[i] = new PC_Slot(tileEntity, i);
        	}
        }

        return invSlots;
    }
}
