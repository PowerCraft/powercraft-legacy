package powercraft.weasel;

import net.minecraft.block.Block;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.registry.PC_SoundRegistry;
import powercraft.api.renderer.PC_Renderer;

public class PCws_WeaselPluginInfoTerminal extends PCws_WeaselPluginInfo {

	private static PCws_WeaselModelTerminal model = new PCws_WeaselModelTerminal();
	
	public PCws_WeaselPluginInfoTerminal() {
		super(PCws_WeaselPluginTerminal.class, "Weasel Terminal");
	}

	@Override
	public void renderInventoryBlock(Block block, Object renderer) {
		PC_Renderer.swapTerrain(block);
		float px = 0.0625F;
		ValueWriting.setBlockBounds(block, 1 * px, 0, 1 * px, 15 * px, 4 * px, 15 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new int[] { 227, 212, 226, 226, 226, 226 });

		// screen
		ValueWriting.setBlockBounds(block, 2 * px, 4 * px, 2 * px, 14 * px, 12 * px, 8 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new int[] { 229, 229, 228, 213, 214, 214 });
		ValueWriting.setBlockBounds(block, 0, 0, 0, 1, 1, 1);
		
		PC_Renderer.resetTerrain(true);
	}

	@Override
	public PCws_WeaselModelBase getModel() {
		return model;
	}

	@Override
	public void getServerMsg(PCws_TileEntityWeasel te, String msg, Object obj) {
		if(msg.equals("play")){
			PC_SoundRegistry.playSound(te.xCoord + 0.5D, te.yCoord + 0.5D, te.zCoord + 0.5D, "random.click", 0.05F, 3F);
		}
	}
	
}
