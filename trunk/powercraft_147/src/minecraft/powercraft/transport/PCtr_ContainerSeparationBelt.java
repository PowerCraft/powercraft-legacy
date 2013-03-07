package powercraft.transport;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.tileentity.PC_TileEntity;

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
