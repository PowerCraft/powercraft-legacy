package net.minecraft.src;


/**
 * Interface for non-XML crop blocks. it lets you do the harvesting and
 * replanting the way you want, even using tile entity.
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public interface PC_ICropBlock {

	/**
	 * Is the block ready for harvesting?
	 * 
	 * @param world the world
	 * @param pos coordinate in world
	 * @return is mature, can be harvested
	 */
	public abstract boolean isMature(World world, PC_CoordI pos);

	/**
	 * Do machine harvesting.<br>
	 * This should not be called until you return true from isMature.<br>
	 * Breaking particles are created by the machine, you only create array of
	 * stacks to dispense here, and replant the block (back to seeds state). <br>
	 * <b>No nulls allowed! return empty array if you need, but never null.</b>
	 * 
	 * @param world the world
	 * @param pos coordinate in world
	 * @return stacks harvested; you must also REPLANT the block!
	 */
	public ItemStack[] machineHarvest(World world, PC_CoordI pos);

}
