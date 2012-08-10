package net.minecraft.src;

public class PCtr_GuiCallerTeleporter implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 19;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		PCtr_TileEntityTeleporter te = (PCtr_TileEntityTeleporter) player.worldObj.getBlockTileEntity(var2, var3, var4);
		return new PCtr_GuiTeleporter(te, true);
	}

}
