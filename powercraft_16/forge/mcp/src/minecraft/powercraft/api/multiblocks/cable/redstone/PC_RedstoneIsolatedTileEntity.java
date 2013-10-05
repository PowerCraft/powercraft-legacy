package powercraft.api.multiblocks.cable.redstone;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Utils;
import powercraft.api.multiblocks.PC_MultiblockIndex;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;
import powercraft.api.multiblocks.cable.PC_CableTileEntity;


public class PC_RedstoneIsolatedTileEntity extends PC_CableTileEntity {

	private boolean firstTick = true;
	private boolean secoundTick = true;
	private PC_RedstoneCable cable[] = new PC_RedstoneCable[16];


	public PC_RedstoneIsolatedTileEntity(NBTTagCompound nbtTagCompound) {
		super(nbtTagCompound);
		int mask = nbtTagCompound.getInteger("mask");
		for (int i = 0; i < cable.length; i++) {
			if ((mask & (1 << i)) == 0) {
				cable[i] = null;
			} else {
				cable[i] = new PC_RedstoneCable(1 << i);
				cable[i].setTileEntity(this);
			}
		}
		calculateThickness();
	}
	
	public PC_RedstoneIsolatedTileEntity() {

		super(2, 4);
	}


	public PC_RedstoneIsolatedTileEntity(int cableType) {

		super(2, 4);
		cable[cableType] = new PC_RedstoneCable(1 << cableType);
		cable[cableType].setTileEntity(this);
	}


	@Override
	protected PC_RedstoneCable getCableType(int cableID) {

		return cable[cableID];
	}


	public boolean isIO() {

		return isIO;
	}


	@Override
	public boolean canMixWith(PC_MultiblockTileEntity tileEntity) {

		if (tileEntity instanceof PC_RedstoneIsolatedTileEntity) {
			PC_RedstoneIsolatedTileEntity redstoneIsolated = (PC_RedstoneIsolatedTileEntity) tileEntity;
			for (int i = 0; i < cable.length; i++) {
				if (cable[i] != null && redstoneIsolated.cable[i] != null) return false;
			}
			return true;
		}
		return false;
	}


	@Override
	public PC_MultiblockTileEntity mixWith(PC_MultiblockTileEntity tileEntity) {

		PC_RedstoneIsolatedTileEntity redstoneIsolated = (PC_RedstoneIsolatedTileEntity) tileEntity;
		for (int i = 0; i < cable.length; i++) {
			if (cable[i] == null && redstoneIsolated.cable[i] != null) {
				cable[i] = redstoneIsolated.cable[i];
				cable[i].setTileEntity(this);
				checkConnections(false);
			}
		}
		calculateThickness();
		return this;
	}


	@Override
	protected Icon getCableIcon() {

		return PC_RedstoneIsolatedItem.getCableIcon();
	}


	@Override
	protected Icon getCableLineIcon(int index) {

		return PC_RedstoneIsolatedItem.getCableLineIcon(index);
	}


	@Override
	public List<ItemStack> getDrop() {

		List<ItemStack> drops = new ArrayList<ItemStack>();
		for(int i=0; i<cable.length; i++){
			if(cable[i]!=null)
				drops.add(new ItemStack(PC_RedstoneIsolatedItem.item, 1, i));
		}
		return drops;
	}


	@Override
	public void saveToNBT(NBTTagCompound nbtCompoundTag) {

		nbtCompoundTag.setInteger("mask", getMask());
		super.saveToNBT(nbtCompoundTag);
	}


	private void calculateThickness() {

		thickness = 2 + getCableCount() / 6;
		width = thickness * 2;
	}


	public int getCableCount() {

		int num = 0;
		for (int i = 0; i < cable.length; i++) {
			if (cable[i] != null) {
				num++;
			}
		}
		return num;
	}


	@Override
	public int getMask() {

		int mask = 0;
		for (int i = 0; i < cable.length; i++) {
			if (cable[i] != null) {
				mask |= 1 << i;
			}
		}
		return mask;
	}


	@Override
	protected int canConnectToMultiblock(PC_MultiblockTileEntity multiblock) {

		if (multiblock instanceof PC_RedstoneIsolatedTileEntity) {
			PC_RedstoneIsolatedTileEntity isolated = (PC_RedstoneIsolatedTileEntity) multiblock;
			int connection = isolated.getMask();
			if (connection != 0) {
				int length = 16;
				if (isolated.getCenterThickness() > 0) length = isolated.getCenterThickness() + 2 + isolated.getThickness() * 2;
				return connection | (length << 16);
			}
		} else if (multiblock instanceof PC_RedstoneUnisolatedTileEntity) {
			PC_RedstoneUnisolatedTileEntity unisolated = (PC_RedstoneUnisolatedTileEntity) multiblock;
			if (getCableCount() == 1) {
				int connection = getMask();
				int length = 16;
				if (unisolated.getCenterThickness() > 0) length = unisolated.getCenterThickness() + 2 + unisolated.getThickness() * 2;
				return connection | (length << 16);
			}
		}
		return 0;
	}


	@Override
	protected int canConnectToBlock(World world, int x, int y, int z, Block block, PC_Direction dir, PC_Direction dir2) {

		if (getCableCount() == 1){
			if(block instanceof BlockRedstoneWire){
				return dir2 == PC_Direction.DOWN || (dir2.getOpposite() == PC_MultiblockIndex.getFaceDir(index) && dir==PC_Direction.DOWN)? getMask() | (16 << 16) : 0;
			}
			return (block != null && PC_Utils.canConnectRedstone(world, x, y, z, dir2)) ? getMask() | (16 << 16) : 0;
		}
		return 0;
	}


	@Override
	public boolean canConnectRedstone(PC_Direction side) {

		return side!=PC_Direction.UNKNOWN && getCableCount() == 1 && (PC_MultiblockIndex.getFaceDir(index) == PC_Direction.DOWN || PC_MultiblockIndex.getFaceDir(index) == side);
	}


	@Override
	protected void updateGrid(boolean updateIO) {

		for (int i = 0; i < cable.length; i++) {
			if (cable[i] != null) {
				if (cable[i].getGrid() == null) {
					cable[i].getGridIfNull();
				} else if (updateIO) {
					cable[i].getGrid().remove(cable[i]);
					cable[i].addToGrid();
				}
				cable[i].getGrid().onUpdateTick(cable[i]);
			}
		}
	}


	public PC_RedstoneGrid getGrid() {

		PC_RedstoneGrid grid = null;
		for (int i = 0; i < cable.length; i++) {
			if (cable[i] != null) {
				if (grid != null) return null;
				grid = cable[i].getGrid();
			}
		}
		return grid;
	}


	public World getWorld() {

		return multiblock.worldObj;
	}


	@Override
	public void update() {

		if (!isClient() && firstTick) {
			firstTick = false;
			updateGrid(false);
		}else if(!isClient() && secoundTick){
			secoundTick = false;
			for (int i = 0; i < cable.length; i++) {
				if (cable[i] != null) {
					if (cable[i].getGrid() != null && cable[i].getGrid().firstTick) {
						cable[i].getGrid().firstTick = false;
						cable[i].getGrid().onUpdateTick(cable[i]);
					} 
				}
			}
		}
	}


	protected PC_RedstoneGrid getGrid(int xOffset, int yOffset, int zOffset, PC_Direction dir, int cableID) {

		return PC_RedstoneUnisolatedTileEntity.getGridS(multiblock.worldObj, multiblock.xCoord + xOffset, multiblock.yCoord + yOffset,
				multiblock.zCoord + zOffset, dir, PC_RedstoneUnisolatedTileEntity.getCable(cableID));
	}


	protected void setBlockAndNeighborGrid(int xOffset, int yOffset, int zOffset, PC_Direction dir, int cableID, PC_RedstoneGrid grid) {

		PC_RedstoneUnisolatedTileEntity.setBlockAndNeighborGridS(multiblock.worldObj, multiblock.xCoord + xOffset, multiblock.yCoord + yOffset,
				multiblock.zCoord + zOffset, dir, PC_RedstoneUnisolatedTileEntity.getCable(cableID), grid);
	}


	@Override
	public void onBreak() {

		super.onBreak();
		for (int i = 0; i < cable.length; i++) {
			if (cable[i] != null) {
				cable[i].removeFormGrid();
			}
		}
	}


	@Override
	public void onChunkUnload() {

		super.onChunkUnload();
		for (int i = 0; i < cable.length; i++) {
			if (cable[i] != null) {
				cable[i].removeFormGrid();
			}
		}
	}


	public boolean isIO(int xOffset, int yOffset, int zOffset, PC_Direction dir, PC_Direction dir2) {

		Block block = PC_Utils.getBlock(multiblock.worldObj, multiblock.xCoord + xOffset, multiblock.yCoord + yOffset, multiblock.zCoord + zOffset);
		return canConnectToBlock(multiblock.worldObj, multiblock.xCoord + xOffset, multiblock.yCoord + yOffset, multiblock.zCoord + zOffset, block, dir, dir2) != 0;
	}


	@Override
	public int getRedstonePowerValue(PC_Direction side) {

		if (!isClient() && side!=PC_Direction.UNKNOWN) {
			PC_RedstoneGrid grid = getGrid();
			if (grid != null && isIO) {
				PC_Direction dir = side.getOpposite();
				PC_Direction dir2 = PC_MultiblockIndex.getFaceDir(index);
				if(isIO(dir.offsetX, dir.offsetY, dir.offsetZ, PC_Direction.DOWN, dir2.getOpposite())){
					return grid.getRedstonePowerValue();
				}
			}
		}
		return 0;
	}


	@Override
	protected int getColorForCable(int cableID) {

		return ItemDye.dyeColors[cableID];
	}


	@Override
	protected boolean useOverlay() {
		return true;
	}

	@Override
	public ItemStack getPickBlock() {
		return null;
	}

}
