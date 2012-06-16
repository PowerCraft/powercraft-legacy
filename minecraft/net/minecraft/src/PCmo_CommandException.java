package net.minecraft.src;

/**
 * Exception thrown during program parsing
 * 
 * @author MightyPork
 */
public class PCmo_CommandException extends Exception {

	String msg;

	public PCmo_CommandException() {
		super(); // call superclass constructor
		msg = "unknown";
	}

	/**
	 * @param err error description (human readable)
	 */
	public PCmo_CommandException(String err) {
		super(err);
		msg = err;
	}

	/**
	 * Get the error message
	 * 
	 * @return what happened (human readable)
	 */
	public String getError() {
		return msg;
	}
}