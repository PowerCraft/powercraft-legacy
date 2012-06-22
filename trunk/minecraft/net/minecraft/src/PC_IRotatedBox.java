package net.minecraft.src;

/**
 * Block rendered as a box with rotated top face. Used for rendering.
 * 
 * @author MightyPork
 */
public interface PC_IRotatedBox {

	/**
	 * Get rotation from the metadata
	 * 
	 * @param meta metadata in the world
	 * @return orientation as 0-3
	 */
	public int getRotation(int meta); // 0,1,2,3

	/**
	 * @return Is the item to be rendered horizontally? Return false for vertical renderer, like gates
	 */
	public boolean renderItemHorizontal();
}
