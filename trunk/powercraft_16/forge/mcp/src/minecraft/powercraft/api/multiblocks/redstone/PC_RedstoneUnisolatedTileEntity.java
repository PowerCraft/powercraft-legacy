package powercraft.api.multiblocks.redstone;

import powercraft.api.multiblocks.PC_MultiblockTileEntity;

public class PC_RedstoneUnisolatedTileEntity extends PC_RedstoneTileEntity implements PC_IRedstoneCable {

	private PC_RedstoneGrid grid;
	
	@Override
	public PC_RedstoneGrid getGrid() {
		return grid;
	}

	@Override
	public void setGrid(PC_RedstoneGrid grid) {
		this.grid = grid;
	}

	@Override
	protected PC_IRedstoneCable getCableType(int cableID) {
		return this;
	}

}
