package xscript.runtime.method;

import xscript.runtime.genericclass.XClassPtr;

public class XLocalEntry {

	private int from;
	private int to;
	private int index;
	private int modifier;
	private String name;
	private XClassPtr type;
	
	public XLocalEntry(int from, int to, int index, int modifier, String name, XClassPtr type) {
		this.from = from;
		this.to = to;
		this.index = index;
		this.name = name;
		this.type = type;
		this.modifier = modifier;
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
	
	public int getIndex() {
		return index;
	}
	
	public int getModifier() {
		return modifier;
	}
	
	public String getName() {
		return name;
	}

	public XClassPtr getType() {
		return type;
	}
	
}
