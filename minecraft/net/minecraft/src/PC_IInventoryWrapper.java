package net.minecraft.src;

/**
 * Interface for tile entities which have inventory, but are not implementing it directly. Instead they provide different IInventory instance on demand.
 * 
 * @author MightyPork
 */
public interface PC_IInventoryWrapper {
	/**
	 * Get currently available inventory.
	 * @return inventory, or null.
	 */
	public abstract IInventory getInventory();
}
