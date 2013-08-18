package weasel.compiler;



public class WeaselSyntaxError extends WeaselCompilerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1435374279085577987L;

	public WeaselSyntaxError(int line, String error, Object...o) {
		super("Weasel Compiler error at line "+line+":"+String.format(error, o));
	}

	public WeaselSyntaxError(int line, Throwable e) {
		super(e, "Weasel Compiler error at line "+line);
	}
	
}
