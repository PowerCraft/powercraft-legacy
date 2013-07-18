package powercraft.api.multiblocks.cable;

import net.minecraft.item.ItemStack;
import powercraft.api.multiblocks.PC_MultiblockItem;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;
import powercraft.api.multiblocks.PC_MultiblockType;

public abstract class PC_CableItem extends PC_MultiblockItem {

	public PC_CableItem(int id) {
		super(id);
	}

	@Override
	public PC_MultiblockType getMultiblockType() {
		return PC_MultiblockType.FACE;
	}

}
