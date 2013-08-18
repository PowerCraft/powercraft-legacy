package weasel.compiler;

import weasel.interpreter.WeaselClass;

public class WeaselClassException extends WeaselCompilerException {

	private static final long serialVersionUID = 7422003611812573371L;

	public WeaselClassException(WeaselClass wealseClass, Throwable e) {
		super(e, wealseClass.getRealName());
	}

}
