package weasel.compiler;

import weasel.interpreter.WeaselGenericClass;

public class WeaselCompilerReturn {

	public final WeaselInstructionList instructions;
	public final WeaselGenericClass returnType;
	public final boolean isClassAccess;
	
	public WeaselCompilerReturn(WeaselInstructionList instructions, WeaselGenericClass returnType) {
		this.instructions = instructions;
		this.returnType = returnType;
		isClassAccess = false;
	}
	
	public WeaselCompilerReturn(WeaselInstructionList instructions, WeaselGenericClass returnType, boolean isClassAccess) {
		this.instructions = instructions;
		this.returnType = returnType;
		this.isClassAccess = isClassAccess;
	}
	
}
