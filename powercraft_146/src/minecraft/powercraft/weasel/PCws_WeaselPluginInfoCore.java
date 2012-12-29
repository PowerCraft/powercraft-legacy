package powercraft.weasel;

import net.minecraft.block.Block;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils.ValueWriting;

public class PCws_WeaselPluginInfoCore extends PCws_WeaselPluginInfo {

	private static PCws_WeaselModelCore model = new PCws_WeaselModelCore();
	
	public PCws_WeaselPluginInfoCore() {
		super(PCws_WeaselPluginCore.class, "Core");
	}

	@Override
	public void renderInventoryBlock(Block block, Object renderer) {
		float px = 0.0625F;
		PC_Renderer.swapTerrain(block);
		//floor
		ValueWriting.setBlockBounds(block, 0, 0, 0, 16 * px, 3 * px, 16 * px);
		block.setBlockBounds(0, 0, 0, 16 * px, 3 * px, 16 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new int[] { 6, 224, 5, 5, 5, 5 });

		//chip
		ValueWriting.setBlockBounds(block, 4 * px, 3 * px, 3 * px, 12 * px, 5 * px, 13 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new int[] { 0, 196, 195, 195, 195, 195 });
		ValueWriting.setBlockBounds(block, 0, 0, 0, 1, 1, 1);
		PC_Renderer.resetTerrain(true);
	}

	@Override
	public PCws_WeaselModelBase getModel() {
		return model;
	}

	
	
}
