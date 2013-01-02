package powercraft.weasel;

import net.minecraft.block.Block;
import powercraft.management.PC_Renderer;
import powercraft.management.PC_Utils.ValueWriting;

public class PCws_WeaselPluginInfoDiskDrive extends PCws_WeaselPluginInfo {
	
	private static PCws_WeaselModelDiskDrive model = new PCws_WeaselModelDiskDrive();
	
	public PCws_WeaselPluginInfoDiskDrive() {
		super(PCws_WeaselPluginDiskDrive.class, "Weasel Disk Drive");
	}

	@Override
	public void renderInventoryBlock(Block block, Object renderer) {
		PC_Renderer.swapTerrain(block);

		float px = 0.0625F;
		ValueWriting.setBlockBounds(block, 0, 0, 0, 16 * px, 13 * px, 16 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new int[] { 230, 225, 211, 211, 211, 211 });
		ValueWriting.setBlockBounds(block, 0, 0, 0, 1, 1, 1);
		PC_Renderer.resetTerrain(true);
	}

	@Override
	public float[] getBounds() {
		return new float[] { 0, 0, 0, 1, 1 - 2 * 0.0625F, 1 };
	}
	
	@Override
	public PCws_WeaselModelBase getModel() {
		return model;
	}
	
	@Override
	public int inventorySize(){
		return 8;
	}
	
	@Override
	public int inventoryStackLimit(){
		return 1;
	}
	
}

