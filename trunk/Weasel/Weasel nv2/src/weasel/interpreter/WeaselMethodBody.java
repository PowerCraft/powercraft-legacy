package weasel.interpreter;

import java.io.DataInputStream;

import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselMethodBody {

	protected final WeaselMethod method;
	protected final WeaselClass parentClass;
	protected WeaselInstruction[] instructions;
	
	protected WeaselMethodBody(WeaselMethod method, WeaselClass parentClass){
		this.method = method;
		this.parentClass = parentClass;
	}
	
	protected WeaselMethodBody(WeaselMethod method, WeaselClass parentClass, DataInputStream dataInputStream) {
		this.method = method;
		this.parentClass = parentClass;
	}

	public WeaselMethod getMethod(){
		return method;
	}
	
	public WeaselClass getParentClass(){
		return parentClass;
	}
	
	public WeaselInstruction getInstruction(int i) {
		if(i>=instructions.length || i<0)
			return null;
		return instructions[i];
	}

	public String getNameAndDesk() {
		return parentClass.getRealName()+"."+method.getNameAndDesk();
	}

	public boolean isNative(){
		return instructions ==null;
	}
	
}
