package net.minecraft.src;

public class PCma_GuiCallerAutomaticWorkbench implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		PCma_TileEntityAutomaticWorkbench inventory = (PCma_TileEntityAutomaticWorkbench)player.worldObj.getBlockTileEntity(var2, var3, var4);
		return new PCma_GuiAutomaticWorkbench(player, inventory);
	}

}
