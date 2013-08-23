package weasel.compiler.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerMessage;
import weasel.compiler.WeaselCompilerMessage.MessageType;
import weasel.compiler.equationSolverNew.Solver;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.WeaselVariableInfo;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselMethod;
import weasel.interpreter.WeaselMethodBody;
import weasel.interpreter.WeaselModifier;
import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselMethodBodyCompilerV2 extends WeaselMethodBody implements WeaselKeyWordCompilerHelper {

	protected final WeaselCompiler compiler;
	protected final List<WeaselToken> methodTokens;
	protected int methodTokenPos;
	protected final HashMap<String, WeaselVariableInfo> variables = new HashMap<String, WeaselVariableInfo>();
	
	protected WeaselMethodBodyCompilerV2(WeaselMethod method, WeaselClass parentClass, List<WeaselToken> methodTokens, List<String> paramNames, List<Integer> paramModifier, WeaselCompiler compiler) {
		super(method, parentClass);
		this.compiler = compiler;
		instructions = new WeaselInstruction[0];
		this.methodTokens = methodTokens;
		methodTokens.add(new WeaselToken(WeaselTokenType.NONE, 0));
		for(int i=0; i<paramNames.size(); i++){
			variables.put(paramNames.get(i), new WeaselVariableInfo(paramModifier.get(i), paramNames.get(i), method.getParamClasses()[i], -i));
		}
		if(!WeaselModifier.isStatic(method.getModifier())){
			variables.put("this", new WeaselVariableInfo(0, "this", parentClass, -paramNames.size()));
		}
	}

	protected WeaselMethodBodyCompilerV2(WeaselMethod method, WeaselClass parentClass) {
		super(method, parentClass);
		compiler = null;
		methodTokens = null;
	}

	public void compile() {
		if(isNative()){
			return;
		}
		List<WeaselInstruction> instructions = new ArrayList<WeaselInstruction>();
		WeaselToken token = getNextToken();
		while(token.tokenType!=WeaselTokenType.NONE){
			List<WeaselToken> tokenList = new ArrayList<WeaselToken>();
			while(token.tokenType!=WeaselTokenType.SEMICOLON){
				tokenList.add(token);
				token = getNextToken();
			}
			try{
				Solver.parse(tokenList.toArray(new WeaselToken[0]));
			}catch(WeaselCompilerException e){
				onException(e.getLine(), e.getMessage());
			}
			token = getNextToken();
		}
		this.instructions = instructions.toArray(this.instructions);
	}

	@Override
	public WeaselToken getNextToken(){
		return methodTokens.remove(0);
	}
	
	@Override
	public void setNextToken(WeaselToken token) {
		methodTokens.add(0, token);
	}
	
	protected void onException(int line, String message, Object...obj){
		compiler.addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.ERROR, line, parentClass.getFileName(), String.format(message, obj)));
	}
	
}
