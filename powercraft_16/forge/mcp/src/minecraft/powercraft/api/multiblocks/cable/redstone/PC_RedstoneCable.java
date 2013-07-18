package powercraft.api.multiblocks.cable.redstone;

public class PC_RedstoneCable implements PC_IRedstoneCable {

	private int cableType;
	private PC_RedstoneGrid grid;
	private PC_RedstoneIsolatedTileEntity tileEntity;
	
	public PC_RedstoneCable(int cableType) {
		this.cableType = cableType;
	}

	@Override
	public PC_RedstoneGrid getGrid() {
		return grid;
	}

	@Override
	public void setGrid(PC_RedstoneGrid grid) {
		this.grid = grid;
	}

	public PC_RedstoneIsolatedTileEntity getTileEntity() {
		return tileEntity;
	}

	public void setTileEntity(PC_RedstoneIsolatedTileEntity tileEntity) {
		this.tileEntity = tileEntity;
	}
	
}
