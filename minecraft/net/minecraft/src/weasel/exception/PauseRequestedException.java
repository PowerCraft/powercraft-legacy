package net.minecraft.src.weasel.exception;


/**
 * Signal exception telling WeaselEngine to pause execution after this
 * instruction.
 * 
 * @author MightyPork
 */
public class PauseRequestedException extends WeaselRuntimeException {

	/**
	 * Signal exception telling WeaselEngine to pause execution after this
	 * instruction.
	 */
	public PauseRequestedException() {
		super("Program pause is requested.");
	}

}
