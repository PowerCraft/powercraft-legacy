package powercraft.weasel;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.registry.PC_SoundRegistry;
import powercraft.api.renderer.PC_Renderer;

public class PCws_WeaselPluginInfoTerminal extends PCws_WeaselPluginInfo {

	private static PCws_WeaselModelTerminal model = new PCws_WeaselModelTerminal();
	
	public PCws_WeaselPluginInfoTerminal() {
		super(PCws_WeaselPluginTerminal.class, "Weasel Terminal", "weasel_terminal_top", "weasel_terminal_side", "weasel_terminal_top_top", "weasel_terminal_top_side", "weasel_terminal_top_display", "weasel_terminal_top_back");
	}

	@Override
	public void renderInventoryBlock(PCws_BlockWeasel block, Object renderer) {
		float px = 0.0625F;
		block.setWeaselBlockBounds(1 * px, 0, 1 * px, 15 * px, 4 * px, 15 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new Icon[] { icons[0], icons[4], icons[5], icons[5], icons[5], icons[5] });

		// screen
		block.setWeaselBlockBounds(2 * px, 4 * px, 2 * px, 14 * px, 12 * px, 8 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new Icon[] { icons[6], icons[6], icons[9], icons[8], icons[7], icons[7] });
		block.setWeaselBlockBounds(0, 0, 0, 1, 1, 1);
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
