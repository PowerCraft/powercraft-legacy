package net.minecraft.src;

/**
 * 
 * 
 * @author MightyPork
 * @copy (c) 2012
 *
 */
public class PC_TileEntity extends TileEntity {

	/**
	 * @return tile entity coordinate
	 */
	public PC_CoordI getCoord(){
		return new PC_CoordI(xCoord,yCoord,zCoord);
	}

}
