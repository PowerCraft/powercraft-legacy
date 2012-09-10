package net.minecraft.src;



/**
 * PowerCraft core module renderer (power crystals)
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCtr_Renderer {

	/**
	 * Render block by render type.
	 * 
	 * @param renderblocks block renderer
	 * @param blockAccess block access
	 * @param i x
	 * @param j y
	 * @param k z
	 * @param block the block
	 * @param rtype render type
	 * @return success
	 */
	public static boolean renderBlockByType(RenderBlocks renderblocks, IBlockAccess blockAccess, int i, int j, int k, Block block, int rtype) {

		return false;
	}

	/**
	 * Render inv block using given render type.
	 * 
	 * @param renderblocks block renderer
	 * @param block the block
	 * @param meta metadata
	 * @param rtype render type
	 */
	public static void renderInvBlockByType(RenderBlocks renderblocks, Block block, int meta, int rtype) {

		double[] a = {block.minX,block.minY,block.minZ,block.maxX,block.maxY,block.maxZ};

		block.setBlockBounds((float)a[0],(float)a[1],(float)a[2],(float)a[3],(float)a[4],(float)a[5]);
		return;
	}
}
