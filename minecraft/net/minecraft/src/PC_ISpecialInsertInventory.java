package net.minecraft.src;

/**
 * Inventory with special method for inserting stacks.
 * Used mostly by conveyors to put stuff into it.
 * 
 * @author MightyPork
 */
public interface PC_ISpecialInsertInventory {
	/**
	 * Try to put stack into the inventory
	 * 
	 * @param stack stack to store
	 * @return stored completely
	 */
	public boolean insertStackIntoInventory(ItemStack stack);
	
	/**
	 * Hook which should be called after insertStackIntoInventory
	 * 
	 * @param stack stack to store
	 */
	public void onStackInserted();
}
