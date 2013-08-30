package weasel.compiler.keywords;

import java.util.Arrays;
import java.util.ListIterator;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;

public abstract class WeaselKeyWordCompiler {
	
	public abstract WeaselCompilerReturn compile(WeaselToken token, WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelpher,  ListIterator<WeaselToken> iterator) throws WeaselCompilerException;

	protected void expect(WeaselToken token, WeaselTokenType...tokenTypes) throws WeaselCompilerException{
		for(int i=0; i<tokenTypes.length; i++){
			if(token.tokenType == tokenTypes[i]){
				return;
			}
		}
		throw new WeaselCompilerException(token.line, "Unexpected token %s expected %s", token, Arrays.toString(tokenTypes));
	}
	
	protected void expectFirst(WeaselToken token, boolean isFirst) throws WeaselCompilerException{
		if(!isFirst){
			throw new WeaselCompilerException(token.line, "Token %s has to be first", token);
		}
	}
	
}
