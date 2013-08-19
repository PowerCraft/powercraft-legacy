package weasel.compiler.tokenmap;

import java.util.List;

import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.interpreter.bytecode.WeaselInstruction;

public abstract class WeaselTokenMap {

	public WeaselToken token;
	
	public WeaselTokenMap(WeaselToken token){
		this.token = token;
	}
	
	public abstract WeaselTokenMap addTokenMap(WeaselTokenMap tokenMap) throws WeaselCompilerException;
	
	protected abstract void addLeftTokenMap(WeaselTokenMap tokenMap) throws WeaselCompilerException;
	
	@Override
	public abstract String toString();

	public abstract List<WeaselInstruction> compileTokenMap(WeaselKeyWordCompilerHelper weaselCompiler, int access, boolean pushThis) throws WeaselCompilerException;
	
}
