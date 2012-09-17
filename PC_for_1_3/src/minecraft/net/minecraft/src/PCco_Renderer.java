package net.minecraft.src;


import java.util.Random;


/**
 * PowerCraft core module renderer (power crystals)
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCco_Renderer {

	/** Power crystal renderer */
	public static int crystalRenderer = 0;

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
		if (rtype == crystalRenderer) {
			return renderBlockCrystal(renderblocks, blockAccess, i, j, k, block);
		}
		return false;
	}

	/**
	 * Render PowerCrystal block (must implement PC_ISwapTerrain)
	 * 
	 * @param renderblocks block renderer
	 * @param iblockaccess block access
	 * @param i x
	 * @param j y
	 * @param k z
	 * @param block the block
	 * @return success
	 */
	public static boolean renderBlockCrystal(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block) {
		RenderEngine renderengine = ModLoader.getMinecraftInstance().renderEngine;
		Tessellator tessellator = Tessellator.instance;
		if (block instanceof PC_ISwapTerrain) {
			tessellator.draw();
			tessellator.startDrawingQuads();
			renderengine.bindTexture(renderengine.getTexture(((PC_ISwapTerrain) block).getTerrainFile()));

			Random posRand = new Random(i + i * j * k + k + iblockaccess.getBlockMetadata(i, j, k));

			for (int q = 3 + posRand.nextInt(2); q > 0; q--) {
				float x, y, z, a, b, c;

				x = posRand.nextFloat() * 0.6F;
				y = (q == 2 ? 0.001F : posRand.nextFloat() * 0.6F);
				z = posRand.nextFloat() * 0.6F;

				a = x + 0.3F + posRand.nextFloat() * (0.7F - x);
				b = y + 0.3F + posRand.nextFloat() * (0.7F - y);
				c = z + 0.3F + posRand.nextFloat() * (0.7F - z);

				block.setBlockBounds(x, y, z, a, b, c);
				renderblocks.renderStandardBlock(block, i, j, k);
			}
			block.setBlockBounds(0, 0, 0, 1, 1, 1);
			
			tessellator.draw();
			tessellator.startDrawingQuads();
			renderengine.bindTexture(renderengine.getTexture("/terrain.png"));

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
		if (rtype == crystalRenderer) {
			renderInvBlockCrystal(renderblocks, block, meta);
		}
		return;
	}

	/**
	 * Render crystal on inventory
	 * 
	 * @param renderblocks block renderer
	 * @param block the block
	 * @param meta metadata
	 */
	public static void renderInvBlockCrystal(RenderBlocks renderblocks, Block block, int meta) {
		RenderEngine renderengine = ModLoader.getMinecraftInstance().renderEngine;
		if (block instanceof PC_ISwapTerrain) {
			renderengine.bindTexture(renderengine.getTexture(((PC_ISwapTerrain) block).getTerrainFile()));

			Random posRand = new Random(meta);

			for (int q = 3 + posRand.nextInt(3); q > 0; q--) {
				float x, y, z, a, b, c;
				x = 0.0F + posRand.nextFloat() * 0.6F;
				y = 0.0F + posRand.nextFloat() * 0.6F;
				z = 0.0F + posRand.nextFloat() * 0.6F;

				a = 0.2F + Math.max(posRand.nextFloat() * (0.7F - x), 0.3F);
				b = 0.2F + Math.max(posRand.nextFloat() * (0.7F - y), 0.3F);
				c = 0.2F + Math.max(posRand.nextFloat() * (0.7F - z), 0.3F);

				block.setBlockBounds(x, y, z, x + a, y + b, z + c);
				PC_Renderer.renderInvBox(renderblocks, block, meta);
			}
			block.setBlockBounds(0, 0, 0, 1, 1, 1);
			renderengine.bindTexture(renderengine.getTexture("/terrain.png"));
		}

	}

}
