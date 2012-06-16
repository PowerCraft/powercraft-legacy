package net.minecraft.src;

/**
 * Interface used by the chest detection gates to read automatic workbench's
 * state.
 * 
 * @author MightyPork
 */
public interface PC_IStateReportingInventory {

	/**
	 * @return is this container is empty?
	 */
	public abstract boolean isContainerEmpty();

	/**
	 * @return is this container full?
	 */
	public abstract boolean isContainerFull();

}
