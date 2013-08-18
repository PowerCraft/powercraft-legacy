package weasel.interpreter;

public class WeaselRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 5813434606758913978L;

	public WeaselRuntimeException(String message, WeaselThread weaselThread) {
		super(message);
		setStackTrace(weaselThread.getStackTrace());
	}

	public WeaselRuntimeException(Throwable e, WeaselThread weaselThread) {
		super(e);
		setStackTrace(weaselThread.getStackTrace());
	}
	
}
