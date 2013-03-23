package powercraft.weasel;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import powercraft.api.PC_Struct4;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.registry.PC_SoundRegistry;
import powercraft.api.renderer.PC_Renderer;

public class PCws_WeaselPluginInfoSpeaker extends PCws_WeaselPluginInfo {
	
	private static PCws_WeaselModelSpeaker model = new PCws_WeaselModelSpeaker();
	
	public PCws_WeaselPluginInfoSpeaker() {
		super(PCws_WeaselPluginSpeaker.class, "Weasel Speaker", "weasel_speaker_top", "weasel_speaker_side");
	}

	@Override
	public void renderInventoryBlock(PCws_BlockWeasel block, Object renderer) {
		float px = 0.0625F;
		
		block.setWeaselBlockBounds(0 * px, 0, 0 * px, 16 * px, 3 * px, 16 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new Icon[] { icons[0], icons[3], icons[1], icons[1], icons[1], icons[1] });

		// box
		block.setWeaselBlockBounds(2F * px, 1 * px, 2F * px, 14F * px, 15 * px, 14F * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new Icon[] { null, icons[4], icons[5], icons[5], icons[5], icons[5] });
		block.setWeaselBlockBounds(0, 0, 0, 1, 1, 1);
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

	@Override
	public Icon getTexture(int side) {
		if(side==1){
			return icons[4];
		}else if(side==0){
			return icons[0];
		}else{
			return icons[5];
		}
	}
	
}
