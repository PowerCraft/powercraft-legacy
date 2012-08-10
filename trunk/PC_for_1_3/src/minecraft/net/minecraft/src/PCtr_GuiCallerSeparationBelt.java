package net.minecraft.src;

public class PCtr_GuiCallerSeparationBelt implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 14;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		PCtr_TileEntitySeparationBelt te = (PCtr_TileEntitySeparationBelt) player.worldObj.getBlockTileEntity(var2, var3, var4);
		return new PCtr_GuiSeparationBelt(player, te);
	}

}
