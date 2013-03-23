package powercraft.weasel;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.renderer.PC_Renderer;

public class PCws_WeaselPluginInfoTouchscreen extends PCws_WeaselPluginInfo {

	private static PCws_WeaselModelTouchscreen model = new PCws_WeaselModelTouchscreen();
	
	public PCws_WeaselPluginInfoTouchscreen() {
		super(PCws_WeaselPluginTouchscreen.class, "Weasel Touchscreen", "weasel_touchscreen");
	}

	@Override
	public void renderInventoryBlock(PCws_BlockWeasel block, Object renderer) {
		float px = 0.0625F;
		// legs
		block.setWeaselBlockBounds(3 * px, 0, 4 * px, 4 * px, 1 * px, 12 * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, icons[4]);
		block.setWeaselBlockBounds(12 * px, 0, 4 * px, 13 * px, 1 * px, 12 * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, icons[4]);
		//sticks
		block.setWeaselBlockBounds(3 * px, 1 * px, 7.5F * px, 4 * px, 2 * px, 8.5F * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, icons[4]);
		block.setWeaselBlockBounds(12 * px, 1 * px, 7.5F * px, 13 * px, 2 * px, 8.5F * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, icons[4]);

		//bottom
		block.setWeaselBlockBounds(0 * px, 2 * px, 7.5F * px, 16 * px, 3 * px, 8.5F * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, icons[4]);
		//top
		block.setWeaselBlockBounds(0 * px, 15 * px, 7.5F * px, 16 * px, 16 * px, 8.5F * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, icons[4]);
		//left
		block.setWeaselBlockBounds(0 * px, 3 * px, 7.5F * px, 1 * px, 15 * px, 8.5F * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, icons[4]);
		//right
		block.setWeaselBlockBounds(15 * px, 3 * px, 7.5F * px, 16 * px, 15 * px, 8.5F * px);
		PC_Renderer.renderInvBoxWithTexture(renderer, block, icons[4]);
		block.setWeaselBlockBounds(0, 0, 0, 1, 1, 1);
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
	
	@Override
	public Icon getTexture(int side) {
		return icons[4];
	}
	
}
