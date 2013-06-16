package powercraft.machines;

import net.minecraft.src.EntityPlayer;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.tileentity.PC_TileEntity;

public class PCma_ContainerRoaster extends PC_GresBaseWithInventory<PCma_TileEntityRoaster>
{
    public PCma_ContainerRoaster(EntityPlayer player, PC_TileEntity te, Object[] o)
    {
        super(player, (PCma_TileEntityRoaster)te, o);
    }
    
    @Override
	protected boolean canShiftTransfer() {
		return true;
	}
    
}
