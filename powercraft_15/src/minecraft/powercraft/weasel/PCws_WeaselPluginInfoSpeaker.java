package powercraft.weasel;

import net.minecraft.block.Block;
import powercraft.api.PC_Struct4;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.registry.PC_SoundRegistry;
import powercraft.api.renderer.PC_Renderer;

public class PCws_WeaselPluginInfoSpeaker extends PCws_WeaselPluginInfo {
	
	private static PCws_WeaselModelSpeaker model = new PCws_WeaselModelSpeaker();
	
	public PCws_WeaselPluginInfoSpeaker() {
		super(PCws_WeaselPluginSpeaker.class, "Weasel Speaker");
	}

	@Override
	public void renderInventoryBlock(Block block, Object renderer) {
		PC_Renderer.swapTerrain(block);

		float px = 0.0625F;
		
		ValueWriting.setBlockBounds(block, 0 * px, 0, 0 * px, 16 * px, 3 * px, 16 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new int[] { 6, 227, 5, 5, 5, 5 });

		// box
		ValueWriting.setBlockBounds(block, 2F * px, 1 * px, 2F * px, 14F * px, 15 * px, 14F * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new int[] { 179, 179, 180, 180, 180, 180 });
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
			PC_SoundRegistry.playSound(te.xCoord + 0.5D, te.yCoord + 0.5D, te.zCoord + 0.5D, s.a, s.b, s.c);
			te.getWorldObj().spawnParticle("note", te.xCoord + 0.5D, te.yCoord + 1.2D, te.zCoord + 0.5D, s.d, 0.0D, 0.0D);
		}
	}

	@Override
	public float[] getBounds() {
		return new float[]{ 0, 0, 0, 1, 1, 1 };
	}

}
