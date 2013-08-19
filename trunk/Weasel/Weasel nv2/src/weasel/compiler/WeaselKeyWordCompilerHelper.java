package weasel.compiler;

import java.util.List;


public interface WeaselKeyWordCompilerHelper {

	public WeaselToken getNextToken();
	
	public void setNextToken(WeaselToken token);
	
	public WeaselBlockCompilerInfo compileBlock(List<WeaselTokenType> end, WeaselTokenType seperator, int access);
	
}
