package net.minecraft.src;

public class PClo_GuiCallerPulsar implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 8;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		PClo_TileEntityPulsar ent = (PClo_TileEntityPulsar) player.worldObj.getBlockTileEntity(var2, var3, var4);
		if (ent != null) {
			return new PClo_GuiPulsar(ent);
		}
		return null;
	}

}
