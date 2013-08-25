package weasel.compiler.v2.tokentree;

import java.util.List;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselCompileReturn {

	public final List<WeaselInstruction> instructions;
	public final WeaselGenericClass returnType;

	public WeaselCompileReturn(List<WeaselInstruction> instructions, WeaselGenericClass returnType) {
		this.instructions = instructions;
		this.returnType = returnType;
	}

	public WeaselCompileReturn(List<WeaselInstruction> instructions, WeaselClass weaselClass) {
		this.instructions = instructions;
		returnType = new WeaselGenericClass(weaselClass);
	}

	public WeaselCompileReturn(List<WeaselInstruction> instructions) {
		this.instructions = instructions;
		returnType = null;
	}
	
}
