package weasel.compiler;

import java.util.List;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselMethod;


public interface WeaselKeyWordCompilerHelper {

	public WeaselToken getNextToken();
	
	public void setNextToken(WeaselToken token);
	
	public List<WeaselCompilerReturn> compileParameterList(WeaselToken token, WeaselTokenType statementEnd, WeaselTokenType seperator);
	
	public WeaselCompilerReturn compileStatement(WeaselToken token, WeaselTokenType statementEnd, WeaselTokenType...otherEnds);
	
	public WeaselVariableInfo getVariable(String name);
	
	public WeaselClass getCompiledClass();
	
	public WeaselMethod getCompiledMethod();
	
}
