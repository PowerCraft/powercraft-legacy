package powercraft.api.multiblock.cable.redstone;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Utils;
import powercraft.api.PC_Vec3IWithRotation;
import powercraft.api.multiblock.PC_MultiblockIndex;


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


	public int getCableType() {

		return cableType;
	}


	@Override
	public void addPoweringBlocks(List<PC_Vec3IWithRotation> poweringBlocks) {

		if (tileEntity.getCableCount() == 1) {
			PC_Direction dir = PC_MultiblockIndex.getFaceDir(tileEntity.getIndex());
			int i = 0;
			for (PC_Direction dir2 : PC_Direction.VALID_DIRECTIONS) {
				if (dir2 == dir || dir2 == dir.getOpposite()) {
					continue;
				}
				int connection[] = tileEntity.getConnections(i);
				if (connection != null) {
					if (connection.length > 1 && (connection[1] & 0xFFFF) != 0) {
						if (tileEntity.isIO(dir.offsetX, dir.offsetY, dir.offsetZ, dir2)) {
							poweringBlocks.add(new PC_Vec3IWithRotation(tileEntity.getTileEntity().xCoord + dir.offsetX,
									tileEntity.getTileEntity().yCoord + dir.offsetY, tileEntity.getTileEntity().zCoord + dir.offsetZ, dir2));
						}
					}
					if (connection.length > 2 && (connection[2] & 0xFFFF) != 0) {
						if (tileEntity.isIO(dir2.offsetX, dir2.offsetY, dir2.offsetZ, dir)) {
							poweringBlocks.add(new PC_Vec3IWithRotation(tileEntity.getTileEntity().xCoord + dir2.offsetX,
									tileEntity.getTileEntity().yCoord + dir2.offsetY, tileEntity.getTileEntity().zCoord + dir2.offsetZ, dir));
						}
					}
					if (connection.length > 3 && (connection[3] & 0xFFFF) != 0) {
						if (tileEntity.isIO(dir.offsetX + dir2.offsetX, dir.offsetY + dir2.offsetY, dir.offsetZ + dir2.offsetZ, dir2.getOpposite())) {
							poweringBlocks.add(new PC_Vec3IWithRotation(tileEntity.getTileEntity().xCoord + dir.offsetX + dir2.offsetX, tileEntity
									.getTileEntity().yCoord + dir.offsetY + dir2.offsetY, tileEntity.getTileEntity().zCoord + dir.offsetZ
									+ dir2.offsetZ, dir2.getOpposite()));
						}
					}
				}
				i++;
			}
		}
	}


	@Override
	public World getWorld() {

		return tileEntity.getWorld();
	}


	@Override
	public void onRedstonePowerChange() {

		if (tileEntity.isIO()) {
			PC_Utils.hugeUpdate(tileEntity.getWorld(), tileEntity.getTileEntity().xCoord, tileEntity.getTileEntity().yCoord,
					tileEntity.getTileEntity().zCoord);
		}
	}


	public void addToGrid() {

		getGridIfNull();
		if (tileEntity.isIO()) {
			grid.addIO(this);
		} else {
			grid.add(this);
		}
	}


	public void getGridIfNull() {

		if (!tileEntity.isClient() && grid == null) {
			PC_Direction dir = PC_MultiblockIndex.getFaceDir(tileEntity.getIndex());
			int i = 0;
			for (PC_Direction dir2 : PC_Direction.VALID_DIRECTIONS) {
				if (dir2 == dir || dir2 == dir.getOpposite()) {
					continue;
				}
				int connection[] = tileEntity.getConnections(i);
				if (connection != null) {
					if ((connection[0] & cableType) != 0) {
						useGrid(tileEntity.getGrid(0, 0, 0, dir2, cableType));
					}
					if (connection.length > 1 && (connection[1] & cableType) != 0) {
						useGrid(tileEntity.getGrid(dir.offsetX, dir.offsetY, dir.offsetZ, dir2, cableType));
					}
					if (connection.length > 2 && (connection[2] & cableType) != 0) {
						useGrid(tileEntity.getGrid(dir2.offsetX, dir2.offsetY, dir2.offsetZ, dir, cableType));
						useGrid(tileEntity.getGrid(dir2.offsetX, dir2.offsetY, dir2.offsetZ, dir2.getOpposite(), cableType));
					}
					if (connection.length > 3 && (connection[3] & cableType) != 0) {
						useGrid(tileEntity.getGrid(dir.offsetX + dir2.offsetX, dir.offsetY + dir2.offsetY, dir.offsetZ + dir2.offsetZ,
								dir2.getOpposite(), cableType));
					}
					if (connection.length > 4 && (connection[4] & cableType) != 0) {
						useGrid(tileEntity.getGrid(dir.offsetX + dir2.offsetX, dir.offsetY + dir2.offsetY, dir.offsetZ + dir2.offsetZ,
								dir.getOpposite(), cableType));
					}
				}
				i++;
			}
			if (grid == null) {
				grid = new PC_RedstoneGrid();
			}
			addToGrid();
		}
	}


	private void useGrid(PC_RedstoneGrid g) {

		if (g != null) {
			if (grid == null) {
				grid = g;
			} else {
				grid.mixGrids(g);
			}
		}
	}


	public void removeFormGrid() {

		if (!tileEntity.isClient() && grid != null) {
			PC_RedstoneGrid oldGrid = grid;
			grid.remove(this);
			grid = null;
			boolean isFirst = true;
			List<PC_RedstoneGrid> doneGrids = new ArrayList<PC_RedstoneGrid>();
			PC_Direction dir = PC_MultiblockIndex.getFaceDir(tileEntity.getIndex());
			int i = 0;
			for (PC_Direction dir2 : PC_Direction.VALID_DIRECTIONS) {
				if (dir2 == dir || dir2 == dir.getOpposite()) {
					continue;
				}
				int connection[] = tileEntity.getConnections(i);
				if (connection != null) {
					if ((connection[0] & cableType) != 0) {
						PC_RedstoneGrid g = tileEntity.getGrid(0, 0, 0, dir2, cableType);
						if (g != null && !doneGrids.contains(g)) {
							if (isFirst) {
								isFirst = false;
							} else {
								PC_RedstoneGrid ng = new PC_RedstoneGrid();
								doneGrids.add(ng);
								tileEntity.setBlockAndNeighborGrid(dir.offsetX, dir.offsetY, dir.offsetZ, dir2, cableType, ng);
							}
						}
					}
					if (connection.length > 1 && (connection[1] & cableType) != 0) {
						PC_RedstoneGrid g = tileEntity.getGrid(dir.offsetX, dir.offsetY, dir.offsetZ, dir2, cableType);
						if (g != null && !doneGrids.contains(g)) {
							if (isFirst) {
								isFirst = false;
							} else {
								PC_RedstoneGrid ng = new PC_RedstoneGrid();
								doneGrids.add(ng);
								tileEntity.setBlockAndNeighborGrid(dir.offsetX, dir.offsetY, dir.offsetZ, dir2, cableType, ng);
							}
						}
					}
					if (connection.length > 2 && (connection[2] & cableType) != 0) {
						PC_RedstoneGrid g = tileEntity.getGrid(dir2.offsetX, dir2.offsetY, dir2.offsetZ, dir, cableType);
						if (g != null && !doneGrids.contains(g)) {
							if (isFirst) {
								isFirst = false;
							} else {
								PC_RedstoneGrid ng = new PC_RedstoneGrid();
								doneGrids.add(ng);
								tileEntity.setBlockAndNeighborGrid(dir2.offsetX, dir2.offsetY, dir2.offsetZ, dir, cableType, ng);
							}
						}
						g = tileEntity.getGrid(dir2.offsetX, dir2.offsetY, dir2.offsetZ, dir2.getOpposite(), cableType);
						if (g != null && !doneGrids.contains(g)) {
							if (isFirst) {
								isFirst = false;
							} else {
								PC_RedstoneGrid ng = new PC_RedstoneGrid();
								doneGrids.add(ng);
								tileEntity.setBlockAndNeighborGrid(dir2.offsetX, dir2.offsetY, dir2.offsetZ, dir2.getOpposite(), cableType, ng);
							}
						}
					}
					if (connection.length > 3 && (connection[3] & cableType) != 0) {
						PC_RedstoneGrid g = tileEntity.getGrid(dir.offsetX + dir2.offsetX, dir.offsetY + dir2.offsetY, dir.offsetZ + dir2.offsetZ,
								dir2.getOpposite(), cableType);
						if (g != null && !doneGrids.contains(g)) {
							if (isFirst) {
								isFirst = false;
							} else {
								PC_RedstoneGrid ng = new PC_RedstoneGrid();
								doneGrids.add(ng);
								tileEntity.setBlockAndNeighborGrid(dir.offsetX + dir2.offsetX, dir.offsetY + dir2.offsetY,
										dir.offsetZ + dir2.offsetZ, dir2.getOpposite(), cableType, ng);
							}
						}
					}
					if (connection.length > 4 && (connection[4] & cableType) != 0) {
						PC_RedstoneGrid g = tileEntity.getGrid(dir.offsetX + dir2.offsetX, dir.offsetY + dir2.offsetY, dir.offsetZ + dir2.offsetZ,
								dir.getOpposite(), cableType);
						if (g != null && !doneGrids.contains(g)) {
							if (isFirst) {
								isFirst = false;
							} else {
								PC_RedstoneGrid ng = new PC_RedstoneGrid();
								doneGrids.add(ng);
								tileEntity.setBlockAndNeighborGrid(dir.offsetX + dir2.offsetX, dir.offsetY + dir2.offsetY,
										dir.offsetZ + dir2.offsetZ, dir.getOpposite(), cableType, ng);
							}
						}
					}
				}
				i++;
			}
			oldGrid.onUpdateTick(this);
		}
	}

}
