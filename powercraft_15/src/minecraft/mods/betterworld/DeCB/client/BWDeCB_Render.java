package mods.betterworld.DeCB.client;

import org.lwjgl.opengl.GL11;

import mods.betterworld.DeCB.blocks.BWDeCB_BlockFenceWood;
import mods.betterworld.DeCB.blocks.BWDeCB_BlockFenceWoodGate;
import mods.betterworld.DeCB.blocks.BWDeCB_BlockWallStone;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BWDeCB_Render implements ISimpleBlockRenderingHandler {

	private static BWDeCB_Render render;

	private int renderID;

	private BWDeCB_Render() {
		renderID = RenderingRegistry.getNextAvailableRenderId();
	}

	public static BWDeCB_Render getRender() {
		if (render == null) {
			render = new BWDeCB_Render();
		}
		return render;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
		if (block instanceof BWDeCB_BlockFenceWood) {
			renderInventoryBlockFenceWood(block, metadata, renderer);
		}
		if (block instanceof BWDeCB_BlockFenceWoodGate) {
			renderInventoryBlockFenceWoodGate(block, metadata, renderer);
		}
		if (block instanceof BWDeCB_BlockWallStone) {
			renderInventoryBlockWallStone(block, metadata, renderer);
		}
	}



	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		if (block instanceof BWDeCB_BlockFenceWood) {
			renderer.renderBlockFence((BWDeCB_BlockFenceWood) block, x, y, z);
			return true;
		}
		if (block instanceof BWDeCB_BlockFenceWoodGate) {
			renderer.renderBlockFenceGate((BWDeCB_BlockFenceWoodGate) block, x, y, z);
			return true;
		}
		if (block instanceof BWDeCB_BlockWallStone) {
			renderer.renderBlockWall((BWDeCB_BlockWallStone) block, x, y, z);
			return true;
		}
		return false;
	}

	private void renderInventoryBlockFenceWood(Block block, int metadata,
			RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		for (int k = 0; k < 4; ++k)
        {
            float f2 = 0.125F;

            if (k == 0)
            {
                renderer.setRenderBounds((double)(0.5F - f2), 0.0D, 0.0D, (double)(0.5F + f2), 1.0D, (double)(f2 * 2.0F));
            }

            if (k == 1)
            {
                renderer.setRenderBounds((double)(0.5F - f2), 0.0D, (double)(1.0F - f2 * 2.0F), (double)(0.5F + f2), 1.0D, 1.0D);
            }

            f2 = 0.0625F;

            if (k == 2)
            {
                renderer.setRenderBounds((double)(0.5F - f2), (double)(1.0F - f2 * 3.0F), (double)(-f2 * 2.0F), (double)(0.5F + f2), (double)(1.0F - f2), (double)(1.0F + f2 * 2.0F));
            }

            if (k == 3)
            {
                renderer.setRenderBounds((double)(0.5F - f2), (double)(0.5F - f2 * 3.0F), (double)(-f2 * 2.0F), (double)(0.5F + f2), (double)(0.5F - f2), (double)(1.0F + f2 * 2.0F));
            }

            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }

       renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	}
	
	private void renderInventoryBlockFenceWoodGate(Block block, int metadata,
			RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		for (int k = 0; k < 3; ++k)
        {
            float f2 = 0.0625F;

            if (k == 0)
            {
                renderer.setRenderBounds((double)(0.5F - f2), 0.30000001192092896D, 0.0D, (double)(0.5F + f2), 1.0D, (double)(f2 * 2.0F));
            }

            if (k == 1)
            {
            	renderer.setRenderBounds((double)(0.5F - f2), 0.30000001192092896D, (double)(1.0F - f2 * 2.0F), (double)(0.5F + f2), 1.0D, 1.0D);
            }

            f2 = 0.0625F;

            if (k == 2)
            {
            	renderer.setRenderBounds((double)(0.5F - f2), 0.5D, 0.0D, (double)(0.5F + f2), (double)(1.0F - f2), 1.0D);
            }

            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
		
	}
	


	private void renderInventoryBlockWallStone(Block block, int metadata,
			RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		
		for (int k = 0; k < 2; ++k)
        {
            if (k == 0)
            {
                renderer.setRenderBounds(0.0D, 0.0D, 0.3125D, 1.0D, 0.8125D, 0.6875D);
            }

            if (k == 1)
            {
            	renderer.setRenderBounds(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
            }

            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
            tessellator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }

		renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
		
	}
	
	
	
	@Override
	public boolean shouldRender3DInInventory() {
		return true;
	}

	@Override
	public int getRenderId() {
		return renderID;
	}

}
