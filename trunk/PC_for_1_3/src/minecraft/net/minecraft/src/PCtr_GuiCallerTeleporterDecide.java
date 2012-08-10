package net.minecraft.src;

public class PCtr_GuiCallerTeleporterDecide implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 20;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		PCtr_TileEntityTeleporter te = (PCtr_TileEntityTeleporter) player.worldObj.getBlockTileEntity(var2, var3, var4);
		if (te == null) {
			return null;
		}
		return new PCtr_GuiTeleporterDecide(te);
	}

}
