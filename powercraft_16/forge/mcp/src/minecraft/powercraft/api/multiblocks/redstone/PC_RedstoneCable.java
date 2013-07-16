package powercraft.api.multiblocks.redstone;

public class PC_RedstoneCable implements PC_IRedstoneCable {

	private int cableType;
	private PC_RedstoneGrid grid;
	
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

}
