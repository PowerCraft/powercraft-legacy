package net.minecraft.src;

public class PCde_GuiCallerTransmutator implements PC_IGresGuiCaller {

	@Override
	public int getGuiID() {
		return 5;
	}

	@Override
	public PC_IGresBase createGui(EntityPlayer player, int var2, int var3,
			int var4) {
		PCde_TileEntityDeco tileentity = (PCde_TileEntityDeco) player.worldObj.getBlockTileEntity(var2, var3, var4);
		if (tileentity != null) {
			if (tileentity.type == 3 && tileentity.getInventory() != null) {
				return new PCde_GuiTransmutator(player, (PCde_InventoryTransmutationContainer)tileentity.getInventory());
			}
		}
		return null;
	}

}
