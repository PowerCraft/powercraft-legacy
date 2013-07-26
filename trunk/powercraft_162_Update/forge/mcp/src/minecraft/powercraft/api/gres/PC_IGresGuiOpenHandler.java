package powercraft.api.gres;


import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public interface PC_IGresGuiOpenHandler {

	@SideOnly(Side.CLIENT)
	public PC_IGresClient openClientGui(EntityPlayer player);


	public PC_GresBaseWithInventory openServerGui(EntityPlayer player);

}
