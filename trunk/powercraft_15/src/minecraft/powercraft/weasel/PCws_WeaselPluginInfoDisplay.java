package powercraft.weasel;

import net.minecraft.util.Icon;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_Direction;

public class PCws_WeaselPluginInfoDisplay extends PCws_WeaselPluginInfo {

	private static PCws_WeaselModelDisplay model = new PCws_WeaselModelDisplay();
	
	public PCws_WeaselPluginInfoDisplay() {
		super(PCws_WeaselPluginDisplay.class, "Weasel Display", "weasel_display_frame", "weasel_display_plate", "weasel_display_back", "weasel_display_front");
	}
	
	@Override
	public void renderInventoryBlock(PCws_BlockWeasel block, Object renderer) {
		float px = 0.0625F;
		block.setWeaselBlockBounds(3 * px, 0, 3 * px, 13 * px, 1 * px, 13 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new Icon[] { icons[5], icons[5], icons[4], icons[4], icons[4], icons[4] });

		// leg
		block.setWeaselBlockBounds(7.2F * px, 1 * px, 7.2F * px, 8.8F * px, 2 * px, 8.8F * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new Icon[] { icons[4], icons[4], icons[4], icons[4], icons[4], icons[4] });

		// screen
		block.setWeaselBlockBounds(0 * px, 2 * px, 7 * px, 16 * px, 16 * px, 9 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new Icon[] { icons[4], icons[4], icons[6], icons[7], icons[4], icons[4] });
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
	public Icon getTexture(PC_Direction side) {
		if(side==PC_Direction.BACK){
			return icons[6];
		}else if(side==PC_Direction.FRONT){
			return icons[7];
		}else{
			return icons[4];
		}
	}
	
}
