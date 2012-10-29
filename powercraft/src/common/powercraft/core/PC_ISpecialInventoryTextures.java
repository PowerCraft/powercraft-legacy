package powercraft.core;

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
