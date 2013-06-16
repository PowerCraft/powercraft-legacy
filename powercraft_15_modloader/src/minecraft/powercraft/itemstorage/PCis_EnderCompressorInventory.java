package powercraft.itemstorage;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import powercraft.api.utils.PC_VecI;

public class PCis_EnderCompressorInventory extends PCis_CompressorInventory {

	public PCis_EnderCompressorInventory(EntityPlayer player, int equipment) {
		super(player, equipment, new PC_VecI(9, 3));
	}

	@Override
	public int getSizeInventory() {
		return player.getInventoryEnderChest().getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return player.getInventoryEnderChest().getStackInSlot(var1);
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		return player.getInventoryEnderChest().decrStackSize(var1, var2);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return player.getInventoryEnderChest().getStackInSlotOnClosing(var1);
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		player.getInventoryEnderChest().setInventorySlotContents(var1, var2);
	}

	@Override
	public int getInventoryStackLimit() {
		return player.getInventoryEnderChest().getInventoryStackLimit();
	}

	@Override
	public void openChest() {
		player.getInventoryEnderChest().openChest();
	}

	@Override
	public void closeChest() {
		player.getInventoryEnderChest().closeChest();
	}
	
}
