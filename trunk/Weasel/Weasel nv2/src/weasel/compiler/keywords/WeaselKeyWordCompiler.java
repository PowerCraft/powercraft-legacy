package weasel.compiler.keywords;

import java.util.List;

import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.interpreter.bytecode.WeaselInstruction;

public abstract class WeaselKeyWordCompiler {
	
	public abstract List<WeaselInstruction> compile(WeaselToken token, WeaselKeyWordCompilerHelper compiler, boolean isFirst) throws WeaselCompilerException;

	protected WeaselToken expect(WeaselKeyWordCompilerHelper compiler, WeaselTokenType tt) throws WeaselCompilerException{
		WeaselToken token = compiler.getNextToken();
		if(token.tokenType!=tt){
			throw new WeaselCompilerException(token.line, "Expect %s but got %s", tt, token);
		}
		return token;
	}
	
	protected void expectFirst(WeaselToken token, boolean isFirst) throws WeaselCompilerException{
		if(!isFirst){
			throw new WeaselCompilerException(token.line, "Token %s has to be first", token);
		}
	}
	
	public abstract boolean letAnythingInStack();
	
}
