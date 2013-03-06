package powercraft.weasel.exception;


/**
 * Random exception generated during execution of a Weasel program
 * 
 * @author MightyPork
 */
public class WeaselRuntimeException extends RuntimeException {

	/**
	 * Random exception generated during execution of a Weasel program
	 * 
	 * @param message what caused this exception
	 */
	public WeaselRuntimeException(String message) {
		super(message);
	}

	/**
	 * Other runtime exception wrapped into WRE
	 * 
	 * @param e
	 */
	public WeaselRuntimeException(Exception e) {
		super(e);
	}

}
