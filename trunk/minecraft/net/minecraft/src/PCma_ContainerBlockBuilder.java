package net.minecraft.src;


/**
 * Block Builder's container
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCma_ContainerBlockBuilder extends Container {

	private IInventory builderInventory;

	/**
	 * @param playerinventory player inventory
	 * @param builderInventory builder inventory (tile entity)
	 */
	public PCma_ContainerBlockBuilder(IInventory playerinventory, IInventory builderInventory) {
		this.builderInventory = builderInventory;
		for (int i = 0; i < 3; i++) {
			for (int l = 0; l < 3; l++) {
				addSlot(new Slot(builderInventory, l + i * 3, 62 + l * 18, 17 + i * 18));
			}

		}

		for (int j = 0; j < 3; j++) {
			for (int i1 = 0; i1 < 9; i1++) {
				addSlot(new Slot(playerinventory, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
			}

		}

		for (int k = 0; k < 9; k++) {
			addSlot(new Slot(playerinventory, k, 8 + k * 18, 142));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return builderInventory.isUseableByPlayer(entityplayer);
	}

	@Override
	public ItemStack transferStackInSlot(int i) {
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(i);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (i < 9) {
				if (!mergeItemStack(itemstack1, 9, 45, true)) { return null; }
			} else if (!mergeItemStack(itemstack1, 0, 9, false)) { return null; }
			if (itemstack1.stackSize == 0) {
				slot.putStack(null);
			} else {
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize != itemstack.stackSize) {
				slot.onPickupFromSlot(itemstack1);
			} else {
				return null;
			}
		}
		return itemstack;
	}
}
