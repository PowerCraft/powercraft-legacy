package net.minecraft.src;

public class PCma_GuiCallerLaserTypeDecide implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 15;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		PCma_TileEntityLaser te = (PCma_TileEntityLaser) player.worldObj.getBlockTileEntity(var2, var3, var4);
		return new PCma_GuiLaserTypeDecide(te);
	}

}
