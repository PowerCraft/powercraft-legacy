package powercraft.api.multiblocks.redstone;

import powercraft.api.multiblocks.PC_MultiblockTileEntity;

public abstract class PC_RedstoneTileEntity extends PC_MultiblockTileEntity {
	
	public PC_RedstoneTileEntity() {
		
	}
	
	protected abstract PC_IRedstoneCable getCableType(int cableID);
	
}
