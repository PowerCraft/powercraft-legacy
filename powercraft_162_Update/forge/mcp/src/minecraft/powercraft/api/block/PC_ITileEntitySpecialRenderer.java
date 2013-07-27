package powercraft.api.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface PC_ITileEntitySpecialRenderer {

	@SideOnly(Side.CLIENT)
	public void renderTileEntityAt(float timeStamp);
	
}
