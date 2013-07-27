package powercraft.api.multiblock.conduits;

import powercraft.api.multiblock.PC_MultiblockItem;
import powercraft.api.multiblock.PC_MultiblockType;

public abstract class PC_ConduitItem extends PC_MultiblockItem {

	public PC_ConduitItem(int id) {
		super(id);
	}

	@Override
	public PC_MultiblockType getMultiblockType() {
		return PC_MultiblockType.CENTER;
	}
	
}
