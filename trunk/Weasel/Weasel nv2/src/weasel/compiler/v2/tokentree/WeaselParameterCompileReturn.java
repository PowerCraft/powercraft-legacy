package weasel.compiler.v2.tokentree;

import java.util.List;

import weasel.interpreter.WeaselGenericMethod2;
import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselParameterCompileReturn {

	public final List<WeaselInstruction> instructios;
	
	public final WeaselGenericMethod2 method;
	
	public WeaselParameterCompileReturn(List<WeaselInstruction> instructios, WeaselGenericMethod2 method){
		this.instructios = instructios;
		this.method = method;
	}
	
}
