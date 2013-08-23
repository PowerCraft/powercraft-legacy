package weasel.compiler.equationSolverNew;

import java.util.List;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselCompileReturn {
	
	public final List<WeaselInstruction> instructions;
	public final WeaselClass returnType;
	
	public WeaselCompileReturn(List<WeaselInstruction> instructions, WeaselClass returnType) {
		this.instructions = instructions;
		this.returnType = returnType;
	}
	
}
