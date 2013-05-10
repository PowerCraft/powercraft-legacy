package mods.betterworld.CB.client;

import org.lwjgl.opengl.GL11;

import mods.betterworld.CB.block.BWCB_BlockStairsGlass;
import mods.betterworld.CB.block.BWCB_BlockStairsGlassR;
import mods.betterworld.CB.block.BWCB_BlockStairsStone;
import mods.betterworld.CB.block.BWCB_BlockStairsStoneR;
import mods.betterworld.CB.block.BWCB_BlockStairsWood;
import mods.betterworld.CB.block.BWCB_BlockStairsWoodR;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BWCB_Render implements ISimpleBlockRenderingHandler {

	private static BWCB_Render render;

	private int renderID;

	private BWCB_Render() {
		renderID = RenderingRegistry.getNextAvailableRenderId();
	}

	public static BWCB_Render getRender() {
		if (render == null) {
			render = new BWCB_Render();
		}
		return render;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
		if (block instanceof BWCB_BlockStairsStone) {
			renderInventoryBlockStairs(block, metadata, renderer);
		}
		if (block instanceof BWCB_BlockStairsStoneR) {
			renderInventoryBlockStairs(block, metadata, renderer);
		}
		if (block instanceof BWCB_BlockStairsWood) {
			renderInventoryBlockStairs(block, metadata, renderer);
		}
		if (block instanceof BWCB_BlockStairsWoodR) {
			renderInventoryBlockStairs(block, metadata, renderer);
		}
		if (block instanceof BWCB_BlockStairsGlass) {
			renderInventoryBlockStairs(block, metadata, renderer);
		}
		if (block instanceof BWCB_BlockStairsGlassR) {
			renderInventoryBlockStairs(block, metadata, renderer);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		if (block instanceof BWCB_BlockStairsStone) {
			renderer.renderBlockStairs((BWCB_BlockStairsStone) block, x, y, z);
			return true;
		}
		if (block instanceof BWCB_BlockStairsStoneR) {
			renderer.renderBlockStairs((BWCB_BlockStairsStoneR) block, x, y, z);
			return true;
		}
		if (block instanceof BWCB_BlockStairsWood) {
			renderer.renderBlockStairs((BWCB_BlockStairsWood) block, x, y, z);
			return true;
		}
		if (block instanceof BWCB_BlockStairsWoodR) {
			renderer.renderBlockStairs((BWCB_BlockStairsWoodR) block, x, y, z);
			return true;
		}
		if (block instanceof BWCB_BlockStairsGlass) {
			renderer.renderBlockStairs((BWCB_BlockStairsGlass) block, x, y, z);
			return true;
		}
		if (block instanceof BWCB_BlockStairsGlassR) {
			renderer.renderBlockStairs((BWCB_BlockStairsGlassR) block, x, y, z);
			return true;
		}
		return false;
	}

	private void renderInventoryBlockStairs(Block block, int metadata,
			RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		for (int i = 0; i < 2; i++) {
			if (i == 0) {
				renderer.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
			}

			if (i == 1) {
				renderer.setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
			}

			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer
					.getBlockIconFromSideAndMetadata(block, 0, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer
					.getBlockIconFromSideAndMetadata(block, 0, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer
					.getBlockIconFromSideAndMetadata(block, 0, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer
					.getBlockIconFromSideAndMetadata(block, 0, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer
					.getBlockIconFromSideAndMetadata(block, 0, metadata));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer
					.getBlockIconFromSideAndMetadata(block, 0, metadata));
			tessellator.draw();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}
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
