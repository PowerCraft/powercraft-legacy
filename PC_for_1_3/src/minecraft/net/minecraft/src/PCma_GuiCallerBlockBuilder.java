package net.minecraft.src;

public class PCma_GuiCallerBlockBuilder implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 11;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		PCma_TileEntityBlockBuilder tileentity = (PCma_TileEntityBlockBuilder) player.worldObj.getBlockTileEntity(var2, var3, var4);
		if (tileentity != null) {
			return new PCma_GuiBlockBuilder(player, tileentity);
		}
		return null;
	}

}
