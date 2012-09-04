package net.minecraft.src;

public class PCma_GuiCallerReplacer implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 16;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		PCma_TileEntityReplacer tileentity = (PCma_TileEntityReplacer) player.worldObj.getBlockTileEntity(var2, var3, var4);
		if (tileentity != null) {
			return new PCma_GuiReplacer(tileentity, player);
		}
		return null;
	}

}
