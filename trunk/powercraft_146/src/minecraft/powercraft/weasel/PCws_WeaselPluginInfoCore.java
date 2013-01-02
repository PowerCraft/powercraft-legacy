package powercraft.weasel;

import java.util.Random;

import net.minecraft.block.Block;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.ValueWriting;

public class PCws_WeaselPluginInfoCore extends PCws_WeaselPluginInfo {

	private static PCws_WeaselModelCore model = new PCws_WeaselModelCore();
	private static Random rand = new Random();
	
	public PCws_WeaselPluginInfoCore() {
		super(PCws_WeaselPluginCore.class, "Weasel Core");
	}

	@Override
	public void renderInventoryBlock(Block block, Object renderer) {
		float px = 0.0625F;
		PC_Renderer.swapTerrain(block);
		//floor
		ValueWriting.setBlockBounds(block, 0, 0, 0, 16 * px, 3 * px, 16 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new int[] { 6, 224, 5, 5, 5, 5 });

		//chip
		ValueWriting.setBlockBounds(block, 4 * px, 3 * px, 3 * px, 12 * px, 5 * px, 13 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new int[] { 0, 196, 195, 195, 195, 195 });
		ValueWriting.setBlockBounds(block, 0, 0, 0, 1, 1, 1);
		PC_Renderer.resetTerrain(true);
	}

	@Override
	public PCws_WeaselModelBase getModel() {
		return model;
	}

	@Override
	public void getServerMsg(PCws_TileEntityWeasel te, String msg, Object obj) {
		if(msg.equalsIgnoreCase("bell")){
			ValueWriting.playSound(te.xCoord + 0.5D, te.yCoord + 0.5D, te.zCoord + 0.5D, "random.orb", 0.8F,
					(rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
		te.getWorldObj().spawnParticle("note", te.xCoord + 0.5D, te.yCoord + 0.3D, te.zCoord + 0.5D, (Double)obj,
				0.0D, 0.0D);
		}
	}
	
}
