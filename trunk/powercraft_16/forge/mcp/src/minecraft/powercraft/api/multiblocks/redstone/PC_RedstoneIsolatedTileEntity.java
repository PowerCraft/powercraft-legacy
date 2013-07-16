package powercraft.api.multiblocks.redstone;

import powercraft.api.multiblocks.PC_MultiblockTileEntity;

public class PC_RedstoneIsolatedTileEntity extends PC_RedstoneTileEntity {

	private PC_RedstoneCable cable[] = new PC_RedstoneCable[16];
	
	public PC_RedstoneIsolatedTileEntity(int cableType) {
		cable[cableType] = new PC_RedstoneCable(1<<cableType);
	}

	@Override
	protected PC_IRedstoneCable getCableType(int cableID){
		return cable[cableID];
	}
	
}
