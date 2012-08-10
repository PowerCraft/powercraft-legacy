package net.minecraft.src;

public class PClo_GuiCallerSensor implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 10;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		return new PClo_GuiSensor((PClo_TileEntitySensor) new PC_CoordI(var2, var3, var4).getTileEntity(player.worldObj));
	}

}
