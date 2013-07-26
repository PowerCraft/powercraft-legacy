package powercraft.api.multiblocks.conduits;

import powercraft.api.multiblocks.PC_MultiblockType;
import powercraft.apiOld.multiblocks.PC_MultiblockItem;

public abstract class PC_ConduitItem extends PC_MultiblockItem {

	public PC_ConduitItem(int id) {
		super(id);
	}

	@Override
	public PC_MultiblockType getMultiblockType() {
		return PC_MultiblockType.CENTER;
	}
	
}
