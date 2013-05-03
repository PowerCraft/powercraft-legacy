package powercraft.weasel;

import net.minecraft.util.Icon;
import powercraft.api.renderer.PC_Renderer;
import powercraft.api.utils.PC_Direction;

public class PCws_WeaselPluginInfoDiskDrive extends PCws_WeaselPluginInfo {
	
	private static PCws_WeaselModelDiskDrive model = new PCws_WeaselModelDiskDrive();
	
	public PCws_WeaselPluginInfoDiskDrive() {
		super(PCws_WeaselPluginDiskDrive.class, "Weasel Disk Drive", "weasel_diskdrive_down", "weasel_diskdrive_top", "weasel_diskdrive_side");
	}

	@Override
	public void renderInventoryBlock(PCws_BlockWeasel block, Object renderer) {

		float px = 0.0625F;
		block.setWeaselBlockBounds(0, 0, 0, 16 * px, 13 * px, 16 * px);
		PC_Renderer.renderInvBoxWithTextures(renderer, block, new Icon[] { icons[4], icons[5], icons[6], icons[6], icons[6], icons[6] });
		block.setWeaselBlockBounds(0, 0, 0, 1, 1, 1);
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
	
	@Override
	public Icon getTexture(PC_Direction side) {
		if(side==PC_Direction.TOP){
			return icons[5];
		}else if(side==PC_Direction.BOTTOM){
			return icons[4];
		}else{
			return icons[6];
		}
	}
	
}

