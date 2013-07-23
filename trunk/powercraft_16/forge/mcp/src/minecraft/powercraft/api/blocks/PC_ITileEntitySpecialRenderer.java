package powercraft.api.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface PC_ITileEntitySpecialRenderer {

	@SideOnly(Side.CLIENT)
	public void renderTileEntityAt(double x, double y, double z, float timeStamp);
	
}
