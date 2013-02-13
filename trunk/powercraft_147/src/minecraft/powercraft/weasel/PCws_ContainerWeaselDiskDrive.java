package powercraft.weasel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.gres.PC_GresBaseWithInventory;
import powercraft.management.inventory.PC_Slot;

public class PCws_ContainerWeaselDiskDrive extends PC_GresBaseWithInventory<PCws_TileEntityWeasel> {
	
	public PCws_ContainerWeaselDiskDrive(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, (PCws_TileEntityWeasel)te, o);
	}

	@Override
	protected boolean canShiftTransfer() {
		return true;
	}
	
}
