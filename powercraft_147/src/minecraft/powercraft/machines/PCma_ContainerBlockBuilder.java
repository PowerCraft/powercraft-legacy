package powercraft.machines;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.management.gres.PC_GresBaseWithInventory;
import powercraft.management.tileentity.PC_TileEntity;

public class PCma_ContainerBlockBuilder extends PC_GresBaseWithInventory<PCma_TileEntityBlockBuilder> {
	
	public PCma_ContainerBlockBuilder(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, (PCma_TileEntityBlockBuilder)te, o);
	}

	@Override
	protected boolean canShiftTransfer() {
		return true;
	}
	
}
