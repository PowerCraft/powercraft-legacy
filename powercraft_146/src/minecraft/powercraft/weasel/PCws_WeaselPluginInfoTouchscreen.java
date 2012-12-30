package powercraft.weasel;

import net.minecraft.block.Block;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils.ValueWriting;

public class PCws_WeaselPluginInfoTouchscreen extends PCws_WeaselPluginInfo {

	private static PCws_WeaselModelTouchscreen model = new PCws_WeaselModelTouchscreen();
	
	public PCws_WeaselPluginInfoTouchscreen() {
		super(PCws_WeaselPluginTouchscreen.class, "Weasel Touchscreen");
	}

	@Override
	public void renderInventoryBlock(Block block, Object renderer) {
		PC_Renderer.swapTerrain(block);

		float px = 0.0625F;
		// legs
		ValueWriting.setBlockBounds(block, 3 * px, 0, 4 * px, 4 * px, 1 * px, 12 * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 197);
		ValueWriting.setBlockBounds(block, 12 * px, 0, 4 * px, 13 * px, 1 * px, 12 * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 197);
		//sticks
		ValueWriting.setBlockBounds(block, 3 * px, 1 * px, 7.5F * px, 4 * px, 2 * px, 8.5F * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 197);
		ValueWriting.setBlockBounds(block, 12 * px, 1 * px, 7.5F * px, 13 * px, 2 * px, 8.5F * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 197);

		//bottom
		ValueWriting.setBlockBounds(block, 0 * px, 2 * px, 7.5F * px, 16 * px, 3 * px, 8.5F * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 197);
		//top
		ValueWriting.setBlockBounds(block, 0 * px, 15 * px, 7.5F * px, 16 * px, 16 * px, 8.5F * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 197);
		//left
		ValueWriting.setBlockBounds(block, 0 * px, 3 * px, 7.5F * px, 1 * px, 15 * px, 8.5F * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 197);
		//right
		ValueWriting.setBlockBounds(block, 15 * px, 3 * px, 7.5F * px, 16 * px, 15 * px, 8.5F * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, 197);
		ValueWriting.setBlockBounds(block, 0, 0, 0, 1, 1, 1);
		
		PC_Renderer.resetTerrain(true);
	}

	@Override
	public boolean hasSpecialRot() {
		return true;
	}
	
	@Override
	public PCws_WeaselModelBase getModel() {
		return model;
	}

	@Override
	public float[] getBounds() {
		return new float[]{ 0, 0, 0, 1, 1, 1 };
	}
	
}
