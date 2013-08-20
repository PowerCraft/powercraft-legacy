package weasel.compiler.tokenmap;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;

public class WeaselTokenMapCode extends WeaselTokenMap {

	private WeaselCompilerReturn compilerReturn;
	
	public WeaselTokenMapCode(WeaselToken token, WeaselCompilerReturn compilerReturn) {
		super(token);
		this.compilerReturn = compilerReturn;
	}

	@Override
	public WeaselTokenMap addTokenMap(WeaselTokenMap tokenMap) throws WeaselCompilerException {
		tokenMap.addLeftTokenMap(this);
		return tokenMap;
	}

	@Override
	protected void addLeftTokenMap(WeaselTokenMap tokenMap)
			throws WeaselCompilerException {
		throw new WeaselCompilerException(token.line, "Can't add token %s to left of %s", tokenMap.token, token);
	}

	@Override
	public String toString() {
		return token.toString();
	}

	@Override
	public WeaselCompilerReturn compileTokenMap(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelpher, int access, boolean pushThis) throws WeaselCompilerException {
		return compilerReturn;
	}

}
