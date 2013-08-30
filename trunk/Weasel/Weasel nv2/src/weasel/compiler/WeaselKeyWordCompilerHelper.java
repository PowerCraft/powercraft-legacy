package weasel.compiler;

import java.util.List;

import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselGenericField;
import weasel.interpreter.WeaselGenericMethod2;



public interface WeaselKeyWordCompilerHelper {

	public WeaselVariableInfo getVariable(String name);

	public List<WeaselGenericMethod2> getGenericMethods(String param) throws WeaselCompilerException;

	public WeaselGenericField getGenericField(String variable);

	public WeaselGenericMethod2 getCompilingMethod();

	public WeaselClassCompiler getClassCompiler();

	public WeaselVariableInfo newVar(int modifier, String varName, WeaselGenericClass wgc);

	public void writeVar(WeaselVariableInfo wvi);
	
}
