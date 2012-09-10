package net.minecraft.src.PCnt;

import net.minecraft.src.PC_CoordF;
import net.minecraft.src.PC_Renderer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;


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
		PC_TeleporterData td = PCnt_TeleporterManager.getTeleporterDataAt(tet.xCoord, tet.yCoord, tet.zCoord);
		
		if(td!=null){
			if (!td.hideLabel) {
	
				String foo = "\u2192" + td.getName();
	
				PC_Renderer.renderEntityLabelAt(foo, new PC_CoordF(tet.xCoord, tet.yCoord, tet.zCoord), 10, 1.3F, x, y, z);
			}
		}
	}
}
