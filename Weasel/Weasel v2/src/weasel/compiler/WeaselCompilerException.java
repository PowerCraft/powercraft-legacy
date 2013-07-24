package weasel.compiler;

import weasel.interpreter.WeaselException;

public class WeaselCompilerException extends WeaselException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8626423438590341183L;

	public WeaselCompilerException(int line, String error, Object...o){
		super("Weasel Compiler error at line "+line+":"+String.format(error, o));
	}

	public WeaselCompilerException(int line, Throwable e) {
		super("Weasel Compiler error at line "+line, e);
	}

}
