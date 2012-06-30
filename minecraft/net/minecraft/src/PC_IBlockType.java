package net.minecraft.src;


import java.util.Set;


/**
 * Block type flags for PowerCraft blocks
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public interface PC_IBlockType {

	/**
	 * @param world the world the block is in
	 * @param pos position in world; This can be used, together with world, to
	 *            get metadata or tile entity, and thus make it possible for
	 *            subtypes to have different type settings.
	 * @return set of flags for given location
	 */
	public abstract Set<String> getBlockFlags(World world, PC_CoordI pos);

	/**
	 * Get block item flags
	 * 
	 * @param damage itemstack's damage
	 * @return set of all flags for given damage
	 */
	public abstract Set<String> getItemFlags(int damage);
}
