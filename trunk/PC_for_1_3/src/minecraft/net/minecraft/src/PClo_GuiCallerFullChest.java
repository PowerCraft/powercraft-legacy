package net.minecraft.src;

public class PClo_GuiCallerFullChest implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 7;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		TileEntity te = player.worldObj.getBlockTileEntity(var2, var3, var4);
		if (te == null) {
			return null;
		}
		PClo_TileEntityGate teg = (PClo_TileEntityGate)te;
		return new PClo_GuiFullChest(teg);
	}

}
