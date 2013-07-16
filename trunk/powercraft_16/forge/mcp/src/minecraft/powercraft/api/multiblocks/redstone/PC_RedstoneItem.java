package powercraft.api.multiblocks.redstone;

import net.minecraft.item.ItemStack;
import powercraft.api.multiblocks.PC_MultiblockItem;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;
import powercraft.api.multiblocks.PC_MultiblockType;

public abstract class PC_RedstoneItem extends PC_MultiblockItem {

	public PC_RedstoneItem(int id) {
		super(id);
	}

	@Override
	public PC_MultiblockType getMultiblockType() {
		return PC_MultiblockType.FACE;
	}

}
