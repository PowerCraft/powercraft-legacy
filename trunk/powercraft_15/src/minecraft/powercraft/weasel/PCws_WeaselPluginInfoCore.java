package powercraft.weasel;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.Icon;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.registry.PC_SoundRegistry;
import powercraft.api.renderer.PC_Renderer;

public class PCws_WeaselPluginInfoCore extends PCws_WeaselPluginInfo {

	private static PCws_WeaselModelCore model = new PCws_WeaselModelCore();
	private static Random rand = new Random();
	
	public PCws_WeaselPluginInfoCore() {
		super(PCws_WeaselPluginCore.class, "Weasel Core", "weasel_core_top", "weasel_core_side");
	}

	@Override
	public void renderInventoryBlock(Block block, Object renderer) {
		float px = 0.0625F;
		//floor
		block.setBlockBounds(0, 0, 0, 16 * px, 3 * px, 16 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new Icon[] { icons[0], icons[2], icons[1], icons[1], icons[1], icons[1]});

		//chip
		block.setBlockBounds(4 * px, 3 * px, 3 * px, 12 * px, 5 * px, 13 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new Icon[] { null, icons[4], icons[5], icons[5], icons[5], icons[5] });
		block.setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	@Override
	public PCws_WeaselModelBase getModel() {
		return model;
	}

	@Override
	public void getServerMsg(PCws_TileEntityWeasel te, String msg, Object obj) {
		if(msg.equalsIgnoreCase("bell")){
			PC_SoundRegistry.playSound(te.xCoord + 0.5D, te.yCoord + 0.5D, te.zCoord + 0.5D, "random.orb", 0.8F,
					(rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
		te.getWorldObj().spawnParticle("note", te.xCoord + 0.5D, te.yCoord + 0.3D, te.zCoord + 0.5D, (Double)obj,
				0.0D, 0.0D);
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
