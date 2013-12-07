package xscript.runtime.method;

import xscript.runtime.genericclass.XClassPtr;

public class XCatchEntry {

	private int from;
	private int to;
	private int error;
	private XClassPtr type;
	
	public XCatchEntry(int from, int to, int error, XClassPtr type) {
		this.from = from;
		this.to = to;
		this.error = error;
		this.type = type;
	}

	public int getFrom(){
		return from;
	}
	
	public int getTo(){
		return to;
	}
	
	public boolean isIn(int programPointer) {
		return from<=programPointer && to>=programPointer;
	}

	public int getJumpPos() {
		return error;
	}

	public XClassPtr getType() {
		return type;
	}
	
}
