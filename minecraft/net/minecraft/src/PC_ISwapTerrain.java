package net.minecraft.src;


/**
 * Block which uses different file instead of terrain.png
 * 
 * @author MightyPork
 */
public interface PC_ISwapTerrain {
	/**
	 * @return path to the terrain file
	 */
	public abstract String getTerrainFile();
}
