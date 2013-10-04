package powercraft.api.multiblocks.cable;


import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import powercraft.api.PC_Direction;
import powercraft.api.PC_Utils;
import powercraft.api.multiblocks.PC_BlockMultiblock;
import powercraft.api.multiblocks.PC_MultiblockIndex;
import powercraft.api.multiblocks.PC_MultiblockTileEntity;
import powercraft.api.multiblocks.PC_TileEntityMultiblock;
import powercraft.api.multiblocks.cable.redstone.PC_IRedstoneCable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
public abstract class PC_CableTileEntity extends PC_MultiblockTileEntity {

	public static int xCoord;
	public static int yCoord;
	public static int zCoord;
	public static RenderBlocks renderer;
	public static PC_Direction dir;
	public static PC_Direction dir2;
	public static double min1;
	public static double max1;
	public static double min2;
	public static double max2;

	protected int width;
	private int centerThickness;
	private int connections[][] = new int[4][];
	protected boolean isIO;
	
	public PC_CableTileEntity(NBTTagCompound nbtTagCompound) {
		super(nbtTagCompound);
		for (int i = 0; i < connections.length; i++) {
			if (nbtTagCompound.hasKey("connections" + i))
				connections[i] = nbtTagCompound.getIntArray("connections" + i);
			else
				connections[i] = null;
		}
		centerThickness = nbtTagCompound.getInteger("centerThickness");
		isIO = nbtTagCompound.getBoolean("isIO");
		width = nbtTagCompound.getInteger("width");
	}
	
	public PC_CableTileEntity(int thickness, int width) {

		super(thickness);
		this.width = width;
	}


	protected abstract PC_IRedstoneCable getCableType(int cableID);


	protected abstract Icon getCableIcon();


	protected abstract Icon getCableLineIcon(int index);


	protected abstract boolean useOverlay();

	
	protected abstract int getColorForCable(int cableID);


	protected abstract int getMask();


	protected abstract void updateGrid(boolean updateIO);


	public int[] getConnections(int n) {

		return connections[n];
	}

	private static boolean[][] fix = { { false, false, false, false, false, false }, { false, false, false, false, false, false },
			{ true, true, true, true, true, true }, { true, true, true, true, true, true }, { true, true, false, false, false, false },
			{ true, true, false, false, false, false } };


	private static boolean overlappingFix(PC_Direction dir, PC_Direction dir2) {
		return fix[dir.ordinal()][dir2.ordinal()];
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void renderWorldBlock(RenderBlocks renderer) {

		PC_CableTileEntity.renderer = renderer;
		xCoord = multiblock.xCoord;
		yCoord = multiblock.yCoord;
		zCoord = multiblock.zCoord;
		dir = PC_MultiblockIndex.getFaceDir(index);
		float s = thickness / 16.0f;
		float w = width / 32.0f;
		PC_BlockMultiblock.setIcons(getCableIcon());
		min1 = 1 - s;
		min2 = 0;
		max1 = 1;
		max2 = s;
		if (centerThickness > 0) {
			float t = (centerThickness + 2) / 32.0f;
			min1 = 0.5f + t;
			min2 = 0.5f - t - s;
			max1 = 0.5f + t + s;
			max2 = 0.5f - t;
		}
		int c[]=new int[4];
		int i = 0;
		for (PC_Direction dir2 : PC_Direction.VALID_DIRECTIONS) {
			PC_CableTileEntity.dir2 = dir2;
			if (dir2 == dir || dir2.getOpposite() == dir) continue;
			int connection[] = connections[i++];
			float minYE = 0;
			float maxYE = 1;
			boolean hd = false;
			if (connection != null) {
				int c1 = connection[0];
				double c1e = (c1 >> 16) / 32.0;
				c1 &= 0xFFFF;
				int c2 = connection.length > 1 ? connection[1] : 0;
				double c2e = (c2 >> 16) / 32.0;
				c2 &= 0xFFFF;
				int c3 = connection.length > 2 ? connection[2] : 0;
				double c3e = (c3 >> 16) / 32.0;
				c3 &= 0xFFFF;
				int c4 = connection.length > 3 ? connection[3] : 0;
				double c4e = (c4 >> 16) / 32.0;
				c4 &= 0xFFFF;
				int c5 = connection.length > 4 ? connection[4] : 0;
				double c5e = (c5 >> 16) / 32.0;
				c5 &= 0xFFFF;
				int p1 = 0;
				double p1e = 0;
				boolean f = overlappingFix(dir, dir2);
				int p2 = 0;
				double p2e = 0;
				int pe = 0;
				int c2ex = c2;
				if((c1 & (getMask() | c2 | c3 | c4 | c5))!=0 && (c2 & (getMask() | c1 | c3 | c4 | c5))!=0){
					if (c1e > c2e) {
						p1e = c2e;
						p2e = c1e;
						c2ex = p1 = (getMask() & c2) | (getMask() & (c1 | c3 | c4 | c5)) | (c2 & (c1 | c3 | c4 | c5));
						p2 = ((getMask() | c2) & c1) | ((getMask() | c2) & (c3 | c4 | c5)) | (c1 & (c3 | c4 | c5));
					} else if (c1e < c2e) {
						p1e = c1e;
						p2e = c2e;
						p1 = (getMask() & c1) | (getMask() & (c2 | c3 | c4 | c5)) | (c1 & (c2 | c3 | c4 | c5));
						c2ex = p2 = ((getMask() | c1) & c2) | ((getMask() | c1) & (c3 | c4 | c5)) | (c2 & (c3 | c4 | c5));
					} else {
						p1e = c1e;
						c2ex = p1 = (c1 & c2) |(getMask() & (c1 | c2)) | (getMask() & (c3 | c4 | c5)) | ((c1 | c2) & (c3 | c4 | c5));
					}
					if(p1==0 && p2!=0){
						p1 = p2;
						p1e = p2e;
						p2 = 0;
						p2e = 0;
					}
				}else if((c1 & (getMask() | c2 | c3 | c4 | c5))!=0){
					p1e = c1e;
					p1 = (getMask() & c1) | (getMask() & (c3 | c4 | c5)) | (c1 & (c3 | c4 | c5));
				}else if((c2 & (getMask() | c1 | c3 | c4 | c5))!=0){
					p1e = c2e;
					c2ex = p1 = (getMask() & c2) | (getMask() & (c3 | c4 | c5)) | (c2 & (c3 | c4 | c5));
				}
				int mask = (getMask() & (p1 | p2 | c3 | c4 | c5)) | (p1 & (p2 | c3 | c4 | c5)) | (p2 & (c3 | c4 | c5));
				c[i-1] = (getMask() & (c1 | c2 | c3 | c4 | c5))==getMask()?1:2;
				pe = mask & (c3 | c4 | c5);
				if ((c1 & mask)!=0) pe = 0;
				if ((c2ex)!=0) pe = 0;
				double pemin = 0.5 - c3e;
				double pemax = 0.5 + c3e;
				boolean ml = (c3&mask) == 0 && (c4&mask) != 0;
				boolean hc = (c3&mask) != 0 && (pemin < min2 || pemax > max1);
				if (p1 != 0) {
					renderCable2(w, w, p1e - s, min2, max1, s, p1 & getMask(), p2==0 && (hc || !ml) && f);
					if (f) {
						renderCable2(w, p1e - s, p1e, min2, max1, s, p1 & getMask(), p2==0 && (hc || !ml));
					}
					if (p2 != 0) {
						renderCable2(w, p1e, p2e - s, min2, max1, s, p2 & p1, pe==0 && (hc || !ml) && f);
						if (f) {
							renderCable2(w, p2e - s, p2e, min2, max1, s, p2 & p1, pe==0 && (hc || !ml));
						}
						if (pe != 0) {
							renderCable2(w, p2e, 0.5, min2, max1, s, p2 & pe, !ml || hc);
						}
					} else if (pe != 0) {
						if (hc) {
							renderCableToOutside(w, p1e - s, p1e, pemin, pemax, p1 & pe);
							renderCable2(w, p1e, 0.5, pemin, pemax, s, p1 & pe, true);
						} else 
							renderCable2(w, p1e, 0.5, min2, max1, s, p1 & pe, !ml);
					}
				} else if (pe != 0) {
					renderCable2(w, w, 0.5, min2, max1, s, getMask() & pe, !ml || hc);
					if (c1 == 0 && hc) {
						renderCableToOutside(w, 0.5 - s, 0.5, pemin, pemax, getMask() & pe);
					}
				}
				if (c2ex != 0) {
					if((c1 & mask)==0){
						min2 += s;
						max1 -= s;
					}
					renderCableToOutside(w, c2e - s, c2e, 0, 1, mask & c2ex);
					if((c1 & mask)==0){
						min2 -= s;
						max1 += s;
					}
				}
				if (ml) {
					xCoord += dir2.offsetX;
					yCoord += dir2.offsetY;
					zCoord += dir2.offsetZ;
					renderCable2(w, -0.5, -c4e, min2, max1, s, pe & (c3 | c4 | c5), !f);
					if (f) {
						renderCable2(w, -c4e, -c4e + s, min2, max1, s, pe & (c3 | c4 | c5), true);
					}
					xCoord -= dir2.offsetX;
					yCoord -= dir2.offsetY;
					zCoord -= dir2.offsetZ;
				}
			}
		}
		renderCable(w, c);
	}


	protected int canConnectToMultiblock(PC_MultiblockTileEntity multiblock) {

		if (multiblock.getClass() != getClass()) return 0;
		int connection = 0xFFFF;
		int length = 16;
		if (((PC_CableTileEntity) multiblock).centerThickness > 0)
			length = ((PC_CableTileEntity) multiblock).centerThickness + 2 + ((PC_CableTileEntity) multiblock).getThickness() * 2;
		return connection | (length << 16);
	}

	protected int canConnectToBlock(World world, int x, int y, int z, Block block, PC_Direction dir, PC_Direction dir2) {

		return 0;
	}


	private int canConnectToBlock(World world, int x, int y, int z, PC_Direction dir, PC_Direction dir2) {

		Block block = PC_Utils.getBlock(world, x, y, z);
		if (block instanceof PC_BlockMultiblock) {
			PC_TileEntityMultiblock multiblock = PC_Utils.getTE(world, x, y, z);
			PC_MultiblockTileEntity mbte = multiblock.getMultiblockTileEntity(PC_MultiblockIndex.FACEINDEXFORDIR[dir.ordinal()]);
			if (mbte != null) 
				return canConnectToMultiblock(mbte);
			return 0;
		}
		if (block != null) {
			int i = canConnectToBlock(world, x, y, z, block, dir, dir2);
			if (i != 0) isIO = true;
			return i;
		}
		return 0;
	}


	private int[] canConnectTo(PC_Direction dir, PC_Direction dir2, int[] oldConnection) {

		World world = multiblock.worldObj;
		int x = multiblock.xCoord;
		int y = multiblock.yCoord;
		int z = multiblock.zCoord;
		int c1 = canConnectToBlock(world, x, y, z, dir2, dir);
		int c2 = canConnectToBlock(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir2, dir.getOpposite());
		int c3 = canConnectToBlock(world, x + dir2.offsetX, y + dir2.offsetY, z + dir2.offsetZ, dir, dir2.getOpposite());
		if (c3 == 0) {
			c3 = canConnectToBlock(world, x + dir2.offsetX, y + dir2.offsetY, z + dir2.offsetZ, dir2.getOpposite(), dir);
		}else{
			c3 |= canConnectToBlock(world, x + dir2.offsetX, y + dir2.offsetY, z + dir2.offsetZ, dir2.getOpposite(), dir) & 0xFFFF;
		}
		int c4 = 0;
		int c5 = 0;
		Block block = PC_Utils.getBlock(world, x + dir2.offsetX, y + dir2.offsetY, z + dir2.offsetZ);
		if (block == null || block instanceof PC_BlockMultiblock) {
			c4 = canConnectToBlock(world, x + dir2.offsetX + dir.offsetX, y + dir2.offsetY + dir.offsetY, z + dir2.offsetZ + dir.offsetZ,
					dir2.getOpposite(), dir.getOpposite());
		}
		Block block2 = PC_Utils.getBlock(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);

		if (block2 == null) {
			if (block == null) {
				c5 = canConnectToBlock(world, x + dir2.offsetX + dir.offsetX, y + dir2.offsetY + dir.offsetY, z + dir2.offsetZ + dir.offsetZ,
						dir.getOpposite(), dir2.getOpposite());
				if (dir.offsetX < 0 || dir.offsetY < 0 || dir.offsetZ < 0) c5 = c5 & 0xFFFF;
			} else {
				c5 = canConnectToBlock(world, x + dir2.offsetX + dir.offsetX, y + dir2.offsetY + dir.offsetY, z + dir2.offsetZ + dir.offsetZ,
						dir.getOpposite(), dir2.getOpposite());
			}
		} else {
			if (block == null) {
				c5 = canConnectToBlock(world, x + dir2.offsetX + dir.offsetX, y + dir2.offsetY + dir.offsetY, z + dir2.offsetZ + dir.offsetZ,
						dir.getOpposite(), dir2.getOpposite()) & 0xFFFF;
			}
		}
		if (c5 != 0) {
			return new int[] { c1, c2, c3, c4, c5 };
		}
		if (c4 != 0) {
			return new int[] { c1, c2, c3, c4 };
		}
		if (c3 != 0) {
			return new int[] { c1, c2, c3 };
		}
		if (c2 != 0) {
			return new int[] { c1, c2 };
		}
		if (c1 != 0) {
			return new int[] { c1 };
		}
		return null;
	}


	public List<ItemStack> checkConnections(boolean b) {

		int i = 0;
		PC_Direction dir = PC_MultiblockIndex.getFaceDir(index);
		int oldCenterThickness = centerThickness;
		centerThickness = 0;

		Block block = PC_Utils.getBlock(multiblock.worldObj, multiblock.xCoord + dir.offsetX, multiblock.yCoord + dir.offsetY, multiblock.zCoord
				+ dir.offsetZ);
		if (block == null
				|| !block.isBlockSolidOnSide(multiblock.worldObj, multiblock.xCoord + dir.offsetX, multiblock.yCoord + dir.offsetY, multiblock.zCoord
						+ dir.offsetZ, ForgeDirection.values()[dir.getOpposite().ordinal()])) {
			if (multiblock.getMultiblockTileEntity(PC_MultiblockIndex.CENTER) != null)
				centerThickness = multiblock.getMultiblockTileEntity(PC_MultiblockIndex.CENTER).getThickness();
			else {
				return multiblock.removeMultiblockTileEntity(index);
			}
		}

		if (isClient()) {
			return null;
		}

		boolean oldIO = isIO;
		boolean notify=oldCenterThickness!=centerThickness;
		isIO = false;
		for (PC_Direction dir2 : PC_Direction.VALID_DIRECTIONS) {
			if (dir2 == dir || dir2.getOpposite() == dir) continue;
			int oldConnection[] = connections[i];
			connections[i] = canConnectTo(dir, dir2, oldConnection);
			i++;
		}
		if(notify || oldIO != isIO)
			multiblock.notifyNeighbors();
		if(!b)
			updateGrid(oldIO != isIO);
		multiblock.sendToClient();
		return null;
	}


	@Override
	public void onNeighborBlockChange(int neighborID) {

		List<ItemStack> drops = checkConnections(neighborID==PC_BlockMultiblock.block.blockID);
		if (drops != null) multiblock.drop(drops);
	}


	@Override
	public boolean onAdded() {

		if (checkConnections(false) != null) {
			return false;
		}
		return true;
	}


	@Override
	public void saveToNBT(NBTTagCompound nbtCompoundTag) {
		super.saveToNBT(nbtCompoundTag);
		for (int i = 0; i < connections.length; i++)
			if (connections[i] != null) nbtCompoundTag.setIntArray("connections" + i, connections[i]);
		nbtCompoundTag.setInteger("centerThickness", centerThickness);
		nbtCompoundTag.setBoolean("isIO", isIO);
		nbtCompoundTag.setInteger("width", width);
	}


	@Override
	public AxisAlignedBB getSelectionBox() {

		float s = thickness / 16.0f;
		float w = width / 32.0f;
		float min[] = { 0, 0.5f - w, 0.5f + w };
		float max[] = { 0.5f - w, 0.5f + w, 1 };
		PC_Direction dir = PC_MultiblockIndex.getFaceDir(index);
		float min1 = 1 - s;
		float min2 = 0;
		float max1 = 1;
		float max2 = s;
		if (centerThickness > 0) {
			float t = (centerThickness + 2) / 32.0f;
			min1 = 0.5f + t;
			min2 = 0.5f - t - s;
			max1 = 0.5f + t + s;
			max2 = 0.5f - t;
		}
		if (dir == PC_Direction.UP || dir == PC_Direction.DOWN) {
			float minY = dir == PC_Direction.UP ? min1 : min2;
			float maxY = dir == PC_Direction.UP ? max1 : max2;
			return AxisAlignedBB.getBoundingBox(0, minY, 0, 1, maxY, 1);
		} else if (dir == PC_Direction.NORTH || dir == PC_Direction.SOUTH) {
			float minZ = dir == PC_Direction.SOUTH ? min1 : min2;
			float maxZ = dir == PC_Direction.SOUTH ? max1 : max2;
			return AxisAlignedBB.getBoundingBox(0, 0, minZ, 1, 1, maxZ);
		} else if (dir == PC_Direction.EAST || dir == PC_Direction.WEST) {
			float minX = dir == PC_Direction.EAST ? min1 : min2;
			float maxX = dir == PC_Direction.EAST ? max1 : max2;
			return AxisAlignedBB.getBoundingBox(minX, 0, 0, maxX, 1, 1);
		}
		return null;
	}


	public int getCenterThickness() {

		return centerThickness;
	}


	public void renderCable(double w, int[] c) {
		int cDir=0;
		if(c[0]==1 && c[1]==1){
			cDir=0;
		}else if(c[2]==1 && c[3]==1){
			cDir=1;
		}else if((c[0]==1 || c[1]==1) && !(c[2]==1 || c[3]==1)){
			cDir=0;
		}else if(!(c[0]==1 || c[1]==1) && (c[2]==1 || c[3]==1)){
			cDir=1;
		}else if(c[0]==1 || c[1]==1 || c[2]==1 || c[3]==1){
			cDir=-1;
		}
		if(dir.offsetX!=0){
			renderer.uvRotateBottom = 2;
			renderer.uvRotateTop = 1;
			renderer.uvRotateEast = 1;
			renderer.uvRotateNorth = cDir==0?0:2;
			renderer.uvRotateSouth = cDir==0?3:1;
			renderer.uvRotateWest = 2;
		}else if(dir.offsetY!=0){
			renderer.uvRotateBottom = cDir==0?0:2;
			renderer.uvRotateTop = cDir==0?0:1;
			renderer.uvRotateEast = 3;
			renderer.uvRotateNorth = 0;
			renderer.uvRotateSouth = 3;
			renderer.uvRotateWest = 0;
		}else{
			renderer.uvRotateBottom = 0;
			renderer.uvRotateTop = 0;
			renderer.uvRotateEast = cDir==0?3:1;
			renderer.uvRotateNorth = 2;
			renderer.uvRotateSouth = 1;
			renderer.uvRotateWest = cDir==0?0:2;
		}
		Icon cableIcon = getCableIcon();
		Icon iconArray[] = new Icon[6];
		int i = 0;
		for (PC_Direction dir2 : PC_Direction.VALID_DIRECTIONS) {
			if (dir2 == dir || dir2.getOpposite() == dir){
				iconArray[dir2.ordinal()] = cableIcon;
				continue;
			}
			if(c[i]==1){
				iconArray[dir2.ordinal()] = null;
			}else{
				iconArray[dir2.ordinal()] = cableIcon;
			}
			i++;
		}
		PC_BlockMultiblock.setIcons(iconArray);
		if (dir.offsetX > 0) {
			renderer.setRenderBounds(min1, 0.5-w, 0.5-w, max1, 0.5+w, 0.5+w);
		} else if (dir.offsetX < 0) {
			renderer.setRenderBounds(min2, 0.5-w, 0.5-w, max2, 0.5+w, 0.5+w);
		} else if (dir.offsetY > 0) {
			renderer.setRenderBounds(0.5-w, min1, 0.5-w, 0.5+w, max1, 0.5+w);
		} else if (dir.offsetY < 0) {
			renderer.setRenderBounds(0.5-w, min2, 0.5-w, 0.5+w, max2, 0.5+w);
		} else if (dir.offsetZ > 0) {
			renderer.setRenderBounds(0.5-w, 0.5-w, min1, 0.5+w, 0.5+w, max1);
		} else if (dir.offsetZ < 0) {
			renderer.setRenderBounds(0.5-w, 0.5-w, min2, 0.5+w, 0.5+w, max2);
		}
		renderer.renderStandardBlock(PC_BlockMultiblock.block, xCoord, yCoord, zCoord);
		if(useOverlay() && cDir!=-1){
			int j = 0;
			for (int k = 0; k < 16; k++) {
				if ((getMask() & 1 << k) != 0) {
					Icon icon = getCableLineIcon(j);
					if (icon != null) {
						i = 0;
						for (PC_Direction dir2 : PC_Direction.VALID_DIRECTIONS) {
							if (dir2 == dir || dir2.getOpposite() == dir){
								iconArray[dir2.ordinal()] = icon;
								continue;
							}
							if(c[i]==0){
								iconArray[dir2.ordinal()] = (i/2 == cDir)?icon:null;
							}else{
								iconArray[dir2.ordinal()] = null;
							}
							i++;
						}
						PC_BlockMultiblock.setIcons(iconArray);
						PC_BlockMultiblock.colorMultiplier = getColorForCable(k);
						renderer.renderStandardBlock(PC_BlockMultiblock.block, xCoord, yCoord, zCoord);
					}
					j++;
				}
			}
			PC_BlockMultiblock.colorMultiplier = 0xFFFFFFFF;
		}
		renderer.uvRotateBottom = 0;
		renderer.uvRotateTop = 0;
		renderer.uvRotateEast = 0;
		renderer.uvRotateNorth = 0;
		renderer.uvRotateSouth = 0;
		renderer.uvRotateWest = 0;
	}

	public void renderCable2(double w, double wl, double l, double min, double max, float s, int mask, boolean ending) {

		double minX = min(w, wl, dir2.offsetX, l);
		double minY = min(w, wl, dir2.offsetY, l);
		double minZ = min(w, wl, dir2.offsetZ, l);
		double maxX = max(w, wl, dir2.offsetX, l);
		double maxY = max(w, wl, dir2.offsetY, l);
		double maxZ = max(w, wl, dir2.offsetZ, l);
		if(dir.offsetX!=0){
			renderer.uvRotateBottom = 2;
			renderer.uvRotateTop = 1;
			renderer.uvRotateEast = 1;
			renderer.uvRotateNorth = dir2.offsetY==0?2:0;
			renderer.uvRotateSouth = dir2.offsetY==0?1:3;
			renderer.uvRotateWest = 2;
		}else if(dir.offsetY!=0){
			renderer.uvRotateBottom = dir2.offsetX==0?0:2;
			renderer.uvRotateTop = dir2.offsetX==0?0:1;
			renderer.uvRotateEast = 3;
			renderer.uvRotateNorth = 0;
			renderer.uvRotateSouth = 3;
			renderer.uvRotateWest = 0;
		}else{
			renderer.uvRotateBottom = 0;
			renderer.uvRotateTop = 0;
			renderer.uvRotateEast = dir2.offsetY==0?1:3;
			renderer.uvRotateNorth = 2;
			renderer.uvRotateSouth = 1;
			renderer.uvRotateWest = dir2.offsetY==0?2:0;
		}
		Icon cableIcon = getCableIcon();
		Icon iconArray[] = new Icon[6];
		iconArray[0] = dir2.offsetY==0 || (dir2==PC_Direction.DOWN&&ending)?cableIcon:null;
		iconArray[1] = dir2.offsetY==0 || (dir2==PC_Direction.UP&&ending)?cableIcon:null;
		iconArray[2] = dir2.offsetZ==0 || (dir2==PC_Direction.NORTH&&ending)?cableIcon:null;
		iconArray[3] = dir2.offsetZ==0 || (dir2==PC_Direction.SOUTH&&ending)?cableIcon:null;
		iconArray[4] = dir2.offsetX==0 || (dir2==PC_Direction.WEST&&ending)?cableIcon:null;
		iconArray[5] = dir2.offsetX==0 || (dir2==PC_Direction.EAST&&ending)?cableIcon:null;
		PC_BlockMultiblock.setIcons(iconArray);
		if (dir.offsetX > 0) {
			renderer.setRenderBounds(max - s, minY, minZ, max, maxY, maxZ);
		} else if (dir.offsetX < 0) {
			renderer.setRenderBounds(min, minY, minZ, min + s, maxY, maxZ);
		} else if (dir.offsetY > 0) {
			renderer.setRenderBounds(minX, max - s, minZ, maxX, max, maxZ);
		} else if (dir.offsetY < 0) {
			renderer.setRenderBounds(minX, min, minZ, maxX, min + s, maxZ);
		} else if (dir.offsetZ > 0) {
			renderer.setRenderBounds(minX, minY, max - s, maxX, maxY, max);
		} else if (dir.offsetZ < 0) {
			renderer.setRenderBounds(minX, minY, min, maxX, maxY, min + s);
		}
		renderer.renderStandardBlock(PC_BlockMultiblock.block, xCoord, yCoord, zCoord);
		if(useOverlay()){
			int j = 0;
			for (int i = 0; i < 16; i++) {
				if ((mask & 1 << i) != 0) {
					Icon icon = getCableLineIcon(j);
					if (icon != null) {
						iconArray[0] = dir.offsetY!=0 || (dir2==PC_Direction.DOWN&&ending)?icon:null;
						iconArray[1] = dir.offsetY!=0 || (dir2==PC_Direction.UP&&ending)?icon:null;
						iconArray[2] = dir.offsetZ!=0 || (dir2==PC_Direction.NORTH&&ending)?icon:null;
						iconArray[3] = dir.offsetZ!=0 || (dir2==PC_Direction.SOUTH&&ending)?icon:null;
						iconArray[4] = dir.offsetX!=0 || (dir2==PC_Direction.WEST&&ending)?icon:null;
						iconArray[5] = dir.offsetX!=0 || (dir2==PC_Direction.EAST&&ending)?icon:null;
						PC_BlockMultiblock.setIcons(iconArray);
						PC_BlockMultiblock.colorMultiplier = getColorForCable(i);
						renderer.renderStandardBlock(PC_BlockMultiblock.block, xCoord, yCoord, zCoord);
					}
					j++;
				}
			}
			PC_BlockMultiblock.colorMultiplier = 0xFFFFFFFF;
		}
		renderer.uvRotateBottom = 0;
		renderer.uvRotateTop = 0;
		renderer.uvRotateEast = 0;
		renderer.uvRotateNorth = 0;
		renderer.uvRotateSouth = 0;
		renderer.uvRotateWest = 0;
	}


	public void renderCableToOutside(double w, double wl, double l, double min, double max, int mask) {

		double minX = min(w, wl, dir2.offsetX, l);
		double minY = min(w, wl, dir2.offsetY, l);
		double minZ = min(w, wl, dir2.offsetZ, l);
		double maxX = max(w, wl, dir2.offsetX, l);
		double maxY = max(w, wl, dir2.offsetY, l);
		double maxZ = max(w, wl, dir2.offsetZ, l);
		if(dir2.offsetX!=0){
			renderer.uvRotateBottom = 2;
			renderer.uvRotateTop = 1;
			renderer.uvRotateEast = 1;
			renderer.uvRotateNorth = dir.offsetY==0?2:0;
			renderer.uvRotateSouth = dir.offsetY==0?1:3;
			renderer.uvRotateWest = 2;
		}else if(dir2.offsetY!=0){
			renderer.uvRotateBottom = dir.offsetX==0?0:2;
			renderer.uvRotateTop = dir.offsetX==0?0:1;
			renderer.uvRotateEast = 3;
			renderer.uvRotateNorth = 0;
			renderer.uvRotateSouth = 3;
			renderer.uvRotateWest = 0;
		}else{
			renderer.uvRotateBottom = 0;
			renderer.uvRotateTop = 0;
			renderer.uvRotateEast = dir.offsetY==0?1:3;
			renderer.uvRotateNorth = 2;
			renderer.uvRotateSouth = 1;
			renderer.uvRotateWest = dir.offsetY==0?2:0;
		}
		Icon cableIcon = getCableIcon();
		Icon iconArray[] = new Icon[6];
		PC_BlockMultiblock.setIcons(cableIcon);
		if (dir.offsetX > 0) {
			renderer.setRenderBounds(max1, minY, minZ, max, maxY, maxZ);
		} else if (dir.offsetX < 0) {
			renderer.setRenderBounds(min, minY, minZ, min2, maxY, maxZ);
		} else if (dir.offsetY > 0) {
			renderer.setRenderBounds(minX, max1, minZ, maxX, max, maxZ);
		} else if (dir.offsetY < 0) {
			renderer.setRenderBounds(minX, min, minZ, maxX, min2, maxZ);
		} else if (dir.offsetZ > 0) {
			renderer.setRenderBounds(minX, minY, max1, maxX, maxY, max);
		} else if (dir.offsetZ < 0) {
			renderer.setRenderBounds(minX, minY, min, maxX, maxY, min2);
		}
		renderer.renderStandardBlock(PC_BlockMultiblock.block, xCoord, yCoord, zCoord);
		if(useOverlay()){
			int j = 0;
			for (int i = 0; i < 16; i++) {
				if ((mask & 1 << i) != 0) {
					Icon icon = getCableLineIcon(j);
					if (icon != null) {
						iconArray[0] = dir.offsetY!=0 || dir2.offsetY!=0?icon:null;
						iconArray[1] = dir.offsetY!=0 || dir2.offsetY!=0?icon:null;
						iconArray[2] = dir.offsetZ!=0 || dir2.offsetZ!=0?icon:null;
						iconArray[3] = dir.offsetZ!=0 || dir2.offsetZ!=0?icon:null;
						iconArray[4] = dir.offsetX!=0 || dir2.offsetX!=0?icon:null;
						iconArray[5] = dir.offsetX!=0 || dir2.offsetX!=0?icon:null;
						PC_BlockMultiblock.setIcons(iconArray);
						PC_BlockMultiblock.colorMultiplier = getColorForCable(i);
						renderer.renderStandardBlock(PC_BlockMultiblock.block, xCoord, yCoord, zCoord);
					}
					j++;
				}
			}
			PC_BlockMultiblock.colorMultiplier = 0xFFFFFFFF;
		}
		renderer.uvRotateBottom = 0;
		renderer.uvRotateTop = 0;
		renderer.uvRotateEast = 0;
		renderer.uvRotateNorth = 0;
		renderer.uvRotateSouth = 0;
		renderer.uvRotateWest = 0;
	}


	private static double min(double w, int offset, double l) {

		if (offset < 0) {
			return 0.5 - l;
		} else if (offset > 0) {
			return 0.5 + w;
		}
		return 0.5 - w;
	}


	private static double max(double w, int offset, double l) {

		if (offset < 0) {
			return 0.5 - w;
		} else if (offset > 0) {
			return 0.5 + l;
		}
		return 0.5 + w;
	}


	private static double min(double w, double wl, int offset, double l) {

		if (offset < 0) {
			return 0.5 - l;
		} else if (offset > 0) {
			return 0.5 + wl;
		}
		return 0.5 - w;
	}


	private static double max(double w, double wl, int offset, double l) {

		if (offset < 0) {
			return 0.5 - wl;
		} else if (offset > 0) {
			return 0.5 + l;
		}
		return 0.5 + w;
	}

}
