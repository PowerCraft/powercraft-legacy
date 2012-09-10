package net.minecraft.src;

import net.minecraft.src.PCnt.PCnt_BlockWeasel;
import net.minecraft.src.PCnt.PCnt_WeaselType;

import org.lwjgl.opengl.GL11;


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
	/** Light renderer */
	public static int lightRenderer;

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

		if (rtype == lightRenderer) {
			return true;
		}

		if (rtype == radioRenderer) {
			return true;
		}

		if (rtype == sensorRenderer) {
			return true;
		}

		if (rtype == weaselRenderer) {
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


		double[] a = {block.minX,block.minY,block.minZ,block.maxX,block.maxY,block.maxZ};		
		
		if (rtype == radioRenderer) {
			renderInvBlockRadio(renderblocks, block, meta);
		}

		if (rtype == sensorRenderer) {
			renderInvBlockSensor(renderblocks, (PClo_BlockSensor) block, meta);
		}

		if (rtype == weaselRenderer) {
			renderInvBlockWeasel(renderblocks, (PCnt_BlockWeasel) block, meta);
		}

		if (rtype == lightRenderer) {
			renderInvBlockLight(renderblocks, block, meta);
		}		

		block.setBlockBounds((float)a[0],(float)a[1],(float)a[2],(float)a[3],(float)a[4],(float)a[5]);

	}

	private static void renderInvBlockLight(RenderBlocks renderblocks, Block block, int meta) {

		PC_Renderer.swapTerrain(mod_PClogic.getTerrainFile());
		
		//if (meta < 32) {
			float sidehalf = 0.1875F;
			float height = 0.15F;
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			block.setBlockBounds(0.5F - sidehalf, 0.5F - sidehalf, 0.5F - height / 2F, 0.5F + sidehalf, 0.5F + sidehalf, 0.5F + height / 2F);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 66);
		/*} else {
			float sidehalf = 0.5F - 0.0625F;
			float height = 0.18F;
			block.setBlockBounds(0.5F - sidehalf, 0.5F - sidehalf, 0.5F - height / 2F, 0.5F + sidehalf, 0.5F + sidehalf, 0.5F + height / 2F);
			PC_Renderer.renderInvBoxWithTexture(renderblocks, block, 117);
		}*/
		
		PC_Renderer.resetTerrain(true);

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

		PC_Renderer.resetTerrain(true);

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
