package powercraft.weasel;

import net.minecraft.block.Block;
import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.renderer.PC_Renderer;

public class PCws_WeaselPluginInfoDisplay extends PCws_WeaselPluginInfo {

	private static PCws_WeaselModelDisplay model = new PCws_WeaselModelDisplay();
	
	public PCws_WeaselPluginInfoDisplay() {
		super(PCws_WeaselPluginDisplay.class, "Weasel Display");
	}

	@Override
	public void renderInventoryBlock(Block block, Object renderer) {
		PC_Renderer.swapTerrain(block);

		float px = 0.0625F;
		ValueWriting.setBlockBounds(block, 3 * px, 0, 3 * px, 13 * px, 1 * px, 13 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new int[] { 192, 192, 176, 176, 176, 176 });

		// leg
		ValueWriting.setBlockBounds(block, 7.2F * px, 1 * px, 7.2F * px, 8.8F * px, 2 * px, 8.8F * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new int[] { 176, 176, 176, 176, 176, 176 });

		// screen
		ValueWriting.setBlockBounds(block, 0 * px, 2 * px, 7 * px, 16 * px, 16 * px, 9 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new int[] { 176, 176, 178, 177, 176, 176 });
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
