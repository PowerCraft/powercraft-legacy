package net.minecraft.src;

/**
 * Decorative block tile entity - because of the renderer.
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCde_TileEntityDeco extends PC_TileEntity {

	/** block type. */
	public int type = 0;

	/** Flag for migrating from 3.4pre3 to newer. */
	private boolean flag34pre4 = true;


	@Override
	public void updateEntity() {

		if (!flag34pre4 && type == 2) {

			// if block was made in 3.4pre3, then type=2 should now be BlockNonSolid, type 0

			Block block = mod_PCdeco.walkable;
			if (worldObj.setBlock(xCoord, yCoord, zCoord, block.blockID)) {
				// set tile entity
				PCde_TileEntityWalkable ted = (PCde_TileEntityWalkable) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);
				if (ted == null) {
					ted = (PCde_TileEntityWalkable) ((BlockContainer) block).getBlockEntity();
				}
				ted.type = 0;
				worldObj.setBlockTileEntity(xCoord, yCoord, zCoord, ted);
				block.onBlockPlaced(worldObj, xCoord, yCoord, zCoord, 0);

				worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
				worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);

				PC_Logger.finest("Old Iron Ledge block updated to use now format at [" + xCoord + ";" + yCoord + ";" + zCoord + "]");
			}
		}

		flag34pre4 = true;

	}

	/**
	 * forge method - receives update ticks
	 * 
	 * @return false
	 */
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		type = tag.getInteger("type");
		flag34pre4 = tag.getBoolean("flag34pre4");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("type", type);
		tag.setBoolean("flag34pre4", flag34pre4);

	}
}
