package powercraft.api.multiblocks.cable.redstone;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Utils;
import powercraft.api.PC_Vec3IWithRotation;
import powercraft.api.multiblocks.PC_MultiblockIndex;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;
import powercraft.api.multiblocks.PC_TileEntityMultiblock;
import powercraft.api.multiblocks.cable.PC_CableTileEntity;


public class PC_RedstoneUnisolatedTileEntity extends PC_CableTileEntity implements PC_IRedstoneCable {

	private boolean removed;
	
	public PC_RedstoneUnisolatedTileEntity(NBTTagCompound nbtTagCompound) {
		super(nbtTagCompound);
	}
	
	public PC_RedstoneUnisolatedTileEntity() {

		super(1, 2);
	}

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


	@Override
	protected Icon getCableIcon() {

		return PC_RedstoneUnisolatedItem.getCableIcon();
	}


	@Override
	protected Icon getCableLineIcon(int index) {

		return null;
	}


	@Override
	public List<ItemStack> getDrop() {

		List<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(PC_RedstoneUnisolatedItem.item, 1, 0));
		return drops;
	}


	@Override
	protected int canConnectToMultiblock(PC_MultiblockTileEntity multiblock) {

		if (multiblock instanceof PC_RedstoneUnisolatedTileEntity) {
			PC_RedstoneUnisolatedTileEntity unisolated = (PC_RedstoneUnisolatedTileEntity) multiblock;
			int connection = 0xFFFF;
			int length = 16;
			if (unisolated.getCenterThickness() > 0) length = unisolated.getCenterThickness() + 2 + unisolated.getThickness() * 2;
			return connection | (length << 16);
		} else if (multiblock instanceof PC_RedstoneIsolatedTileEntity) {
			PC_RedstoneIsolatedTileEntity isolated = (PC_RedstoneIsolatedTileEntity) multiblock;
			if (isolated.getCableCount() == 1) {
				int connection = isolated.getMask();
				int length = 16;
				if (isolated.getCenterThickness() > 0) length = isolated.getCenterThickness() + 2 + isolated.getThickness() * 2;
				return connection | (length << 16);
			}
		}
		return 0;
	}


	@Override
	protected int getMask() {

		return 0xFFFF;
	}


	@Override
	protected int canConnectToBlock(World world, int x, int y, int z, Block block, PC_Direction dir, PC_Direction dir2) {
		if(block instanceof BlockRedstoneWire){
			return dir2 == PC_Direction.DOWN || (dir2.getOpposite() == PC_MultiblockIndex.getFaceDir(index) && dir==PC_Direction.DOWN)?0xFF | (16 << 16) : 0;
		}
		return block != null && block.canProvidePower() ? 0xFF | (16 << 16) : 0;
	}


	@Override
	public boolean canConnectRedstone(PC_Direction side) {

		return side!=PC_Direction.UNKNOWN && (PC_MultiblockIndex.getFaceDir(index) == PC_Direction.DOWN || PC_MultiblockIndex.getFaceDir(index) == side);
	}


	@Override
	protected void updateGrid(boolean updateIO) {

		if (!isClient() && !removed) {
			if (grid == null) {
				getGridIfNull();
			} else if (updateIO) {
				grid.remove(this);
				addToGrid();
			}
			grid.onUpdateTick(this);
		}
	}


	private void getGridIfNull() {

		if (!isClient() && grid == null) {
			PC_Direction dir = PC_MultiblockIndex.getFaceDir(index);
			int i = 0;
			for (PC_Direction dir2 : PC_Direction.VALID_DIRECTIONS) {
				if (dir2 == dir || dir2 == dir.getOpposite()) {
					continue;
				}
				int connection[] = getConnections(i);
				if (connection != null) {
					if ((connection[0] & 0xFFFF) != 0) {
						useGrid(getGrid(0, 0, 0, dir2));
					}
					if (connection.length > 1 && (connection[1] & 0xFFFF) != 0) {
						useGrid(getGrid(dir.offsetX, dir.offsetY, dir.offsetZ, dir2));
					}
					if (connection.length > 2 && (connection[2] & 0xFFFF) != 0) {
						useGrid(getGrid(dir2.offsetX, dir2.offsetY, dir2.offsetZ, dir));
						useGrid(getGrid(dir2.offsetX, dir2.offsetY, dir2.offsetZ, dir2.getOpposite()));
					}
					if (connection.length > 3 && (connection[3] & 0xFFFF) != 0) {
						useGrid(getGrid(dir.offsetX + dir2.offsetX, dir.offsetY + dir2.offsetY, dir.offsetZ + dir2.offsetZ, dir2.getOpposite()));
					}
					if (connection.length > 4 && (connection[4] & 0xFFFF) != 0) {
						useGrid(getGrid(dir.offsetX + dir2.offsetX, dir.offsetY + dir2.offsetY, dir.offsetZ + dir2.offsetZ, dir.getOpposite()));
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


	private boolean isIO(int xOffset, int yOffset, int zOffset, PC_Direction dir, PC_Direction dir2) {

		Block block = PC_Utils.getBlock(multiblock.worldObj, multiblock.xCoord + xOffset, multiblock.yCoord + yOffset, multiblock.zCoord + zOffset);
		return canConnectToBlock(multiblock.worldObj, multiblock.xCoord + xOffset, multiblock.yCoord + yOffset, multiblock.zCoord + zOffset, block, dir, dir2) != 0;
	}


	private void addToGrid() {

		if (!isClient() && !removed) {
			getGridIfNull();
			if (isIO) {
				grid.addIO(this);
			} else {
				grid.add(this);
			}
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


	private PC_RedstoneGrid getGrid(int xOffset, int yOffset, int zOffset, PC_Direction dir) {

		return getGridS(multiblock.worldObj, multiblock.xCoord + xOffset, multiblock.yCoord + yOffset, multiblock.zCoord + zOffset, dir, -1);
	}


	protected static PC_RedstoneGrid getGridS(World world, int x, int y, int z, PC_Direction dir, int cableID) {

		TileEntity te = PC_Utils.getTE(world, x, y, z);
		if (te instanceof PC_TileEntityMultiblock) {
			PC_TileEntityMultiblock tileEntity = (PC_TileEntityMultiblock) te;
			PC_MultiblockTileEntity multiblockTileEntity = tileEntity.getMultiblockTileEntity(PC_MultiblockIndex.FACEINDEXFORDIR[dir.ordinal()]);
			if (multiblockTileEntity instanceof PC_RedstoneUnisolatedTileEntity) {
				return ((PC_RedstoneUnisolatedTileEntity) multiblockTileEntity).getGrid();
			} else if (multiblockTileEntity instanceof PC_RedstoneIsolatedTileEntity) {
				if (cableID == -1) return ((PC_RedstoneIsolatedTileEntity) multiblockTileEntity).getGrid();
				PC_RedstoneCable cable = ((PC_RedstoneIsolatedTileEntity) multiblockTileEntity).getCableType(cableID);
				if (cable != null) return cable.getGrid();
			}
		}
		return null;
	}


	private void removeFormGrid() {

		if (!isClient() && grid != null) {
			removed = true;
			PC_RedstoneGrid oldGrid = grid;
			grid.remove(this);
			grid = null;
			boolean isFirst = true;
			List<PC_RedstoneGrid> doneGrids = new ArrayList<PC_RedstoneGrid>();
			PC_Direction dir = PC_MultiblockIndex.getFaceDir(index);
			int i = 0;
			for (PC_Direction dir2 : PC_Direction.VALID_DIRECTIONS) {
				if (dir2 == dir || dir2 == dir.getOpposite()) {
					continue;
				}
				int connection[] = getConnections(i);
				if (connection != null) {
					if ((connection[0] & 0xFFFF) != 0) {
						PC_RedstoneGrid g = getGrid(0, 0, 0, dir2);
						if (g != null && !doneGrids.contains(g)) {
							if (isFirst) {
								isFirst = false;
							} else {
								PC_RedstoneGrid ng = new PC_RedstoneGrid();
								doneGrids.add(ng);
								setBlockAndNeighborGrid(dir.offsetX, dir.offsetY, dir.offsetZ, dir2, ng);
							}
						}
					}
					if (connection.length > 1 && (connection[1] & 0xFFFF) != 0) {
						PC_RedstoneGrid g = getGrid(dir.offsetX, dir.offsetY, dir.offsetZ, dir2);
						if (g != null && !doneGrids.contains(g)) {
							if (isFirst) {
								isFirst = false;
							} else {
								PC_RedstoneGrid ng = new PC_RedstoneGrid();
								doneGrids.add(ng);
								setBlockAndNeighborGrid(dir.offsetX, dir.offsetY, dir.offsetZ, dir2, ng);
							}
						}
					}
					if (connection.length > 2 && (connection[2] & 0xFFFF) != 0) {
						PC_RedstoneGrid g = getGrid(dir2.offsetX, dir2.offsetY, dir2.offsetZ, dir);
						if (g != null && !doneGrids.contains(g)) {
							if (isFirst) {
								isFirst = false;
							} else {
								PC_RedstoneGrid ng = new PC_RedstoneGrid();
								doneGrids.add(ng);
								setBlockAndNeighborGrid(dir2.offsetX, dir2.offsetY, dir2.offsetZ, dir, ng);
							}
						}
						g = getGrid(dir2.offsetX, dir2.offsetY, dir2.offsetZ, dir2.getOpposite());
						if (g != null && !doneGrids.contains(g)) {
							if (isFirst) {
								isFirst = false;
							} else {
								PC_RedstoneGrid ng = new PC_RedstoneGrid();
								doneGrids.add(ng);
								setBlockAndNeighborGrid(dir2.offsetX, dir2.offsetY, dir2.offsetZ, dir2.getOpposite(), ng);
							}
						}
					}
					if (connection.length > 3 && (connection[3] & 0xFFFF) != 0) {
						PC_RedstoneGrid g = getGrid(dir.offsetX + dir2.offsetX, dir.offsetY + dir2.offsetY, dir.offsetZ + dir2.offsetZ,
								dir2.getOpposite());
						if (g != null && !doneGrids.contains(g)) {
							if (isFirst) {
								isFirst = false;
							} else {
								PC_RedstoneGrid ng = new PC_RedstoneGrid();
								doneGrids.add(ng);
								setBlockAndNeighborGrid(dir.offsetX + dir2.offsetX, dir.offsetY + dir2.offsetY, dir.offsetZ + dir2.offsetZ,
										dir2.getOpposite(), ng);
							}
						}
					}
					if (connection.length > 4 && (connection[4] & 0xFFFF) != 0) {
						PC_RedstoneGrid g = getGrid(dir.offsetX + dir2.offsetX, dir.offsetY + dir2.offsetY, dir.offsetZ + dir2.offsetZ,
								dir.getOpposite());
						if (g != null && !doneGrids.contains(g)) {
							if (isFirst) {
								isFirst = false;
							} else {
								PC_RedstoneGrid ng = new PC_RedstoneGrid();
								doneGrids.add(ng);
								setBlockAndNeighborGrid(dir.offsetX + dir2.offsetX, dir.offsetY + dir2.offsetY, dir.offsetZ + dir2.offsetZ,
										dir.getOpposite(), ng);
							}
						}
					}
				}
				i++;
			}
			for (PC_RedstoneGrid grid:doneGrids) {
				grid.onUpdateTick(this);
			}
			oldGrid.onUpdateTick(this);
		}
	}


	private void setBlockAndNeighborGrid(int xOffset, int yOffset, int zOffset, PC_Direction dir, PC_RedstoneGrid grid) {

		setBlockAndNeighborGridS(multiblock.worldObj, multiblock.xCoord + xOffset, multiblock.yCoord + yOffset, multiblock.zCoord + zOffset, dir, -1,
				grid);
	}


	protected static int getCable(int mask) {

		int cable = -1;
		for (int i = 0; i < 16; i++) {
			if ((mask & 1 << i) != 0) {
				if (cable != -1) return -1;
				cable = i;
			}
		}
		return cable;
	}


	protected static void setBlockAndNeighborGridS(World world, int x, int y, int z, PC_Direction dir, int cable, PC_RedstoneGrid grid) {

		@SuppressWarnings("hiding")
		class PRC {

			public int x;
			public int y;
			public int z;
			public PC_Direction dir;
			public int cable;


			public PRC(int x, int y, int z, PC_Direction dir, int cable) {

				super();
				this.x = x;
				this.y = y;
				this.z = z;
				this.dir = dir;
				this.cable = cable;
			}


			@Override
			public int hashCode() {

				final int prime = 31;
				int result = 1;
				result = prime * result + cable;
				result = prime * result + ((dir == null) ? 0 : dir.hashCode());
				result = prime * result + x;
				result = prime * result + y;
				result = prime * result + z;
				return result;
			}


			@Override
			public boolean equals(Object obj) {

				if (this == obj) return true;
				if (obj == null) return false;
				if (getClass() != obj.getClass()) return false;
				PRC other = (PRC) obj;
				if (cable != other.cable) return false;
				if (dir != other.dir) return false;
				if (x != other.x) return false;
				if (y != other.y) return false;
				if (z != other.z) return false;
				return true;
			}
		}
		List<PRC> toLookAt = new ArrayList<PRC>();
		toLookAt.add(new PRC(x, y, z, dir, cable));
		PC_IRedstoneCable first=null;
		while (!toLookAt.isEmpty()) {
			PRC posAndRot = toLookAt.remove(0);
			PC_TileEntityMultiblock tileEntity = PC_Utils.getTE(world, posAndRot.x, posAndRot.y, posAndRot.z);
			if (tileEntity != null) {
				PC_MultiblockTileEntity multiblockTileEntity = tileEntity.getMultiblockTileEntity(PC_MultiblockIndex.FACEINDEXFORDIR[posAndRot.dir
						.ordinal()]);
				if (multiblockTileEntity instanceof PC_RedstoneUnisolatedTileEntity) {
					PC_RedstoneUnisolatedTileEntity unisolated = (PC_RedstoneUnisolatedTileEntity) multiblockTileEntity;
					PC_RedstoneGrid og = unisolated.getGrid();
					if(first==null)
						first = unisolated;
					og.remove(unisolated);
					unisolated.setGrid(grid);
					unisolated.addToGrid();
					int i = 0;
					dir = posAndRot.dir;
					for (PC_Direction dir2 : PC_Direction.VALID_DIRECTIONS) {
						if (dir2 == dir || dir2 == dir.getOpposite()) {
							continue;
						}
						int connection[] = unisolated.getConnections(i);
						if (connection != null) {
							if ((connection[0] & 0xFFFF) != 0) {
								PC_RedstoneGrid g = getGridS(world, posAndRot.x, posAndRot.y, posAndRot.z, dir2, -1);
								if (g != null && g != grid) {
									PRC p = new PRC(posAndRot.x, posAndRot.y, posAndRot.z, dir2, getCable(connection[0] & 0xFFFF));
									if (!toLookAt.contains(p)) {
										toLookAt.add(p);
									}
								}
							}
							if (connection.length > 1 && (connection[1] & 0xFFFF) != 0) {
								PC_RedstoneGrid g = getGridS(world, posAndRot.x + dir.offsetX, posAndRot.y + dir.offsetY, posAndRot.z + dir.offsetZ,
										dir2, -1);
								if (g != null && g != grid) {
									PRC p = new PRC(posAndRot.x + dir.offsetX, posAndRot.y + dir.offsetY, posAndRot.z + dir.offsetZ, dir2,
											getCable(connection[1] & 0xFFFF));
									if (!toLookAt.contains(p)) {
										toLookAt.add(p);
									}
								}
							}
							if (connection.length > 2 && (connection[2] & 0xFFFF) != 0) {
								PC_RedstoneGrid g = getGridS(world, posAndRot.x + dir2.offsetX, posAndRot.y + dir2.offsetY, posAndRot.z
										+ dir2.offsetZ, dir, -1);
								if (g != null && g != grid) {
									PRC p = new PRC(posAndRot.x + dir2.offsetX, posAndRot.y + dir2.offsetY, posAndRot.z + dir2.offsetZ, dir,
											getCable(connection[2] & 0xFFFF));
									if (!toLookAt.contains(p)) {
										toLookAt.add(p);
									}
								}
								g = getGridS(world, posAndRot.x + dir2.offsetX, posAndRot.y + dir2.offsetY, posAndRot.z + dir2.offsetZ,
										dir2.getOpposite(), -1);
								if (g != null && g != grid) {
									PRC p = new PRC(posAndRot.x + dir2.offsetX, posAndRot.y + dir2.offsetY, posAndRot.z + dir2.offsetZ,
											dir2.getOpposite(), getCable(connection[2] & 0xFFFF));
									if (!toLookAt.contains(p)) {
										toLookAt.add(p);
									}
								}
							}
							if (connection.length > 3 && (connection[3] & 0xFFFF) != 0) {
								PC_RedstoneGrid g = getGridS(world, posAndRot.x + dir.offsetX + dir2.offsetX, posAndRot.y + dir.offsetY
										+ dir2.offsetY, posAndRot.z + dir.offsetZ + dir2.offsetZ, dir2.getOpposite(), -1);
								if (g != null && g != grid) {
									PRC p = new PRC(posAndRot.x + dir.offsetX + dir2.offsetX, posAndRot.y + dir.offsetY + dir2.offsetY, posAndRot.z
											+ dir.offsetZ + dir2.offsetZ, dir2.getOpposite(), getCable(connection[3] & 0xFFFF));
									if (!toLookAt.contains(p)) {
										toLookAt.add(p);
									}
								}
							}
							if (connection.length > 4 && (connection[4] & 0xFFFF) != 0) {
								PC_RedstoneGrid g = getGridS(world, posAndRot.x + dir.offsetX + dir2.offsetX, posAndRot.y + dir.offsetY
										+ dir2.offsetY, posAndRot.z + dir.offsetZ + dir2.offsetZ, dir.getOpposite(), -1);
								if (g != null && g != grid) {
									PRC p = new PRC(posAndRot.x + dir.offsetX + dir2.offsetX, posAndRot.y + dir.offsetY + dir2.offsetY, posAndRot.z
											+ dir.offsetZ + dir2.offsetZ, dir.getOpposite(), getCable(connection[4] & 0xFFFF));
									if (!toLookAt.contains(p)) {
										toLookAt.add(p);
									}
								}
							}
						}
						i++;
					}
				} else if (multiblockTileEntity instanceof PC_RedstoneIsolatedTileEntity) {
					PC_RedstoneIsolatedTileEntity isolated = (PC_RedstoneIsolatedTileEntity) multiblockTileEntity;
					if (posAndRot.cable == -1) {
						if (isolated.getCableCount() != 1) continue;
						posAndRot.cable = getCable(isolated.getMask());
					}
					PC_RedstoneCable rCable = isolated.getCableType(posAndRot.cable);
					if(first==null)
						first = rCable;
					PC_RedstoneGrid og = rCable.getGrid();
					if (og != null) og.remove(rCable);
					rCable.setGrid(grid);
					rCable.addToGrid();
					int i = 0;
					dir = posAndRot.dir;
					for (PC_Direction dir2 : PC_Direction.VALID_DIRECTIONS) {
						if (dir2 == dir || dir2 == dir.getOpposite()) {
							continue;
						}
						int connection[] = isolated.getConnections(i);
						if (connection != null) {
							if ((connection[0] & rCable.getCableType()) != 0) {
								PC_RedstoneGrid g = getGridS(world, posAndRot.x, posAndRot.y, posAndRot.z, dir2, getCable(rCable.getCableType()));
								if (g != null && g != grid) {
									PRC p = new PRC(posAndRot.x, posAndRot.y, posAndRot.z, dir2, getCable(rCable.getCableType()));
									if (!toLookAt.contains(p)) {
										toLookAt.add(p);
									}
								}
							}
							if (connection.length > 1 && (connection[1] & rCable.getCableType()) != 0) {
								PC_RedstoneGrid g = getGridS(world, posAndRot.x + dir.offsetX, posAndRot.y + dir.offsetY, posAndRot.z + dir.offsetZ,
										dir2, getCable(rCable.getCableType()));
								if (g != null && g != grid) {
									PRC p = new PRC(posAndRot.x + dir.offsetX, posAndRot.y + dir.offsetY, posAndRot.z + dir.offsetZ, dir2,
											getCable(rCable.getCableType()));
									if (!toLookAt.contains(p)) {
										toLookAt.add(p);
									}
								}
							}
							if (connection.length > 2 && (connection[2] & rCable.getCableType()) != 0) {
								PC_RedstoneGrid g = getGridS(world, posAndRot.x + dir2.offsetX, posAndRot.y + dir2.offsetY, posAndRot.z
										+ dir2.offsetZ, dir, getCable(rCable.getCableType()));
								if (g != null && g != grid) {
									PRC p = new PRC(posAndRot.x + dir2.offsetX, posAndRot.y + dir2.offsetY, posAndRot.z + dir2.offsetZ, dir,
											getCable(rCable.getCableType()));
									if (!toLookAt.contains(p)) {
										toLookAt.add(p);
									}
								}
								g = getGridS(world, posAndRot.x + dir2.offsetX, posAndRot.y + dir2.offsetY, posAndRot.z + dir2.offsetZ,
										dir2.getOpposite(), getCable(rCable.getCableType()));
								if (g != null && g != grid) {
									PRC p = new PRC(posAndRot.x + dir2.offsetX, posAndRot.y + dir2.offsetY, posAndRot.z + dir2.offsetZ,
											dir2.getOpposite(), getCable(rCable.getCableType()));
									if (!toLookAt.contains(p)) {
										toLookAt.add(p);
									}
								}
							}
							if (connection.length > 3 && (connection[3] & rCable.getCableType()) != 0) {
								PC_RedstoneGrid g = getGridS(world, posAndRot.x + dir.offsetX + dir2.offsetX, posAndRot.y + dir.offsetY
										+ dir2.offsetY, posAndRot.z + dir.offsetZ + dir2.offsetZ, dir2.getOpposite(), getCable(rCable.getCableType()));
								if (g != null && g != grid) {
									PRC p = new PRC(posAndRot.x + dir.offsetX + dir2.offsetX, posAndRot.y + dir.offsetY + dir2.offsetY, posAndRot.z
											+ dir.offsetZ + dir2.offsetZ, dir2.getOpposite(), getCable(rCable.getCableType()));
									if (!toLookAt.contains(p)) {
										toLookAt.add(p);
									}
								}
							}
							if (connection.length > 4 && (connection[4] & rCable.getCableType()) != 0) {
								PC_RedstoneGrid g = getGridS(world, posAndRot.x + dir.offsetX + dir2.offsetX, posAndRot.y + dir.offsetY
										+ dir2.offsetY, posAndRot.z + dir.offsetZ + dir2.offsetZ, dir.getOpposite(), getCable(rCable.getCableType()));
								if (g != null && g != grid) {
									PRC p = new PRC(posAndRot.x + dir.offsetX + dir2.offsetX, posAndRot.y + dir.offsetY + dir2.offsetY, posAndRot.z
											+ dir.offsetZ + dir2.offsetZ, dir.getOpposite(), getCable(rCable.getCableType()));
									if (!toLookAt.contains(p)) {
										toLookAt.add(p);
									}
								}
							}
						}
						i++;
					}
				}
			}
		}
	}


	@Override
	public void onBreak() {

		super.onBreak();
		removeFormGrid();
	}


	@Override
	public void onChunkUnload() {

		super.onChunkUnload();
		removeFormGrid();
	}


	@Override
	public int getRedstonePowerValue(PC_Direction side) {

		if (!isClient() && side!=PC_Direction.UNKNOWN) {
			getGridIfNull();
			PC_Direction dir = side.getOpposite();
			PC_Direction dir2 = PC_MultiblockIndex.getFaceDir(index);
			if((isIO && isIO(dir.offsetX, dir.offsetY, dir.offsetZ, PC_Direction.DOWN, dir2.getOpposite())) || dir==dir2){
				return grid.getRedstonePowerValue();
			}
		}
		return 0;
	}


	@Override
	public void addPoweringBlocks(List<PC_Vec3IWithRotation> poweringBlocks) {

		PC_Direction dir = PC_MultiblockIndex.getFaceDir(index);
		int i = 0;
		for (PC_Direction dir2 : PC_Direction.VALID_DIRECTIONS) {
			if (dir2 == dir || dir2 == dir.getOpposite()) {
				continue;
			}
			int connection[] = getConnections(i);
			if (connection != null) {
				if (connection.length > 1 && (connection[1] & 0xFFFF) != 0) {
					if (isIO(dir.offsetX, dir.offsetY, dir.offsetZ, dir, dir2)) {
						poweringBlocks.add(new PC_Vec3IWithRotation(multiblock.xCoord + dir.offsetX, multiblock.yCoord + dir.offsetY,
								multiblock.zCoord + dir.offsetZ, dir));
					}
				}
				if (connection.length > 2 && (connection[2] & 0xFFFF) != 0) {
					if (isIO(dir2.offsetX, dir2.offsetY, dir2.offsetZ, dir2, dir)) {
						poweringBlocks.add(new PC_Vec3IWithRotation(multiblock.xCoord + dir2.offsetX, multiblock.yCoord + dir2.offsetY,
								multiblock.zCoord + dir2.offsetZ, dir2));
					}
				}
				if (connection.length > 3 && (connection[3] & 0xFFFF) != 0) {
					if (isIO(dir.offsetX + dir2.offsetX, dir.offsetY + dir2.offsetY, dir.offsetZ + dir2.offsetZ, dir, dir2)) {
						poweringBlocks.add(new PC_Vec3IWithRotation(multiblock.xCoord + dir.offsetX + dir2.offsetX, multiblock.yCoord + dir.offsetY
								+ dir2.offsetY, multiblock.zCoord + dir.offsetZ + dir2.offsetZ, dir));
					}
				}
			}
			i++;
		}
	}


	@Override
	public World getWorld() {

		return multiblock.worldObj;
	}


	@Override
	public void onRedstonePowerChange() {

		PC_Utils.hugeUpdate(multiblock.worldObj, multiblock.xCoord, multiblock.yCoord, multiblock.zCoord);
	}


	@Override
	public void update() {

		if (!isClient()){
			if(grid == null) {
				updateGrid(false);
			}else if(grid.firstTick){
				grid.firstTick = false;
				grid.onUpdateTick(this);
			}
		}
	}


	@Override
	protected int getColorForCable(int cableID) {

		return 0xFFFFFFFF;
	}


	@Override
	protected boolean useOverlay() {
		return false;
	}

	@Override
	public ItemStack getPickBlock() {
		return new ItemStack(PC_RedstoneUnisolatedItem.item, 1, 0);
	}

}
