package weasel.compiler.tokenmap;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.interpreter.WeaselClass;

public abstract class WeaselTokenMap {

	public WeaselToken token;
	
	public WeaselTokenMap(WeaselToken token){
		this.token = token;
	}
	
	public abstract WeaselTokenMap addTokenMap(WeaselTokenMap tokenMap) throws WeaselCompilerException;
	
	protected abstract void addLeftTokenMap(WeaselTokenMap tokenMap) throws WeaselCompilerException;
	
	@Override
	public abstract String toString();

	public abstract WeaselCompilerReturn compileTokenMap(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelpher, WeaselClass write) throws WeaselCompilerException;
	
}
