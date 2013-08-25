package weasel.compiler.equationSolverNew;

import java.util.List;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselInterpreter;
import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselCompileReturn {
	
	public final List<WeaselInstruction> instructions;
	public final WeaselGenericClass returnType;
	
	public WeaselCompileReturn(List<WeaselInstruction> instructions, WeaselGenericClass returnType) {
		this.instructions = instructions;
		this.returnType = returnType;
	}
	
	public WeaselCompileReturn(List<WeaselInstruction> instructions, WeaselClass returnType, WeaselInterpreter interpreter) {
		this.instructions = instructions;
		this.returnType = new WeaselGenericClass(interpreter, returnType, new WeaselClass[0]);
	}
	
}
