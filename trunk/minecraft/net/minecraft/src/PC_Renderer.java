package net.minecraft.src;

import org.lwjgl.opengl.GL11;

/**
 * Common rendering utils for PowerCraft
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PC_Renderer {

	/** Block with alternative terrain file */
	public static int swapTerrainRenderer = 0;
	/** block with rotated top face (gates and conveyors) */
	public static int rotatedBoxRenderer = 0;

	/**
	 * Render block by render type.<br>
	 * Works for swapTerrainRenderer and rotatedBoxRenderer
	 * 
	 * @param renderblocks block renderer
	 * @param blockAccess block access
	 * @param x x
	 * @param y y
	 * @param z z
	 * @param block the block
	 * @param rtype render type
	 * @return success
	 */
	public static boolean renderBlockByType(RenderBlocks renderblocks, IBlockAccess blockAccess, int x, int y, int z, Block block, int rtype) {
		if (rtype == swapTerrainRenderer) { return renderBlockSwapTerrain(renderblocks, blockAccess, x, y, z, block); }
		if (rtype == rotatedBoxRenderer) { return renderBlockRotatedBox(renderblocks, blockAccess, x, y, z, block); }
		return false;
	}

	/**
	 * Render world block with swapped terrain - must implement PC_ISwapTerrain
	 * 
	 * @param renderblocks block renderer
	 * @param blockAccess block access
	 * @param x x
	 * @param y y
	 * @param z z
	 * @param block the block
	 * @return success
	 */
	public static boolean renderBlockSwapTerrain(RenderBlocks renderblocks, IBlockAccess blockAccess, int x, int y, int z, Block block) {

		Tessellator tessellator = Tessellator.instance;

		/**
		 * Standard block, but with swapped terrain
		 */

		tessellator.draw();
		tessellator.startDrawingQuads();

		boolean swapped = swapTerrain(block);

		boolean gf = RenderBlocks.cfgGrassFix;
		RenderBlocks.cfgGrassFix = false;

		block.setBlockBoundsBasedOnState(blockAccess, x, y, z);
		renderblocks.renderStandardBlock(block, x, y, z);

		RenderBlocks.cfgGrassFix = gf;

		tessellator.draw();
		tessellator.startDrawingQuads();

		resetTerrain(swapped);

		return true;
	}

	/**
	 * Render world block as Rotated Box - must implement PC_IRotatedBox; swaps terrain if needed
	 * 
	 * @param renderblocks block renderer
	 * @param blockAccess block access
	 * @param x x
	 * @param y y
	 * @param z z
	 * @param block the block
	 * @return success
	 */
	public static boolean renderBlockRotatedBox(RenderBlocks renderblocks, IBlockAccess blockAccess, int x, int y, int z, Block block) {

		Tessellator tessellator = Tessellator.instance;
		int metaAt = blockAccess.getBlockMetadata(x, y, z);

		if (block instanceof PC_IRotatedBox) {

			tessellator.draw();
			tessellator.startDrawingQuads();

			boolean swapped = swapTerrain(block);

			block.setBlockBoundsBasedOnState(blockAccess, x, y, z);
			int l = ((PC_IRotatedBox) block).getRotation(metaAt);
			int i1 = l;
			renderblocks.renderStandardBlock(block, x, y, z);

			tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, x, y, z));
			tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
			tessellator.setNormal(0.0F, 1F, 0.0F);
			int k1 = block.getBlockTexture(blockAccess, x, y, z, 1);
			int l1 = (k1 & 0xf) << 4;
			int i2 = k1 & 0xf0;
			double d5 = l1 / 256F;
			double d6 = (l1 + 15.99F) / 256F;
			double d7 = i2 / 256F;
			double d8 = (i2 + 15.99F) / 256F;
			double d9 = (block.maxY);
			double d10 = x + block.maxX;
			double d11 = x + block.maxX;
			double d12 = x + block.minX;
			double d13 = x + block.minX;
			double d14 = z + block.minZ;
			double d15 = z + block.maxZ;
			double d16 = z + block.maxZ;
			double d17 = z + block.minZ;
			double d18 = y + d9;
			if (i1 == 2) {
				d10 = d11 = x + block.minX;
				d12 = d13 = x + block.maxX;
				d14 = d17 = z + block.maxZ;
				d15 = d16 = z + block.minZ;
			} else if (i1 == 3) {
				d10 = d13 = x + block.minX;
				d11 = d12 = x + block.maxX;
				d14 = d15 = z + block.minZ;
				d16 = d17 = z + block.maxZ;
			} else if (i1 == 1) {
				d10 = d13 = x + block.maxX;
				d11 = d12 = x + block.minX;
				d14 = d15 = z + block.maxZ;
				d16 = d17 = z + block.minZ;
			}

			tessellator.addVertexWithUV(d13, d18, d17, d5, d7);
			tessellator.addVertexWithUV(d12, d18, d16, d5, d8);
			tessellator.addVertexWithUV(d11, d18, d15, d6, d8);
			tessellator.addVertexWithUV(d10, d18, d14, d6, d7);

			tessellator.draw();
			tessellator.startDrawingQuads();

			resetTerrain(swapped);

			return true;
		}

		return false;
	}

	/**
	 * Render crossed squares in world; swaps terrain if needed
	 * TODO untested
	 * 
	 * @param renderblocks
	 * @param blockAccess
	 * @param x x
	 * @param y y
	 * @param z z
	 * @param block
	 */
	public static void renderCrossedSquares(RenderBlocks renderblocks, IBlockAccess blockAccess, int x, int y, int z, Block block) {
		Tessellator tessellator = Tessellator.instance;

		tessellator.draw();
		tessellator.startDrawingQuads();

		boolean swapped = swapTerrain(block);

		block.setBlockBoundsBasedOnState(blockAccess, x, y, z);
		tessellator.setBrightness(block.getMixedBrightnessForBlock(blockAccess, x, y, z));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

		int t = block.getBlockTextureFromSideAndMetadata(0, blockAccess.getBlockMetadata(x, y, z));

		int k = (t & 0xf) << 4;
		int l = t & 0xf0;
		double d3 = k / 256F;
		double d4 = (k + 15.99F) / 256F;
		double d5 = l / 256F;
		double d6 = (l + 15.99F) / 256F;
		double d7 = (x + 0.5D) - 0.45000000000000001D;
		double d8 = x + 0.5D + 0.45000000000000001D;
		double d9 = (z + 0.5D) - 0.45000000000000001D;
		double d10 = z + 0.5D + 0.45000000000000001D;
		tessellator.addVertexWithUV(d7, y + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d7, y + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d8, y + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d8, y + 1.0D, d10, d4, d5);
		tessellator.addVertexWithUV(d8, y + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d8, y + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d7, y + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d7, y + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d7, y + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d7, y + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d8, y + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d8, y + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d8, y + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d8, y + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d7, y + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d7, y + 1.0D, d10, d4, d5);

		tessellator.draw();
		tessellator.startDrawingQuads();

		resetTerrain(swapped);
	}

	/* ITEMS */

	/**
	 * Render inv block using given render type.
	 * 
	 * @param renderblocks block renderer
	 * @param block the block
	 * @param meta metadata
	 * @param rtype render type
	 */
	public static void renderInvBlockByType(RenderBlocks renderblocks, Block block, int meta, int rtype) {
		if (rtype == swapTerrainRenderer) {
			renderInvBlock(renderblocks, block, meta);
		} else if (rtype == rotatedBoxRenderer) {
			renderInvBlockRotatedBox(renderblocks, block, meta);
		}
		return;
	}

	/**
	 * Render block on inventory. Does not swap terrain.
	 * 
	 * @param renderblocks block renderer
	 * @param block the block
	 * @param meta metadata
	 */
	public static void renderInvBox(RenderBlocks renderblocks, Block block, int meta) {
		Tessellator tessellator = Tessellator.instance;

		int[] textures = new int[6];
		if (block instanceof PC_ISpecialInventoryTextures) {
			for (int a = 0; a < 6; a++) {
				textures[a] = ((PC_ISpecialInventoryTextures) block).getInvTexture(a, meta);
			}
		} else {
			for (int a = 0; a < 6; a++) {
				textures[a] = block.getBlockTextureFromSideAndMetadata(a, meta);
			}
		}

		block.setBlockBoundsForItemRender();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1F, 0.0F);
		renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, textures[0]);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, textures[1]);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1F);
		renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, textures[2]);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, textures[3]);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, textures[4]);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, textures[5]);
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);

	}


	/**
	 * Render inventory block. Set it's bounds before rendering if you want.<br>
	 * <b>Does not swap terrain</b>, do it before and after calling it.<br>
	 * <b>This method uses provided texture index for all sides!</b>
	 * 
	 * @param renderblocks block renderer
	 * @param block the block
	 * @param texture used texture index
	 */
	public static void renderInvBoxWithTexture(RenderBlocks renderblocks, Block block, int texture) {
		Tessellator tessellator = Tessellator.instance;

		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1F, 0.0F);
		renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, texture);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, texture);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1F);
		renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, texture);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, texture);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, texture);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, texture);
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	/**
	 * Render item block; swap terrain if needed
	 * 
	 * @param renderblocks block renderer
	 * @param block the block
	 * @param meta metadata
	 */
	public static void renderInvBlock(RenderBlocks renderblocks, Block block, int meta) {

		boolean swapped = swapTerrain(block);

		renderInvBox(renderblocks, block, meta);

		resetTerrain(swapped);

	}

	/**
	 * Render item block as rotated box; swaps terrain if needed
	 * 
	 * @param renderblocks
	 * @param block
	 * @param meta
	 */
	public static void renderInvBlockRotatedBox(RenderBlocks renderblocks, Block block, int meta) {
		Tessellator tessellator = Tessellator.instance;

		if (block instanceof PC_IRotatedBox) {

			boolean swapped = swapTerrain(block);

			boolean renderOnSide = ((PC_IRotatedBox) block).renderItemHorizontal();

			if (renderOnSide) {
				block.setBlockBoundsForItemRender();
			} else {
				block.setBlockBounds(-0.1F, -0.1F, 0.4F, 1.1F, 1.1F, 0.6F);
			}
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1F, 0.0F);
			renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 0 : 0, meta));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 1 : 0, meta));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1F);
			renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 2 : 0, meta));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 3 : 1, meta));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1F, 0.0F, 0.0F);
			renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 4 : 0, meta));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 5 : 0, meta));
			tessellator.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);

			resetTerrain(swapped);

			return;
		}
	}

	/**
	 * Render item block as Crossed Squares - grass, flower etc.; swaps terrain if needed
	 * 
	 * @param renderblocks
	 * @param block
	 * @param meta
	 */
	public static void renderInvBlockCrossedSquares(RenderBlocks renderblocks, Block block, int meta) {

		int texture = block.getBlockTextureFromSideAndMetadata(0, meta);

		renderInvBlockCrossedSquaresWithTexture(renderblocks, texture, block);

		return;
	}

	/**
	 * Render item block as Crossed Squares - grass, flower etc.; swaps terrain if needed
	 * <b>This method uses provided texture index for all sides!</b>
	 * 
	 * @param renderblocks
	 * @param textureIndex
	 * @param block
	 */
	public static void renderInvBlockCrossedSquaresWithTexture(RenderBlocks renderblocks, int textureIndex, Block block) {

		boolean swapped = swapTerrain(block);

		renderInvBlockCrossedSquares_do(renderblocks, textureIndex, block);

		resetTerrain(swapped);

		return;
	}

	/**
	 * Do render inv block crossed squares. Does not swap terrain.
	 * 
	 * @param renderblocks
	 * @param textureIndex
	 * @param block
	 */
	private static void renderInvBlockCrossedSquares_do(RenderBlocks renderblocks, int textureIndex, Block block) {
		Tessellator tessellator = Tessellator.instance;

		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();

		double d = 0;
		double d1 = 0;
		double d2 = 0;

		int k = (textureIndex & 0xf) << 4;
		int l = textureIndex & 0xf0;
		double d3 = k / 256F;
		double d4 = (k + 15.99F) / 256F;
		double d5 = l / 256F;
		double d6 = (l + 15.99F) / 256F;
		double d7 = (d + 0.5D) - 0.45000000000000001D;
		double d8 = d + 0.5D + 0.45000000000000001D;
		double d9 = (d2 + 0.5D) - 0.45000000000000001D;
		double d10 = d2 + 0.5D + 0.45000000000000001D;
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d10, d4, d5);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d10, d3, d5);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d10, d3, d6);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d9, d4, d6);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d9, d4, d5);
		tessellator.addVertexWithUV(d8, d1 + 1.0D, d9, d3, d5);
		tessellator.addVertexWithUV(d8, d1 + 0.0D, d9, d3, d6);
		tessellator.addVertexWithUV(d7, d1 + 0.0D, d10, d4, d6);
		tessellator.addVertexWithUV(d7, d1 + 1.0D, d10, d4, d5);

		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);

	}

	/**
	 * Use texture file as terrain.png
	 * 
	 * @param filename name of the used texture file (png)
	 */
	public static void swapTerrain(String filename) {
		RenderEngine renderengine = ModLoader.getMinecraftInstance().renderEngine;
		renderengine.bindTexture(renderengine.getTexture(filename));
	}

	/**
	 * If block implements ISwapTerrain, set used terrain texture to the one from this block
	 * 
	 * @param block the block to render
	 * @return true if terrain was swapped -> call resetTerrain() to re-enable original terrain.png
	 */
	public static boolean swapTerrain(Block block) {
		RenderEngine renderengine = ModLoader.getMinecraftInstance().renderEngine;
		if (block instanceof PC_ISwapTerrain) {
			renderengine.bindTexture(renderengine.getTexture(((PC_ISwapTerrain) block).getTerrainFile()));
			return true;
		}
		return false;
	}

	/**
	 * Reset swapped terrain - set the original terrain file back.
	 * 
	 * @param do_it false = do nothing
	 */
	public static void resetTerrain(Boolean do_it) {
		RenderEngine renderengine = ModLoader.getMinecraftInstance().renderEngine;
		renderengine.bindTexture(renderengine.getTexture("/terrain.png"));
	}

	/**
	 * preload texture
	 * 
	 * @param filename texture file name (png)
	 * @return texture index (usually useless)
	 */
	public static int preloadTexture(String filename) {
		return ModLoader.getMinecraftInstance().renderEngine.getTexture(filename);
	}

	/**
	 * Label renderer for entity and tile entity renderers.
	 * 
	 * @param label label text
	 * @param realPos real position in world
	 * @param viewDistance distance to player at which the label disappears
	 * @param yOffset height of the label
	 * @param x relative x
	 * @param y relative y
	 * @param z relative z
	 */
	public static void renderEntityLabelAt(String label, PC_CoordF realPos, int viewDistance, float yOffset, double x, double y, double z) {

		RenderManager renderManager = RenderManager.instance;

		float f = (float) renderManager.livingPlayer.getDistance(realPos.x + 0.5D, realPos.y + 0.5D, realPos.z + 0.5D);

		if (f > viewDistance) { return; }

		FontRenderer fontrenderer = renderManager.getFontRenderer();
		float f1 = 1.0F; // 1.6F;
		float f2 = 0.01666667F * f1;
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + yOffset, (float) z + 0.5F);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-f2, -f2, f2);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		// GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator = Tessellator.instance;
		byte byte0 = 0;

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		tessellator.startDrawingQuads();
		float i = (fontrenderer.getStringWidth(label) / 2) * 1.12F;
		tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
		tessellator.addVertex(-i - 1, -1 + byte0, 0.0D);
		tessellator.addVertex(-i - 1, 8 + byte0, 0.0D);
		tessellator.addVertex(i + 1, 8 + byte0, 0.0D);
		tessellator.addVertex(i + 1, -1 + byte0, 0.0D);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		fontrenderer.drawString(label, -fontrenderer.getStringWidth(label) / 2, byte0, 0xffffffff);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();

	}

}
