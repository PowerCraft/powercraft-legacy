package net.minecraft.src;


/**
 * Block with special textures for item renderer
 * 
 * @author MightyPork
 */
public interface PC_ISpecialInventoryTextures {

	/**
	 * get inventory block texture for side and metadata
	 * 
	 * @param side the side rendered
	 * @param meta block metadata
	 * @return texture index
	 */
	public abstract int getInvTexture(int side, int meta);
}
