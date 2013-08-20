package weasel.compiler;

import java.util.List;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselCompilerReturn {

	public List<WeaselInstruction> instructions;
	public WeaselClass inStack;
	public WeaselToken token;
	
	public WeaselCompilerReturn(WeaselToken token, List<WeaselInstruction> instructions, WeaselClass inStack) {
		this.token = token;
		this.instructions = instructions;
		this.inStack = inStack;
	}
	
}
