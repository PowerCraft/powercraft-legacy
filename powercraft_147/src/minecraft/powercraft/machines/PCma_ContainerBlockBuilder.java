package powercraft.machines;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.gres.PC_GresBaseWithInventory;
import powercraft.management.inventory.PC_Slot;

public class PCma_ContainerBlockBuilder extends PC_GresBaseWithInventory<PCma_TileEntityBlockBuilder> {
	
	public PCma_ContainerBlockBuilder(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, (PCma_TileEntityBlockBuilder)te, o);
	}

	@Override
	protected boolean canShiftTransfer() {
		return true;
	}
	
}
