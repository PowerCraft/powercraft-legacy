package weasel.compiler;

import java.util.List;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselMethod;
import weasel.interpreter.WeaselMethodBody;
import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselMethodBodyCompiler extends WeaselMethodBody {

	protected final WeaselCompiler compiler;
	protected final List<WeaselToken> methodTokens;
	
	protected WeaselMethodBodyCompiler(WeaselMethod method, WeaselClass parentClass, List<WeaselToken> methodTokens) {
		super(method, parentClass);
		compiler = ((WeaselClassCompilerV2)parentClass).compiler;
		instructions = new WeaselInstruction[0];
		this.methodTokens = methodTokens;
	}

	protected WeaselMethodBodyCompiler(WeaselMethod method, WeaselClass parentClass) {
		super(method, parentClass);
		compiler = ((WeaselClassCompilerV2)parentClass).compiler;
		methodTokens = null;
	}

	public void compile() {
		if(isNative()){
			return;
		}
		
	}

}
