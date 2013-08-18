package weasel.compiler;

public class WeaselCompilerException extends Exception {

	private static final long serialVersionUID = -8433372419975005389L;

	public WeaselCompilerException(String message, Object ... obj) {
		super(String.format(message, obj));
	}
	
	public WeaselCompilerException(Throwable e, String message, Object ... obj) {
		super(String.format(message, obj), e);
	}

}
