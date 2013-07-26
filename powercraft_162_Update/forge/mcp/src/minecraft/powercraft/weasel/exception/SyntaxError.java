package powercraft.weasel.exception;


/**
 * Syntax error generated during compilation.
 * 
 * @author MightyPork
 */
public class SyntaxError extends Exception {
	/**
	 * @param cause description of what caused the error
	 */
	public SyntaxError(String cause) {
		super(cause);
	}
}
