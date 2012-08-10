package net.minecraft.src;

public class PCma_GuiCallerXPBank implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 17;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		PCma_TileEntityXPBank tex = (PCma_TileEntityXPBank)player.worldObj.getBlockTileEntity(var2, var3, var4);
		return new PCma_GuiXPBank(tex);
	}

}
