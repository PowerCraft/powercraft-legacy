package net.minecraft.src;


/**
 * PowerCraft deco module renderer (iron frame + subtypes)
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCde_Renderer {

	/** Iron frame block renderer */
	public static int decorativeBlockRenderer = 0;
	/** Walk-through block renderer */
	public static int walkableBlockRenderer = 0;

	/**
	 * Render block by render type.<br>
	 * Works for swapTerrainRenderer and rotatedBoxRenderer
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
		if (rtype == decorativeBlockRenderer) {
			return renderBlockDecorative(renderblocks, blockAccess, i, j, k, block);
		}
		return false;
	}

	/**
	 * Render Deco block (must implement PC_ISwapTerrain)
	 * 
	 * @param renderblocks block renderer
	 * @param iblockaccess block access
	 * @param i x
	 * @param j y
	 * @param k z
	 * @param block the block
	 * @return success
	 */
	public static boolean renderBlockDecorative(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block) {

		TileEntity te = iblockaccess.getBlockTileEntity(i, j, k);
		if (te == null) {
			return false;
		}
		PCde_TileEntityDeco ted = (PCde_TileEntityDeco) te;

		if (ted.type == 1) {
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

			PC_Renderer.renderBlockSwapTerrain(renderblocks, iblockaccess, i, j, k, block);

			return true;
		}

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
		if (rtype == decorativeBlockRenderer) {
			renderInvBlockDeco(renderblocks, block, meta);
		}
		if (rtype == walkableBlockRenderer) {
			renderInvBlockDecoNonSolid(renderblocks, block, meta);
		}
		return;
	}

	/**
	 * Render Deco block on inventory
	 * 
	 * @param renderblocks block renderer
	 * @param block the block
	 * @param meta metadata
	 */
	public static void renderInvBlockDeco(RenderBlocks renderblocks, Block block, int meta) {

		if (meta == 0) {

			boolean swapped = PC_Renderer.swapTerrain(block);

			float s = 0.1875F;

			// pillars
			block.setBlockBounds(0, 0, 0, s, 1, s);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 21);
			block.setBlockBounds(1 - s, 0, 0, 1, 1, s);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 21);
			block.setBlockBounds(0, 0, 1 - s, s, 1, 1);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 21);
			block.setBlockBounds(1 - s, 0, 1 - s, 1, 1, 1);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 21);

			// x-sticks
			block.setBlockBounds(s, 0, 0, 1 - s, s, s);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 21);
			block.setBlockBounds(s, 0, 1 - s, 1 - s, s, 1);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 21);
			block.setBlockBounds(s, 1 - s, 0, 1 - s, 1, s);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 21);
			block.setBlockBounds(s, 1 - s, 1 - s, 1 - s, 1, 1);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 21);

			// z-sticks
			block.setBlockBounds(0, 0, s, s, s, 1 - s);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 21);
			block.setBlockBounds(0, 1 - s, s, s, 1, 1 - s);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 21);

			block.setBlockBounds(1 - s, 0, s, 1, s, 1 - s);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 21);
			block.setBlockBounds(1 - s, 1 - s, s, 1, 1, 1 - s);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 21);

			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

			PC_Renderer.resetTerrain(swapped);

			return;
		} else if (meta == 1) {
			block.setBlockBounds(0, 0, 0, 1, 1, 1);
			PC_Renderer.renderInvBlock(renderblocks, block, meta);
		} else if (meta == 2) {
			float p = 0.0625F;
			boolean swapped = PC_Renderer.swapTerrain(block);
			block.setBlockBounds(0, 0, 0, 1, p, 1);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 22);
			block.setBlockBounds(0, 0, 1 - p, 1, 1, 1);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 20);
			block.setBlockBounds(0, 0, 0, p, 1, 1);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 20);
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			PC_Renderer.resetTerrain(swapped);
		}

	}

	/**
	 * Render Deco block on inventory
	 * 
	 * @param renderblocks block renderer
	 * @param block the block
	 * @param meta metadata
	 */
	public static void renderInvBlockDecoNonSolid(RenderBlocks renderblocks, Block block, int meta) {

		if (meta == 0) {
			float p = 0.0625F;
			boolean swapped = PC_Renderer.swapTerrain(block);
			block.setBlockBounds(0, 0, 0, 1, p, 1);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 22);
			block.setBlockBounds(0, 0, 1 - p, 1, 1, 1);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 20);
			block.setBlockBounds(0, 0, 0, p, 1, 1);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 20);
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			PC_Renderer.resetTerrain(swapped);
		} else if (meta == 1) {
			float p = 0.0625F;
			boolean swapped = PC_Renderer.swapTerrain(block);

			block.setBlockBounds(0, 0F, 0.5F, 1, p, 1);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 22);

			block.setBlockBounds(0, 0.5F - p, 0, 1, 0.5F, 0.5F);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 22);

			block.setBlockBounds(0, 0, 0.5F, p, 0.5F, 1);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 20);
			block.setBlockBounds(0, 0.5F, 0, p, 0.5F + 0.5F, 0.5F);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 20);

			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			PC_Renderer.resetTerrain(swapped);
		}

	}

}
