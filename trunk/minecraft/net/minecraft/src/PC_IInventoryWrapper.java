package net.minecraft.src;

/**
 * Interface for tile entities which have inventory, but are not implementing it directly. Instead they provide different IInventory
 * instance on demand.
 * 
 * @author MightyPork
 */
public interface PC_IInventoryWrapper {
	/**
	 * Get currently available inventory.<br>
	 * Implementing classes can return themselves if they implement IInventory, or any other valid IInventory.<br>
	 * Return null if no inventory is available from this tile instance.
	 * 
	 * @return inventory, or null.
	 */
	public abstract IInventory getInventory();
}
