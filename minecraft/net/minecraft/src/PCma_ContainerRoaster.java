package net.minecraft.src;

/**
 * Roaster's container
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCma_ContainerRoaster extends Container {

	private IInventory roasterInventory;
	private int numRows;

	/**
	 * @param playerinv
	 * @param roasterinv
	 */
	public PCma_ContainerRoaster(IInventory playerinv, IInventory roasterinv) {
		roasterInventory = roasterinv;
		numRows = roasterinv.getSizeInventory() / 9;
		roasterinv.openChest();
		int i = (numRows - 4) * 18;
		for (int j = 0; j < numRows; j++) {
			for (int i1 = 0; i1 < 9; i1++) {
				addSlot(new Slot(roasterinv, i1 + j * 9, 8 + i1 * 18, 18 + j * 18));
			}

		}

		for (int k = 0; k < 3; k++) {
			for (int j1 = 0; j1 < 9; j1++) {
				addSlot(new Slot(playerinv, j1 + k * 9 + 9, 8 + j1 * 18, 103 + k * 18 + i));
			}

		}

		for (int l = 0; l < 9; l++) {
			addSlot(new Slot(playerinv, l, 8 + l * 18, 161 + i));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return roasterInventory.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(int i) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (i < numRows * 9) {
				if (!mergeItemStack(itemstack1, numRows * 9, inventorySlots.size(), true)) { return null; }
			} else if (!mergeItemStack(itemstack1, 0, numRows * 9, false)) { return null; }
			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}

	private int getLimit(int a, boolean flag) {
		if (flag) { return a; }
		return a < roasterInventory.getInventoryStackLimit() ? a : roasterInventory.getInventoryStackLimit();
	}

	@Override
	protected boolean mergeItemStack(ItemStack itemstack, int i, int j, boolean flag) {
		boolean flag1 = false;
		int k = i;
		if (flag) {
			k = j - 1;
		}
		if (itemstack.isStackable()) {
			while (itemstack.stackSize > 0 && (!flag && k < j || flag && k >= i)) {
				Slot slot = (Slot) inventorySlots.get(k);
				ItemStack itemstack1 = slot.getStack();
				if (itemstack1 != null && (flag || itemstack1.stackSize < roasterInventory.getInventoryStackLimit())
						&& itemstack1.itemID == itemstack.itemID
						&& (!itemstack.getHasSubtypes() || itemstack.getItemDamage() == itemstack1.getItemDamage())) {
					int i1 = itemstack1.stackSize + itemstack.stackSize;
					if (i1 <= getLimit(itemstack.getMaxStackSize(), flag))
					{
						itemstack.stackSize = 0;
						itemstack1.stackSize = i1;
						slot.onSlotChanged();
						flag1 = true;
					} else if (itemstack1.stackSize < getLimit(itemstack.getMaxStackSize(), flag)) {
						itemstack.stackSize -= getLimit(itemstack.getMaxStackSize(), flag) - itemstack1.stackSize;
						itemstack1.stackSize = getLimit(itemstack.getMaxStackSize(), flag);
						slot.onSlotChanged();
						flag1 = true;
					}
				}
				if (flag) {
					k--;
				} else {
					k++;
				}
			}
		}
		if (itemstack.stackSize > 0) {
			int l;
			if (flag) {
				l = j - 1;
			} else {
				l = i;
			}
			do {
				if ((flag || l >= j) && (!flag || l < i)) {
					break;
				}
				Slot slot1 = (Slot) inventorySlots.get(l);
				ItemStack itemstack2 = slot1.getStack();
				if (itemstack2 == null) {
					// empty field in inventory
					ItemStack toStore = itemstack.copy();
					toStore.stackSize = getLimit(toStore.stackSize, flag);
					itemstack.stackSize -= toStore.stackSize;

					slot1.putStack(toStore);
					slot1.onSlotChanged();

					if (itemstack.stackSize <= 0) {
						flag1 = true;
						itemstack.stackSize = 0;
						break;
					}
				}
				if (flag) {
					l--;
				} else {
					l++;
				}
			} while (true);
		}
		return flag1;
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer entityplayer) {
		super.onCraftGuiClosed(entityplayer);
		roasterInventory.closeChest();
	}

}
