package net.minecraft.src;

/**
 * Inventory with special method for inserting stacks.
 * Used mostly by conveyors to put stuff into it.
 * 
 * @author MightyPork
 */
public interface PC_ISpecialAccessInventory {

	/**
	 * Try to put stack into the inventory.<br>
	 * The inventory will select the right slot itself.
	 * 
	 * @param stack stack to store
	 * @return stored completely
	 */
	public boolean insertStackIntoInventory(ItemStack stack);

	/**
	 * @return true if you must use "insertStackIntoInventory" to store something.
	 */
	public boolean needsSpecialInserter();


	/**
	 * Can we put this stack into this slot?<br>
	 * For the insertion itself, use methods from PC_InvUtils.
	 * 
	 * @param slot slot number
	 * @param stack stack to store
	 * @return can store it here
	 */
	public boolean canInsertStackTo(int slot, ItemStack stack);

	/**
	 * Can stack in this slot be dispensed (ejected by conveyor, for example)?
	 * 
	 * @param slot slot number
	 * @return can dispense this stack
	 */
	public boolean canDispenseStackFrom(int slot);

}
