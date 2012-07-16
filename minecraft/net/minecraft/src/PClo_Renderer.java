package net.minecraft.src;


/**
 * PowerCraft logic module renderer (power crystals) [template file, no function
 * yet]
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PClo_Renderer {

	/** Radio block renderer (item only) */
	public static int radioRenderer;
	/** Sensor block renderer (item only) */
	public static int sensorRenderer;
	/** Renderer for weasel devices (items only) */
	public static int weaselRenderer;

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

		if (rtype == radioRenderer) {
			renderInvBlockRadio(renderblocks, block, meta);
			return;
		}

		if (rtype == sensorRenderer) {
			renderInvBlockSensor(renderblocks, (PClo_BlockSensor) block, meta);
			return;
		}

		if (rtype == weaselRenderer) {
			renderInvBlockWeasel(renderblocks, (PClo_BlockWeasel) block, meta);
			return;
		}

	}

	private static void renderInvBlockRadio(RenderBlocks renderblocks, Block block, int meta) {

		PC_Renderer.swapTerrain(mod_PClogic.getTerrainFile());

		int tx = meta == 0 ? 69 : 70;

		float px = 0.0625F;


		block.setBlockBounds(0, 0, 0, 16 * px, 4 * px, 16 * px);
		PC_Renderer.renderInvBox(renderblocks, block, 0);
		block.setBlockBounds(6.5F * px, 4F * px, 6.5F * px, 9.5F * px, 7F * px, 9.5F * px);
		PC_Renderer.renderInvBoxWithTexture(renderblocks, block, tx);
		block.setBlockBounds(7F * px, 7F * px, 7F * px, 9F * px, 10F * px, 9F * px);
		PC_Renderer.renderInvBoxWithTexture(renderblocks, block, tx);
		block.setBlockBounds(7.5F * px, 10F * px, 7.5F * px, 8.5F * px, 13F * px, 8.5F * px);
		PC_Renderer.renderInvBoxWithTexture(renderblocks, block, tx);
		block.setBlockBounds(7F * px, 12F * px, 7F * px, 9F * px, 14F * px, 9F * px);
		PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 68);

		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.255F, 1.0F);

		PC_Renderer.resetTerrain(true);

	}

	private static void renderInvBlockSensor(RenderBlocks renderblocks, PClo_BlockSensor block, int meta) {

		PC_Renderer.swapTerrain(mod_PClogic.getTerrainFile());

		float px = 0.0625F;

		int tx = meta == 0 ? 65 : meta == 1 ? 6 : 64;

		block.setBlockBounds(0, 0, 0, 16 * px, 4 * px, 16 * px);
		PC_Renderer.renderInvBoxWithTexture(renderblocks, block, tx);
		block.setBlockBounds(6 * px, 4 * px, 6 * px, 10 * px, 9 * px, 10 * px);
		PC_Renderer.renderInvBoxWithTexture(renderblocks, block, tx);
		block.setBlockBounds(5 * px, 8 * px, 5 * px, 11 * px, 14 * px, 11 * px);
		PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 68);
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		PC_Renderer.resetTerrain(true);

	}

	private static void renderInvBlockWeasel(RenderBlocks renderblocks, PClo_BlockWeasel block, int meta) {

		PC_Renderer.swapTerrain(mod_PClogic.getTerrainFile());

		float px = 0.0625F;

		switch (meta) {
			case PClo_WeaselType.CORE:
				//floor
				block.setBlockBounds(0, 0, 0, 16 * px, 3 * px, 16 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 6, 224, 5, 5, 5, 5 });

				//chip
				block.setBlockBounds(4 * px, 3 * px, 3 * px, 12 * px, 5 * px, 13 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 0, 196, 195, 195, 195, 195 });
				block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;

			case PClo_WeaselType.PORT:
				//floor piece
				block.setBlockBounds(0, 0, 0, 16 * px, 3 * px, 16 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 6, 208, 5, 5, 5, 5 });

				//chip
				block.setBlockBounds(5 * px, 3 * px, 5 * px, 11 * px, 5 * px, 11 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 0, 194, 193, 193, 193, 193 });
				block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;

			case PClo_WeaselType.DISPLAY:
				// floor
				block.setBlockBounds(3 * px, 0, 3 * px, 13 * px, 1 * px, 13 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 192, 192, 176, 176, 176, 176 });

				// leg
				block.setBlockBounds(7.2F * px, 1 * px, 7.2F * px, 8.8F * px, 2 * px, 8.8F * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 176, 176, 176, 176, 176, 176 });

				// screen
				block.setBlockBounds(0 * px, 2 * px, 7 * px, 16 * px, 16 * px, 9 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 176, 176, 178, 177, 176, 176 });

				block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;

			case PClo_WeaselType.SPEAKER:
				// floor
				block.setBlockBounds(0 * px, 0, 0 * px, 16 * px, 3 * px, 16 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 6, 227, 5, 5, 5, 5 });

				// box
				block.setBlockBounds(2F * px, 1 * px, 2F * px, 14F * px, 15 * px, 14F * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 179, 179, 180, 180, 180, 180 });

				block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
		}

		PC_Renderer.resetTerrain(true);

	}
}
