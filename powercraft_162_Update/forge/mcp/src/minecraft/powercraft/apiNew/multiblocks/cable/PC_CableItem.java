package powercraft.api.multiblocks.cable;


import powercraft.api.multiblocks.PC_MultiblockType;
import powercraft.apiOld.multiblocks.PC_MultiblockItem;


public abstract class PC_CableItem extends PC_MultiblockItem {

	public PC_CableItem(int id) {

		super(id);
	}


	@Override
	public PC_MultiblockType getMultiblockType() {

		return PC_MultiblockType.FACE;
	}

}
