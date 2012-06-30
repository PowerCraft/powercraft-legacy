package net.minecraft.src;


/**
 * @author MightyPork
 * @copy (c) 2012
 */
public abstract class PC_TileEntity extends TileEntity {

	/**
	 * @return tile entity coordinate
	 */
	public PC_CoordI getCoord() {
		return new PC_CoordI(xCoord, yCoord, zCoord);
	}

	/**
	 * Forge method - can update?
	 * 
	 * @return can update
	 */
	public abstract boolean canUpdate();

}
