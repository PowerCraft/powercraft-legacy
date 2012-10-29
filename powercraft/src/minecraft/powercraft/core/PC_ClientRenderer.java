package powercraft.core;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class PC_ClientRenderer extends PC_Renderer implements ISimpleBlockRenderingHandler {
	
	public PC_ClientRenderer(boolean render3d){
		super(render3d);
		if(render3d)
			render3dId = RenderingRegistry.instance().getNextAvailableRenderId();
		else
			render2dId = RenderingRegistry.instance().getNextAvailableRenderId();
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		if(block instanceof PC_IBlockRenderer){
			((PC_IBlockRenderer) block).renderInventoryBlock(block, metadata, modelID, renderer);
		}else if(block instanceof PC_IRotatedBox){
			iRenderInvBlockRotatedBox(block, metadata, modelID, renderer);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if(block instanceof PC_IBlockRenderer){
			((PC_IBlockRenderer) block).renderWorldBlock(world, x, y, z, block, modelId, renderer);
			return true;
		}else if(block instanceof PC_IRotatedBox){
			iRenderBlockRotatedBox(world, x, y, z, block, modelId, renderer);
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return render3d;
	}

	@Override
	public int getRenderId() {
		if(render3d)
			return render3dId;
		return render2dId;
	}
	
	@Override
	protected void iTessellatorDraw(){
		Tessellator.instance.draw();
	};
	
	@Override
	protected void iTessellatorStartDrawingQuads(){
		Tessellator.instance.startDrawingQuads();
	};
	
	private RenderEngine getRenderEngine(){
		return PC_ClientUtils.mc().renderEngine;
	}
	
	@Override
	protected void iBindTexture(String texture){
		RenderEngine re = getRenderEngine();
		re.bindTexture(re.getTexture(texture));
	};
	
	@Override
	protected void iRenderStandardBlock(Object renderer, Block block, int x, int y, int z){
		((RenderBlocks)renderer).renderStandardBlock(block, x, y, z);
	};

	protected void iRenderInvBox(Object renderer, Block block, int metadata){
		Tessellator tessellator = Tessellator.instance;
		RenderBlocks renderblocks = (RenderBlocks)renderer;
		
		int[] textures = new int[6];
		if (block instanceof PC_ISpecialInventoryTextures) {
			for (int a = 0; a < 6; a++) {
				textures[a] = ((PC_ISpecialInventoryTextures) block).getInvTexture(a, metadata);
			}
		} else {
			for (int a = 0; a < 6; a++) {
				textures[a] = block.getBlockTextureFromSideAndMetadata(a, metadata);
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
	
	@Override
	protected void iRenderBlockRotatedBox(IBlockAccess world, int x, int y, int z, Block block, int modelId, Object renderer){
		RenderBlocks renderblocks = (RenderBlocks)renderer;
		Tessellator tessellator = Tessellator.instance;

		int metadata = PC_Utils.getMD(world, x, y, z);
		
		if (block instanceof PC_IRotatedBox) {

			boolean swapped = swapTerrain(block);

			boolean renderOnSide = ((PC_IRotatedBox) block).renderItemHorizontal();

			if (renderOnSide) {
				block.setBlockBoundsForItemRender();
			} else {
				block.setBlockBounds(-0.1F, -0.1F, 0.4F, 1.1F, 1.1F, 0.6F);
			}
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1F, 0.0F);
			renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 0 : 0, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 1 : 0, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1F);
			renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 2 : 0, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 3 : 1, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1F, 0.0F, 0.0F);
			renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 4 : 0, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 5 : 0, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);

			resetTerrain(swapped);

			return;
		}
	}
	
	@Override
	protected void iRenderInvBlockRotatedBox(Block block, int metadata, int modelID, Object renderer){
		RenderBlocks renderblocks = (RenderBlocks)renderer;
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
			renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 0 : 0, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 1 : 0, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1F);
			renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 2 : 0, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 3 : 1, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1F, 0.0F, 0.0F);
			renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 4 : 0, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(renderOnSide ? 5 : 0, metadata));
			tessellator.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);

			resetTerrain(swapped);

			return;
		}
	}
	
	/**
	 * Use texture file as terrain.png
	 * 
	 * @param filename name of the used texture file (png)
	 */
	public static void swapTerrain(String filename) {
		RenderEngine renderengine = PC_ClientUtils.mc().renderEngine;
		renderengine.bindTexture(renderengine.getTexture(filename));
	}

	/**
	 * If block implements ISwapTerrain, set used terrain texture to the one
	 * from this block
	 * 
	 * @param block the block to render
	 * @return true if terrain was swapped -> call resetTerrain() to re-enable
	 *         original terrain.png
	 */
	public static boolean swapTerrain(Block block) {
		RenderEngine renderengine = PC_ClientUtils.mc().renderEngine;
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
		RenderEngine renderengine = PC_ClientUtils.mc().renderEngine;
		renderengine.bindTexture(renderengine.getTexture("/terrain.png"));
	}
	
}