package net.minecraft.src;


/**
 * PowerCraft core module renderer (power crystals)
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_Renderer {

	/**
	 * Laser block renderer (the inventory item, terrain block is rendered by
	 * TileEntitySpecialRenderer)
	 */
	public static int laserRenderer = 0;
	/** XP bank block renderer */
	public static int xpbankRenderer = 0;
	/** Mirror/Prism block renderer (only for items!) */
	public static int opticalRenderer = 0;

	/**
	 * Render block by render type.<br>
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
		if (rtype == laserRenderer) {
			return true; /* Laser is not rendered as block! */
		}
		if (rtype == xpbankRenderer) {
			return renderBlockXPBank(renderblocks, blockAccess, i, j, k, block);
		}
		return false;
	}


	/**
	 * Render XP bank block
	 * 
	 * @param renderblocks block renderer
	 * @param iblockaccess block access
	 * @param i x
	 * @param j y
	 * @param k z
	 * @param block the block
	 * @return success
	 */
	public static boolean renderBlockXPBank(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block) {
		boolean gf = RenderBlocks.cfgGrassFix;
		RenderBlocks.cfgGrassFix = false;

		// XP bank inner thingy
		mod_PCmachines.xpbank.rendering = true;
		PC_Renderer.renderBlockSwapTerrain(renderblocks, iblockaccess, i, j, k, block);
		mod_PCmachines.xpbank.rendering = false;
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		// obsidian body
		Block.obsidian.setBlockBounds(0.0F, 0.3F, 0.0F, 1.0F, 0.7F, 1.0F);
		renderblocks.renderStandardBlock(Block.obsidian, i, j, k);

		// legs
		Block.obsidian.setBlockBounds(0.0F, 0.0F, 0.0F, 0.15F, 0.3F, 0.15F);
		renderblocks.renderStandardBlock(Block.obsidian, i, j, k);
		Block.obsidian.setBlockBounds(0.85F, 0.0F, 0.0F, 1.0F, 0.3F, 0.15F);
		renderblocks.renderStandardBlock(Block.obsidian, i, j, k);
		Block.obsidian.setBlockBounds(0.0F, 0.0F, 0.85F, 0.15F, 0.3F, 1.0F);
		renderblocks.renderStandardBlock(Block.obsidian, i, j, k);
		Block.obsidian.setBlockBounds(0.85F, 0.0F, 0.85F, 1.0F, 0.3F, 1.0F);
		renderblocks.renderStandardBlock(Block.obsidian, i, j, k);

		// reset
		Block.obsidian.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		RenderBlocks.cfgGrassFix = gf;

		return true;
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
		if (rtype == laserRenderer) {
			renderInvBlockLaser(renderblocks, block, meta);
		}

		if (rtype == xpbankRenderer) {
			renderInvBlockXPBank(renderblocks, block, meta);
		}

		if (rtype == opticalRenderer) {
			renderInvBlockOptical(renderblocks, block, meta);
		}
		return;
	}

	/**
	 * Render mirror/prism on ivnentory
	 * 
	 * @param renderblocks block renderer
	 * @param block the block
	 * @param meta metadata
	 */
	public static void renderInvBlockOptical(RenderBlocks renderblocks, Block block, int meta) {

		if (meta == 0) {
			Block steel = Block.blockSteel;
			float px = 0.0625F;
			steel.setBlockBounds(0 * px, 6 * px, 7 * px, 15 * px, 15 * px, 9 * px);
			PC_Renderer.renderInvBox(renderblocks, steel, 0);
			steel.setBlockBounds(3 * px, 0 * px, 7 * px, 5 * px, 6 * px, 9 * px);
			PC_Renderer.renderInvBox(renderblocks, steel, 0);
			steel.setBlockBounds(10 * px, 0 * px, 7 * px, 12 * px, 6 * px, 9 * px);
			PC_Renderer.renderInvBox(renderblocks, steel, 0);
			steel.setBlockBounds(0, 0, 0, 1, 1, 1);
		} else {
			Block ice = Block.ice;
			float px = 0.0625F;
			ice.setBlockBounds(3 * px, 3 * px, 3 * px, 12 * px, 12 * px, 12 * px);
			PC_Renderer.renderInvBox(renderblocks, ice, 0);
			ice.setBlockBounds(4 * px, 4 * px, 2 * px, 11 * px, 11 * px, 13 * px);
			PC_Renderer.renderInvBox(renderblocks, ice, 0);
			ice.setBlockBounds(2 * px, 4 * px, 4 * px, 13 * px, 11 * px, 11 * px);
			PC_Renderer.renderInvBox(renderblocks, ice, 0);
			ice.setBlockBounds(4 * px, 2 * px, 4 * px, 11 * px, 13 * px, 11 * px);
			PC_Renderer.renderInvBox(renderblocks, ice, 0);
			ice.setBlockBounds(0, 0, 0, 1, 1, 1);
		}

	}

	/**
	 * Render laser on inventory
	 * 
	 * @param renderblocks block renderer
	 * @param block the block
	 * @param meta metadata
	 */
	public static void renderInvBlockLaser(RenderBlocks renderblocks, Block block, int meta) {
		PC_Renderer.renderInvBlock(renderblocks, block, meta);
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		// cobble body
		Block.cobblestone.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
		PC_Renderer.renderInvBox(renderblocks, Block.cobblestone, 0);
		Block.cobblestone.setBlockBounds(0.4F, 0.2F, 0.4F, 0.6F, 0.3F, 0.6F);
		PC_Renderer.renderInvBox(renderblocks, Block.cobblestone, 0);
		// reset
		Block.cobblestone.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Render XP Bank on inventory
	 * 
	 * @param renderblocks block renderer
	 * @param block the block
	 * @param meta metadata
	 */
	public static void renderInvBlockXPBank(RenderBlocks renderblocks, Block block, int meta) {
		PC_Renderer.renderInvBlock(renderblocks, block, meta);
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		// obsidian body
		Block.obsidian.setBlockBounds(0.0F, 0.3F, 0.0F, 1.0F, 0.8F, 1.0F);
		PC_Renderer.renderInvBox(renderblocks, Block.obsidian, 0);

		// legs
		Block.obsidian.setBlockBounds(0.0F, 0.0F, 0.0F, 0.2F, 0.3F, 0.2F);
		PC_Renderer.renderInvBox(renderblocks, Block.obsidian, 0);
		Block.obsidian.setBlockBounds(0.8F, 0.0F, 0.0F, 1.0F, 0.3F, 0.2F);
		PC_Renderer.renderInvBox(renderblocks, Block.obsidian, 0);
		Block.obsidian.setBlockBounds(0.0F, 0.0F, 0.8F, 0.2F, 0.3F, 1.0F);
		PC_Renderer.renderInvBox(renderblocks, Block.obsidian, 0);
		Block.obsidian.setBlockBounds(0.8F, 0.0F, 0.8F, 1.0F, 0.3F, 1.0F);
		PC_Renderer.renderInvBox(renderblocks, Block.obsidian, 0);

		// reset
		Block.obsidian.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

}
