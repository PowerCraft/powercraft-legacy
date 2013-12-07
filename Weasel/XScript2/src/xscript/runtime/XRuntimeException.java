package xscript.runtime;

public class XRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 720783362120453486L;

	public XRuntimeException(String message, Object...args){
		super(String.format(message, args));
	}

	public XRuntimeException(Throwable e, String message, Object...args) {
		super(String.format(message, args), e);
	}
	
}
