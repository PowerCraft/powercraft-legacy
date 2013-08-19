package weasel.compiler;

import java.util.List;

import weasel.compiler.tokenmap.WeaselTokenMap;

public class WeaselBlockTokenMap {

	public List<WeaselTokenMap> tokenMap;
	public WeaselTokenType endingToken;
	
	public WeaselBlockTokenMap(List<WeaselTokenMap> tokenMap, WeaselTokenType endingToken) {
		this.tokenMap = tokenMap;
		this.endingToken = endingToken;
	}
	
	
	
}
