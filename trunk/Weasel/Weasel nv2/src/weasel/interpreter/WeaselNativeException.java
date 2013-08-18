package weasel.interpreter;


public class WeaselNativeException extends RuntimeException {

	private static final long serialVersionUID = 9017682348769351928L;

	public WeaselNativeException(String message, Object...obj){
		super(String.format(message, obj));
	}

	public WeaselNativeException(Throwable e) {
		super(e);
	}
	
	public WeaselNativeException(Throwable e, String message, Object...obj) {
		super(String.format(message, obj), e);
	}
	
}
