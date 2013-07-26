package powercraft.weasel.exception;


/**
 * Weasel runtime exception thrown when instruction list reached end of scope
 * without RETURN or END
 * 
 * @author MightyPork
 */
public class EndOfProgramException extends WeaselRuntimeException {

	/**
	 * EOS
	 */
	public EndOfProgramException() {
		super("End of an instruction list reached.");
	}

}
