package net.minecraft.src;


/**
 * TRASH SLOT! Holy cow!
 * 
 * @author MightyPork
 */
public class PC_SlotTrash extends Slot {

	/**
	 * blackhole
	 */
	public PC_SlotTrash() {
		super(null, 0, 0, 0);
	}

	@Override
	public void onSlotChange(ItemStack par1ItemStack, ItemStack par2ItemStack) {}

	@Override
	protected void onCrafting(ItemStack itemstack, int i) {}

	@Override
	protected void onCrafting(ItemStack itemstack) {}

	@Override
	public void onPickupFromSlot(ItemStack par1ItemStack) {}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {
		return true;
	}

	@Override
	public ItemStack getStack() {
		return null;
	}

	/**
	 * Returns if this slot contains a stack.
	 */
	@Override
	public boolean getHasStack() {
		return false;
	}

	/**
	 * Helper method to put a stack in the slot.
	 */
	@Override
	public void putStack(ItemStack par1ItemStack) {}

	/**
	 * Called when the stack in a Slot changes
	 */
	@Override
	public void onSlotChanged() {}

	/**
	 * Returns the maximum stack size for a given slot (usually the same as
	 * getInventoryStackLimit(), but 1 in the case of armor slots)
	 */
	@Override
	public int getSlotStackLimit() {
		return 128;
	}

	/**
	 * Returns the icon index on items.png that is used as background image of
	 * the slot.
	 */
	@Override
	public int getBackgroundIconIndex() {
		return -1;
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of
	 * the second int arg. Returns the new stack.
	 */
	@Override
	public ItemStack decrStackSize(int par1) {
		return null;
	}
}
