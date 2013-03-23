package powercraft.weasel;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import powercraft.api.PC_Struct4;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.renderer.PC_Renderer;

public class PCws_WeaselPluginInfoPort extends PCws_WeaselPluginInfo {

	private static PCws_WeaselModelPort model = new PCws_WeaselModelPort();
	
	public PCws_WeaselPluginInfoPort() {
		super(PCws_WeaselPluginPort.class, "Weasel Port", "weasel_port_top", "weasel_port_side");
	}

	@Override
	public void renderInventoryBlock(Block block, Object renderer) {
		float px = 0.0625F;
		block.setBlockBounds(0, 0, 0, 16 * px, 3 * px, 16 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new Icon[] { icons[0], icons[2], icons[1], icons[1], icons[1], icons[1] });

		//chip
		block.setBlockBounds(5 * px, 3 * px, 5 * px, 11 * px, 5 * px, 11 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new Icon[] { null, icons[4], icons[5], icons[5], icons[5], icons[5] });
		block.setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	@Override
	public PCws_WeaselModelBase getModel() {
		return model;
	}

	@Override
	public void getServerMsg(PCws_TileEntityWeasel te, String msg, Object obj) {
		if(msg.equalsIgnoreCase("play")){
			PC_Struct4<String, Float, Float, Float> s = (PC_Struct4<String, Float, Float, Float>)obj;
			te.getWorldObj().playSoundEffect(te.xCoord + 0.5D, te.yCoord + 0.5D, te.zCoord + 0.5D, s.a, s.b, s.c);
			te.getWorldObj().spawnParticle("note", te.xCoord + 0.5D, te.yCoord + 0.5D, te.zCoord + 0.5D, s.d, 0.0D, 0.0D);
		}
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
