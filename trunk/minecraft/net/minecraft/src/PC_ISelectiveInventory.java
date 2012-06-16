package net.minecraft.src;

/**
 * Inventory which can't store some items.<br>
 * Used by conveyers to determine whether it's the right slot to fill.
 * 
 * @author MightyPork
 */
public interface PC_ISelectiveInventory {
	/**
	 * Can put this stack into this slot?
	 * 
	 * @param slot slot number
	 * @param stack stack to store
	 * @return can store it here
	 */
	public boolean canInsertStackTo(int slot, ItemStack stack);
}
