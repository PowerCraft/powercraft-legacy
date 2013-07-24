package weasel.compiler;


public class WeaselSyntaxError extends WeaselCompilerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1435374279085577987L;

	public WeaselSyntaxError(int line, String error, Object...o) {
		super(line, error, o);
	}
	
}
