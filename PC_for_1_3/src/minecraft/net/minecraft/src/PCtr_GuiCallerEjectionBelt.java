package net.minecraft.src;

public class PCtr_GuiCallerEjectionBelt implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 18;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		PCtr_TileEntityEjectionBelt te = (PCtr_TileEntityEjectionBelt) player.worldObj.getBlockTileEntity(var2, var3, var4);
		return new PCtr_GuiEjectionBelt(te);
	}

}
