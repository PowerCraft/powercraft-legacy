package net.minecraft.src;


/**
 * Common methods for coordinate classes;<br>
 * This interface is mainly used to require overrides, coords should not be
 * casted to it.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public interface PC_ICoord extends PC_INBT {

	/**
	 * @return copy of this, with coordinates inverted
	 */
	public abstract PC_ICoord getInverted();



	/**
	 * Get copy moved by offset coord
	 * 
	 * @param added added coord
	 * @return offset copy
	 */
	public abstract PC_ICoord offset(PC_CoordI added);

	/**
	 * Get copy moved by offset coord
	 * 
	 * @param added added coord
	 * @return offset copy
	 */
	public abstract PC_ICoord offset(PC_CoordD added);

	/**
	 * Get copy moved by offset coord
	 * 
	 * @param added added coord
	 * @return offset copy
	 */
	public abstract PC_ICoord offset(PC_CoordF added);

	/**
	 * Get copy moved by offset coord
	 * 
	 * @param x x coord
	 * @param y y coord
	 * @param z z coord
	 * @return offset copy
	 */
	public abstract PC_ICoord offset(int x, int y, int z);

	/**
	 * Get copy moved by offset coord
	 * 
	 * @param x x coord
	 * @param y y coord
	 * @param z z coord
	 * @return offset copy
	 */
	public abstract PC_ICoord offset(long x, long y, long z);

	/**
	 * Get copy moved by offset coord
	 * 
	 * @param x x coord
	 * @param y y coord
	 * @param z z coord
	 * @return offset copy
	 */
	public abstract PC_ICoord offset(double x, double y, double z);

	/**
	 * Get copy moved by offset coord
	 * 
	 * @param x x coord
	 * @param y y coord
	 * @param z z coord
	 * @return offset copy
	 */
	public abstract PC_ICoord offset(float x, float y, float z);



	/**
	 * Round to integer coordinate.
	 * 
	 * @return rounded
	 */
	public abstract PC_CoordI round();

	/**
	 * Round down (floor) to integer coordinate.
	 * 
	 * @return rounded
	 */
	public abstract PC_CoordI floor();

	/**
	 * Round up (ceil) to integer coordinate.
	 * 
	 * @return rounded
	 */
	public abstract PC_CoordI ceil();

}
