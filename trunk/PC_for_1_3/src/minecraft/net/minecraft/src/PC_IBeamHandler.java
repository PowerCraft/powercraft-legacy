package net.minecraft.src;


/**
 * Beam tracer's event listener
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public interface PC_IBeamHandler {

	/**
	 * Block was hit.
	 * 
	 * @param world
	 * @param coord coordinate of the block hit
	 * @param startCoord coordinate of the starting device
	 * @return true if the beam should stop here.
	 */
	public abstract boolean onBlockHit(World world, PC_CoordI coord, PC_CoordI startCoord);

	/**
	 * Entities were hit.
	 * 
	 * @param world
	 * @param array array of entities hit in this block
	 * @param startCoord coordinate of the starting device
	 * @return true if the beam should stop here.
	 */
	public abstract boolean onEntityHit(World world, Entity[] array, PC_CoordI startCoord);

}
