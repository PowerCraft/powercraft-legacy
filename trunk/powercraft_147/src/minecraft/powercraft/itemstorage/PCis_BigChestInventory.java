package powercraft.itemstorage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class PCis_BigChestInventory implements IInventory {

	private ItemStack inv[] = new ItemStack[100];
	
	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return inv[var1];
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		if (inv[var1] != null) {
			if (inv[var1].stackSize <= var2) {
				ItemStack itemstack = inv[var1];
				inv[var1] = null;
				onInventoryChanged();
				return itemstack;
			}
			ItemStack itemstack1 = inv[var1].splitStack(var2);
			if (inv[var1].stackSize == 0) {
				inv[var1] = null;
			}
			onInventoryChanged();
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return inv[var1];
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		inv[var1] = var2;
	}

	@Override
	public String getInvName() {
		return "BigChestInventory";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void onInventoryChanged() {}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return false;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

}
