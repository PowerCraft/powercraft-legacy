package powercraft.logic;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.gres.PC_GresBaseWithInventory;
import powercraft.management.tileentity.PC_TileEntity;

public class PClo_ContainerSpecial extends PC_GresBaseWithInventory<PClo_TileEntitySpecial>
{
    public PClo_ContainerSpecial(EntityPlayer player, PC_TileEntity te, Object[] o)
    {
        super(player, (PClo_TileEntitySpecial)te, o);
    }
    
    @Override
	protected boolean canShiftTransfer() {
		return true;
	}
    
}
