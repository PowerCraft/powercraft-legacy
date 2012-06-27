package net.minecraft.src.weasel.exception;


/**
 * Weasel runtime exception thrown when instruction list reached end of scope without RETURN or END
 * 
 * @author MightyPork
 *
 */
public class EndOfScopeException extends WeaselRuntimeException {

	/**
	 * EOS
	 */
	public EndOfScopeException() {
		super("End of an instruction list reached.");
	}

}
