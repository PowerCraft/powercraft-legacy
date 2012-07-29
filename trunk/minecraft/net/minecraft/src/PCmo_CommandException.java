package net.minecraft.src;


/**
 * Exception thrown during program parsing
 * 
 * @author MightyPork
 */
public class PCmo_CommandException extends Exception {
	/**
	 * generic error
	 */
	public PCmo_CommandException() {
		super("Unknown parse error.");
	}

	/**
	 * @param err error description (human readable)
	 */
	public PCmo_CommandException(String err) {
		super(err);
	}
}