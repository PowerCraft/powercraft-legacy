package net.minecraft.src;

public class PCma_GuiCallerRoaster implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 12;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		PCma_TileEntityRoaster tileentity = (PCma_TileEntityRoaster) player.worldObj.getBlockTileEntity(var2, var3, var4);
		if (tileentity != null) {
			return new PCma_GuiRoaster(player, tileentity);
		}
		return null;
	}

}
