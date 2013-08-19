package weasel.compiler;

import java.util.List;

import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselBlockCompilerInfo {

	public List<WeaselInstruction> instructions;
	public WeaselTokenType endingToken;
	
	public WeaselBlockCompilerInfo(List<WeaselInstruction> instructions, WeaselTokenType endingToken) {
		this.instructions = instructions;
		this.endingToken = endingToken;
	}
	
}
