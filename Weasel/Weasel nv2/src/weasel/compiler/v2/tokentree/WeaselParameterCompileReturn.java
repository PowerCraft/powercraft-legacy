package weasel.compiler.v2.tokentree;

import java.util.List;

import weasel.interpreter.WeaselGenericMethod2;
import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselParameterCompileReturn {

	public final List<WeaselInstruction> instructions;
	
	public final WeaselGenericMethod2 method;
	
	public WeaselParameterCompileReturn(List<WeaselInstruction> instructions, WeaselGenericMethod2 method){
		this.instructions = instructions;
		this.method = method;
	}
	
}
