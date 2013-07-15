package powercraft.api.gres;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;

public interface PC_IGresGuiOpenHandler {
	
	@SideOnly(Side.CLIENT)
	public PC_IGresClient openClientGui(EntityPlayer player);
	
	public PC_GresBaseWithInventory openServerGui(EntityPlayer player);
	
}
