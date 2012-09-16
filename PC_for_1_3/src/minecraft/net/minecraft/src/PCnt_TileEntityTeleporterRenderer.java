package net.minecraft.src;



/**
 * Renderer for teleporter - the "label"
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCnt_TileEntityTeleporterRenderer extends TileEntitySpecialRenderer {


	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f0) {

		PCnt_TileEntityTeleporter tet = (PCnt_TileEntityTeleporter) tileEntity;
		PCnt_TeleporterData td = PCnt_TeleporterManager.getTeleporterDataAt(tet.dimension, tet.xCoord, tet.yCoord, tet.zCoord);
		
		if(td!=null){
			if (!td.hideLabel) {
	
				String foo = "\u2192" + td.getName();
	
				PC_Renderer.renderEntityLabelAt(foo, new PC_CoordF(tet.xCoord, tet.yCoord, tet.zCoord), 10, 1.3F, x, y, z);
			}
		}
	}
}
