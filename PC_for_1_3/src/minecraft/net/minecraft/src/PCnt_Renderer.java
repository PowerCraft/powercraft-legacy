package net.minecraft.src;

import net.minecraft.src.PCnt.PC_TeleporterData;
import net.minecraft.src.PCnt.PCnt_TeleporterManager;

public class PCnt_Renderer {

	/** Teleporter block renderer */
	public static int teleporterRenderer = 0;
	
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
		PC_TeleporterData td = PCnt_TeleporterManager.getTeleporterDataAt(i, j, k);
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
}
