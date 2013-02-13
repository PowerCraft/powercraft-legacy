package powercraft.transport;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_TileEntity;
import powercraft.management.gres.PC_GresBaseWithInventory;

public class PCtr_ContainerSeparationBelt extends PC_GresBaseWithInventory<PCtr_TileEntitySeparationBelt>
{

    public PCtr_ContainerSeparationBelt(EntityPlayer player, PC_TileEntity te, Object[]o)
    {
        super(player, (PCtr_TileEntitySeparationBelt)te, o);
    }
    
    @Override
	protected boolean canShiftTransfer() {
		return true;
	}
    
}
