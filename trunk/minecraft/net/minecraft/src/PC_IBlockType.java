package net.minecraft.src;

/**
 * Block type flags for PowerCraft blocks
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public interface PC_IBlockType {

	/**
	 * @param world the world the block is in
	 * @param pos position in world; This can be used, together with world, to get metadata or tile entity, and thus make it possible for
	 *            subtypes to have different type settings.
	 * @return true if Laser beam goes through.<br>
	 *         This is the last thing checked by laser.
	 */
	public abstract boolean isTranslucentForLaser(IBlockAccess world, PC_CoordI pos);

	/**
	 * @param world the world the block is in
	 * @param pos position in world; This can be used, together with world, to get metadata or tile entity, and thus make it possible for
	 *            subtypes to have different type settings.
	 * @return true if Harvester skips this block.
	 */
	public abstract boolean isHarvesterIgnored(IBlockAccess world, PC_CoordI pos);

	/**
	 * @param world the world the block is in
	 * @param pos position in world; This can be used, together with world, to get metadata or tile entity, and thus make it possible for
	 *            subtypes to have different type settings.
	 * @return true if Harvester stops at this block
	 */
	public abstract boolean isHarvesterDelimiter(IBlockAccess world, PC_CoordI pos);

	/**
	 * @return true if Builder ignores this block (item, thus no position is provided)
	 */
	public abstract boolean isBuilderIgnored();

	/**
	 * @param world the world the block is in
	 * @param pos position in world; This can be used, together with world, to get metadata or tile entity, and thus make it possible for
	 *            subtypes to have different type settings.
	 * @return true if it's conveyor-like (flat with items movement)
	 */
	public abstract boolean isConveyor(IBlockAccess world, PC_CoordI pos);

	/**
	 * @param world the world the block is in
	 * @param pos position in world; This can be used, together with world, to get metadata or tile entity, and thus make it possible for
	 *            subtypes to have different type settings.
	 * @return true if it's an elevator
	 */
	public abstract boolean isElevator(IBlockAccess world, PC_CoordI pos);

}
