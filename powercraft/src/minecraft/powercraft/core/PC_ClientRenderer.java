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
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		if(block instanceof PC_IBlockRenderer){
			((PC_IBlockRenderer) block).renderWorldBlock(world, x, y, z, block, modelId, renderer);
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
	
}
