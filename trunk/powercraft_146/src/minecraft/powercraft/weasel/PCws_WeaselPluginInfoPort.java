package powercraft.weasel;

import net.minecraft.block.Block;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils.ValueWriting;

public class PCws_WeaselPluginInfoPort extends PCws_WeaselPluginInfo {

	private static PCws_WeaselModelPort model = new PCws_WeaselModelPort();
	
	public PCws_WeaselPluginInfoPort() {
		super(PCws_WeaselPluginPort.class, "Weasel Port");
	}

	@Override
	public void renderInventoryBlock(Block block, Object renderer) {
		PC_Renderer.swapTerrain(block);

		float px = 0.0625F;
		ValueWriting.setBlockBounds(block, 0, 0, 0, 16 * px, 3 * px, 16 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new int[] { 6, 208, 5, 5, 5, 5 });

		//chip
		ValueWriting.setBlockBounds(block, 5 * px, 3 * px, 5 * px, 11 * px, 5 * px, 11 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new int[] { 0, 194, 193, 193, 193, 193 });
		ValueWriting.setBlockBounds(block, 0, 0, 0, 1, 1, 1);
		
		PC_Renderer.resetTerrain(true);
	}

	@Override
	public PCws_WeaselModelBase getModel() {
		return model;
	}

	@Override
	public void getServerMsg(PCws_TileEntityWeasel te, String msg, Object obj) {
		// TODO Auto-generated method stub
		super.getServerMsg(te, msg, obj);
	}

	
	
}
