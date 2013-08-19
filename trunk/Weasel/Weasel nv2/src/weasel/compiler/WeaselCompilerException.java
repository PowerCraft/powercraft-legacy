package weasel.compiler;

public class WeaselCompilerException extends Exception {

	private static final long serialVersionUID = -8433372419975005389L;

	private int line;
	
	public WeaselCompilerException(int line, String message, Object ... obj) {
		super(String.format(message, obj));
		this.line = line;
	}

	public int getLine() {
		return line;
	}

}
