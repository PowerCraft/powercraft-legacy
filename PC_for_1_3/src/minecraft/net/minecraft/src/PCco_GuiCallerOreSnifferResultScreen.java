package net.minecraft.src;

public class PCco_GuiCallerOreSnifferResultScreen implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 2;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		// TODO direction
		return new PCco_GuiOreSnifferResultScreen(player, player.worldObj, new PC_CoordI(var2, var3, var4), new PC_CoordI(1, 0, 0));
	}

}
