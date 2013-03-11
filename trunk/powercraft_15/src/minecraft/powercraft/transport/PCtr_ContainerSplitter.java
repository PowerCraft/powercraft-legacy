package powercraft.transport;

import net.minecraft.entity.player.EntityPlayer;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.tileentity.PC_TileEntity;

public class PCtr_ContainerSplitter extends PC_GresBaseWithInventory<PCtr_TileEntitySplitter> {

	public PCtr_ContainerSplitter(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, (PCtr_TileEntitySplitter)te, o);
	}

}
