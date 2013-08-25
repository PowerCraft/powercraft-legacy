package weasel.compiler;

import java.util.List;

import weasel.compiler.v2.tokentree.WeaselTreeGeneric;
import weasel.interpreter.WeaselGenericMethod2;



public interface WeaselKeyWordCompilerHelper {

	public WeaselToken getNextToken();
	
	public void setNextToken(WeaselToken token);

	public WeaselVariableInfo getVariable(String name);

	public List<WeaselGenericMethod2> getGenericMethods(String param) throws WeaselCompilerException;
	
}
