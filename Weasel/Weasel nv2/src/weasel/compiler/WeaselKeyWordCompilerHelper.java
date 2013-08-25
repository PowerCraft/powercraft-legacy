package weasel.compiler;

import java.util.List;

import weasel.interpreter.WeaselGenericMethod2;



public interface WeaselKeyWordCompilerHelper {

	public WeaselVariableInfo getVariable(String name);

	public List<WeaselGenericMethod2> getGenericMethods(String param) throws WeaselCompilerException;
	
}
