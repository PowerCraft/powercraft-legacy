package powercraft.weasel;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.PC_TileEntity;
import powercraft.management.gres.PC_GresBaseWithInventory;

public class PCws_ContainerWeaselDiskDrive extends PC_GresBaseWithInventory<PCws_TileEntityWeasel> {
	
	public PCws_ContainerWeaselDiskDrive(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, (PCws_TileEntityWeasel)te, o);
	}

	@Override
	protected boolean canShiftTransfer() {
		return true;
	}
	
}
