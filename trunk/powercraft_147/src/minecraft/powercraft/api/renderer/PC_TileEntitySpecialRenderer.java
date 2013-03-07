package powercraft.api.renderer;

import powercraft.api.tileentity.PC_ITileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class PC_TileEntitySpecialRenderer extends TileEntitySpecialRenderer {
	
	private static PC_TileEntitySpecialRenderer instance = null;
	
	public static TileEntitySpecialRenderer getInstance() {
		if(instance==null)
			instance = new PC_TileEntitySpecialRenderer();
		return instance;
	}
	
	private PC_TileEntitySpecialRenderer(){
		
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float rot) {
		if(tileEntity instanceof PC_ITileEntityRenderer){
			((PC_ITileEntityRenderer)tileEntity).renderTileEntityAt(x, y, z, rot);
		}
	}

}
