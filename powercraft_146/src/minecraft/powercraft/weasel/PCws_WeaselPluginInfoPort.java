package powercraft.weasel;

import net.minecraft.block.Block;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Struct4;
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
		if(msg.equalsIgnoreCase("play")){
			PC_Struct4<String, Float, Float, Float> s = (PC_Struct4<String, Float, Float, Float>)obj;
			te.worldObj.playSoundEffect(te.xCoord + 0.5D, te.yCoord + 0.5D, te.zCoord + 0.5D, s.a, s.b, s.c);
			te.worldObj.spawnParticle("note", te.xCoord + 0.5D, te.yCoord + 0.5D, te.zCoord + 0.5D, s.d, 0.0D, 0.0D);
		}
	}

	
	
}
