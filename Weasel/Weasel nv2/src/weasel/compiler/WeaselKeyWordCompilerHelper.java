package weasel.compiler;



public interface WeaselKeyWordCompilerHelper {

	public WeaselToken getNextToken();
	
	public void setNextToken(WeaselToken token);

	public WeaselVariableInfo getVariable(String name);
	
}
