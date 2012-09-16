package net.minecraft.src;

import net.minecraft.src.*;


public class PCnt_Renderer {

	/** Teleporter block renderer */
	public static int teleporterRenderer = 0;

	/** Renderer for weasel devices (items only) */
	public static int weaselRenderer;
	
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
		if (rtype == teleporterRenderer) {
			return renderBlockTeleporter(renderblocks, blockAccess, i, j, k, block);
		}
	
		if (rtype == weaselRenderer) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Render Teleporter block
	 * 
	 * @param renderblocks block renderer
	 * @param iblockaccess block access
	 * @param i x
	 * @param j y
	 * @param k z
	 * @param block the block
	 * @return success
	 */
	public static boolean renderBlockTeleporter(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block) {
		Tessellator tessellator = Tessellator.instance;

		tessellator.draw();

		tessellator.startDrawingQuads();

		boolean gf = RenderBlocks.cfgGrassFix;
		RenderBlocks.cfgGrassFix = false;
		//PCtr_TileEntityTeleporter tet = PCtr_BlockTeleporter.getTE(iblockaccess, i, j, k);
		PCnt_TeleporterData td = null;
		if(iblockaccess instanceof World)
			td = PCnt_TeleporterManager.getTeleporterDataAt(((World)iblockaccess).worldInfo.getDimension(), i, j, k);
		//if (tet.isReceiver()) {
			Block.blockGold.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F);
			renderblocks.renderStandardBlock(Block.blockGold, i, j, k);
			float m = 0.0625F * 6F;
			float n = 0.0625F * 10F;
			if(td!=null){
				if (td.direction.equals("N")) {
					Block.blockGold.setBlockBounds(m, 0, 0.0625F, n, 0.125F, 0.0625F * 2);
				} else if (td.direction.equals("S")) {
					Block.blockGold.setBlockBounds(m, 0, 1 - 0.0625F * 2, n, 0.125F, 1 - 0.0625F);
				} else if (td.direction.equals("E")) {
					Block.blockGold.setBlockBounds(1 - 0.0625F * 2, 0, m, 1 - 0.0625F, 0.125F, n);
				} else if (td.direction.equals("W")) {
					Block.blockGold.setBlockBounds(0.0625F, 0, m, 0.0625F * 2, 0.125F, n);
				}
			}
			renderblocks.renderStandardBlock(Block.blockGold, i, j, k);
		/*} else {
			Block.blockSteel.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F);
			renderblocks.renderStandardBlock(Block.blockSteel, i, j, k);
		}*/

		Block.blockSteel.setBlockBounds(0.4375F, 0.125F, 0.4375F, 1F - 0.4375F, 0.25F, 1F - 0.4375F);
		renderblocks.renderStandardBlock(Block.blockSteel, i, j, k);

		float centr = 0.0625F * 4;
		Block.blockSteel.setBlockBounds(0.5F - centr, 0.5F - centr, 0.5F - centr, 0.5F + centr, 0.5F + centr, 0.5F + centr);
		renderblocks.renderStandardBlock(Block.blockSteel, i, j, k);

		Block.blockSteel.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		Block.blockGold.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		if(true) {
			block.setBlockBounds(0.1875F, 0.1875F, 0.1875F, 1.0F - 0.1875F, 1.0F - 0.1875F, 1.0F - 0.1875F);
			renderblocks.renderStandardBlock(block, i, j, k);
			block.setBlockBounds(0.125F, 0.0F, 0.125F, 1.0F - 0.125F, 1.0F - 0.125F, 1.0F - 0.125F);
		}

		RenderBlocks.cfgGrassFix = gf;

		tessellator.draw();

		tessellator.startDrawingQuads();

		return true;
	}
	
	public static void renderInvBlockByType(RenderBlocks renderblocks, Block block, int meta, int rtype) {

		double[] a = {block.minX,block.minY,block.minZ,block.maxX,block.maxY,block.maxZ};
		if (rtype == teleporterRenderer) {
			renderInvBlockTeleporter(renderblocks, block, meta);
		}

		if (rtype == weaselRenderer) {
			renderInvBlockWeasel(renderblocks, (PCnt_BlockWeasel) block, meta);
		}
		
		block.setBlockBounds((float)a[0],(float)a[1],(float)a[2],(float)a[3],(float)a[4],(float)a[5]);
		return;
	}
	
	/**
	 * Render teleporter on inventory
	 * 
	 * @param renderblocks block renderer
	 * @param block the block
	 * @param meta metadata
	 */
	public static void renderInvBlockTeleporter(RenderBlocks renderblocks, Block block, int meta) {
		Block.blockSteel.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.125F, 0.875F);
		PC_Renderer.renderInvBox(renderblocks, Block.blockSteel, 0);

		Block.blockSteel.setBlockBounds(0.4375F, 0.125F, 0.4375F, 1F - 0.4375F, 0.25F, 1F - 0.4375F);
		PC_Renderer.renderInvBox(renderblocks, Block.blockSteel, 0);

		float centr = 0.0625F * 4;
		Block.blockSteel.setBlockBounds(0.5F - centr, 0.5F - centr, 0.5F - centr, 0.5F + centr, 0.5F + centr, 0.5F + centr);
		PC_Renderer.renderInvBox(renderblocks, Block.blockSteel, 0);

		Block.blockSteel.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		block.setBlockBounds(0.1875F, 0.0F, 0.1875F, 1.0F - 0.1875F, 1.0F - 0.1875F, 1.0F - 0.1875F);
		PC_Renderer.renderInvBox(renderblocks, block, 0);
		block.setBlockBounds(0.125F, 0.0F, 0.125F, 1.0F - 0.125F, 1.0F - 0.125F, 1.0F - 0.125F);
	}
	
	private static void renderInvBlockWeasel(RenderBlocks renderblocks, PCnt_BlockWeasel block, int meta) {

		PC_Renderer.swapTerrain(mod_PClogic.getTerrainFile());

		float px = 0.0625F;

		switch (meta) {			
			case PCnt_WeaselType.CORE:
			case PCnt_WeaselType.SLAVE:	
				//floor
				block.setBlockBounds(0, 0, 0, 16 * px, 3 * px, 16 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 6, meta == PCnt_WeaselType.SLAVE?208:224, 5, 5, 5, 5 });

				//chip
				block.setBlockBounds(4 * px, 3 * px, 3 * px, 12 * px, 5 * px, 13 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 0, 196, 195, 195, 195, 195 });

				break;

			case PCnt_WeaselType.PORT:
				//floor piece
				block.setBlockBounds(0, 0, 0, 16 * px, 3 * px, 16 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 6, 208, 5, 5, 5, 5 });

				//chip
				block.setBlockBounds(5 * px, 3 * px, 5 * px, 11 * px, 5 * px, 11 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 0, 194, 193, 193, 193, 193 });

				break;

			case PCnt_WeaselType.DISPLAY:
				// floor
				block.setBlockBounds(3 * px, 0, 3 * px, 13 * px, 1 * px, 13 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 192, 192, 176, 176, 176, 176 });

				// leg
				block.setBlockBounds(7.2F * px, 1 * px, 7.2F * px, 8.8F * px, 2 * px, 8.8F * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 176, 176, 176, 176, 176, 176 });

				// screen
				block.setBlockBounds(0 * px, 2 * px, 7 * px, 16 * px, 16 * px, 9 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 176, 176, 178, 177, 176, 176 });

				break;

			case PCnt_WeaselType.SPEAKER:
				// floor
				block.setBlockBounds(0 * px, 0, 0 * px, 16 * px, 3 * px, 16 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 6, 227, 5, 5, 5, 5 });

				// box
				block.setBlockBounds(2F * px, 1 * px, 2F * px, 14F * px, 15 * px, 14F * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 179, 179, 180, 180, 180, 180 });
				break;

			case PCnt_WeaselType.TOUCHSCREEN:
				// legs
				block.setBlockBounds(3 * px, 0, 4 * px, 4 * px, 1 * px, 12 * px);
				PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 197);
				block.setBlockBounds(12 * px, 0, 4 * px, 13 * px, 1 * px, 12 * px);
				PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 197);
				//sticks
				block.setBlockBounds(3 * px, 1 * px, 7.5F * px, 4 * px, 2 * px, 8.5F * px);
				PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 197);
				block.setBlockBounds(12 * px, 1 * px, 7.5F * px, 13 * px, 2 * px, 8.5F * px);
				PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 197);

				//bottom
				block.setBlockBounds(0 * px, 2 * px, 7.5F * px, 16 * px, 3 * px, 8.5F * px);
				PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 197);
				//top
				block.setBlockBounds(0 * px, 15 * px, 7.5F * px, 16 * px, 16 * px, 8.5F * px);
				PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 197);
				//left
				block.setBlockBounds(0 * px, 3 * px, 7.5F * px, 1 * px, 15 * px, 8.5F * px);
				PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 197);
				//right
				block.setBlockBounds(15 * px, 3 * px, 7.5F * px, 16 * px, 15 * px, 8.5F * px);
				PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 197);

				break;

			case PCnt_WeaselType.DISK_MANAGER:
				block.setBlockBounds(0, 0, 0, 16 * px, 13 * px, 16 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 230, 209, 210, 210, 210, 210 });

				break;

			case PCnt_WeaselType.DISK_DRIVE:
				block.setBlockBounds(0, 0, 0, 16 * px, 13 * px, 16 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 230, 225, 211, 211, 211, 211 });

				break;

			case PCnt_WeaselType.TERMINAL:
				// floor
				block.setBlockBounds(1 * px, 0, 1 * px, 15 * px, 4 * px, 15 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 227, 212, 226, 226, 226, 226 });

				// screen
				block.setBlockBounds(2 * px, 4 * px, 2 * px, 14 * px, 12 * px, 8 * px);
				PC_Renderer.renderInvBoxWithTextures(renderblocks, block, new int[] { 229, 229, 228, 213, 214, 214 });

				break;

		}

		PC_Renderer.resetTerrain(true);

	}
}
