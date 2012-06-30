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
		}
		if (rtype == sensorRenderer) {
			renderInvBlockSensor(renderblocks, (PClo_BlockSensor) block, meta);
		}
	}

	private static void renderInvBlockRadio(RenderBlocks renderblocks, Block block, int meta) {
		RenderEngine renderengine = ModLoader.getMinecraftInstance().renderEngine;

		renderengine.bindTexture(renderengine.getTexture(mod_PClogic.getTerrainFile()));

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

		renderengine.bindTexture(renderengine.getTexture("/terrain.png"));

	}

	private static void renderInvBlockSensor(RenderBlocks renderblocks, PClo_BlockSensor block, int meta) {
		RenderEngine renderengine = ModLoader.getMinecraftInstance().renderEngine;

		renderengine.bindTexture(renderengine.getTexture(mod_PClogic.getTerrainFile()));

		float px = 0.0625F;

		int tx = meta == 0 ? 65 : meta == 1 ? 6 : 64;

		block.setBlockBounds(0, 0, 0, 16 * px, 4 * px, 16 * px);
		PC_Renderer.renderInvBoxWithTexture(renderblocks, block, tx);
		block.setBlockBounds(6 * px, 4 * px, 6 * px, 10 * px, 9 * px, 10 * px);
		PC_Renderer.renderInvBoxWithTexture(renderblocks, block, tx);
		block.setBlockBounds(5 * px, 8 * px, 5 * px, 11 * px, 14 * px, 11 * px);
		PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 68);
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		renderengine.bindTexture(renderengine.getTexture("/terrain.png"));

	}
}
