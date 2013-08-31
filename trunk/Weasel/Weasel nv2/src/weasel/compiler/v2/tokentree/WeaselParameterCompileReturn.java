package weasel.compiler.v2.tokentree;

import weasel.compiler.WeaselInstructionList;
import weasel.interpreter.WeaselGenericMethod2;

public class WeaselParameterCompileReturn {

	public final WeaselInstructionList instructions;
	
	public final WeaselGenericMethod2 method;
	
	public WeaselParameterCompileReturn(WeaselInstructionList instructions, WeaselGenericMethod2 method){
		this.instructions = instructions;
		this.method = method;
	}
	
}
