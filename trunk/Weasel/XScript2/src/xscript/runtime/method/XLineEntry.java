package xscript.runtime.method;


public class XLineEntry {

	private int from;
	private int line;
	
	public XLineEntry(int from, int line){
		this.from = from;
		this.line = line;
	}
	
	public int getFrom() {
		return from;
	}

	public int getLine() {
		return line;
	}
	
}
