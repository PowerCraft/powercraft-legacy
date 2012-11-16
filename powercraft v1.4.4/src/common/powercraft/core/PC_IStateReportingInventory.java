package powercraft.core;

import net.minecraft.src.ItemStack;

public interface PC_IStateReportingInventory {
	/**
	 * @return is this container is empty?
	 */
	public abstract boolean isContainerEmpty();

	/**
	 * @return is this container full?
	 */
	public abstract boolean isContainerFull();

	/**
	 * @return is this container full? - only check for null slots
	 */
	public abstract boolean hasContainerNoFreeSlots();

	
	public abstract boolean hasInventoryPlaceFor(ItemStack itemStack);

	public abstract boolean isContainerEmptyOf(ItemStack itemStack);
}
