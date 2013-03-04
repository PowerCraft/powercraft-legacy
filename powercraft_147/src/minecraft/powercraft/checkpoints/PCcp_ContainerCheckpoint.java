package powercraft.checkpoints;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.gres.PC_GresBaseWithInventory;
import powercraft.management.tileentity.PC_TileEntity;

public class PCcp_ContainerCheckpoint extends PC_GresBaseWithInventory<PCcp_TileEntityCheckpoint> {
	
	public PCcp_ContainerCheckpoint(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, (PCcp_TileEntityCheckpoint)te, o);
	}

	@Override
	protected boolean canShiftTransfer() {
		return true;
	}
	
}
