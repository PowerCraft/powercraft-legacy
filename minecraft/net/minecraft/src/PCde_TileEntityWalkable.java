package net.minecraft.src;

/**
 * Decorative block tile entity - because of the renderer.
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCde_TileEntityWalkable extends PC_TileEntity {

	/** block type. */
	public int type = 0;


	@Override
	public void updateEntity() {
		// nothing
	}

	/**
	 * forge method - receives update ticks
	 * 
	 * @return false
	 */
	public boolean canUpdate() {
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		type = tag.getInteger("type");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("type", type);

	}
}
