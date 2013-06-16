package powercraft.core;

import net.minecraft.src.EntityPlayer;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.tileentity.PC_TileEntity;

public class PCco_ContainerCraftingTool extends PC_GresBaseWithInventory<PC_TileEntity> {

	public PCco_ContainerCraftingTool(EntityPlayer player, PC_TileEntity te,
			Object[] o) {
		super(player, te, o);
	}

}
