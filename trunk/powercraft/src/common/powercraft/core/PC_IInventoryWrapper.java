package powercraft.core;

import net.minecraft.src.IInventory;

public interface PC_IInventoryWrapper {
	/**
	 * Get currently available inventory.<br>
	 * Implementing classes can return themselves if they implement IInventory,
	 * or any other valid IInventory.<br>
	 * Return null if no inventory is available from this tile instance.
	 * 
	 * @return inventory, or null.
	 */
	public abstract IInventory getInventory();
}
