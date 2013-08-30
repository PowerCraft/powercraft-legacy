package weasel.compiler;

import java.util.List;

import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselCompilerReturn {

	public final List<WeaselInstruction> instructions;
	public final WeaselGenericClass returnType;
	public final boolean isClassAccess;
	
	public WeaselCompilerReturn(List<WeaselInstruction> instructions, WeaselGenericClass returnType) {
		this.instructions = instructions;
		this.returnType = returnType;
		isClassAccess = false;
	}
	
	public WeaselCompilerReturn(List<WeaselInstruction> instructions, WeaselGenericClass returnType, boolean isClassAccess) {
		this.instructions = instructions;
		this.returnType = returnType;
		this.isClassAccess = isClassAccess;
	}
	
}
