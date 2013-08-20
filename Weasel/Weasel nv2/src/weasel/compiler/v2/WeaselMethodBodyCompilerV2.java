package weasel.compiler.v2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerMessage;
import weasel.compiler.WeaselCompilerMessage.MessageType;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;
import weasel.compiler.WeaselVariableInfo;
import weasel.compiler.keywords.WeaselKeyWord;
import weasel.compiler.keywords.WeaselKeyWordCompiler;
import weasel.compiler.tokenmap.WeaselTokenMap;
import weasel.compiler.tokenmap.WeaselTokenMapCode;
import weasel.compiler.tokenmap.WeaselTokenMapOperator;
import weasel.compiler.tokenmap.WeaselTokenMapOperatorBlock;
import weasel.compiler.tokenmap.WeaselTokenMapValue;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselMethod;
import weasel.interpreter.WeaselMethodBody;
import weasel.interpreter.WeaselModifier;
import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionPop;

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
			WeaselCompilerReturn compilerReturn = compileStatement(token, WeaselTokenType.SEMICOLON);
			if(compilerReturn.instructions!=null){
				instructions.addAll(compilerReturn.instructions);
			}
			if(compilerReturn.inStack!=compiler.baseTypes.voidClass){
				instructions.add(new WeaselInstructionPop());
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

	@Override
	public List<WeaselCompilerReturn> compileParameterList(WeaselToken token, WeaselTokenType statementEnd, WeaselTokenType seperator) {
		List<WeaselCompilerReturn> parameterList = new ArrayList<WeaselCompilerReturn>();
		WeaselCompilerReturn compilerReturn;
		do{
			compilerReturn = compileStatement(getNextToken(), statementEnd, seperator);
			parameterList.add(compilerReturn);
		}while(compilerReturn.token.tokenType==seperator);
		return parameterList;
	}

	@Override
	public WeaselCompilerReturn compileStatement(WeaselToken token, WeaselTokenType statementEnd, WeaselTokenType...otherEnds) {
		try {
			BlockMap blockMap = makeBlockMap(token, statementEnd, otherEnds);
			return blockMap.tokenMap.compileTokenMap(compiler, this, compiler.baseTypes.voidClass);
		} catch (WeaselCompilerException e) {
			onException(e.getLine(), e.getMessage());
			token = getNextToken();
			List<WeaselTokenType> end = new ArrayList<WeaselTokenType>();
			end.add(statementEnd);
			end.addAll(Arrays.asList(otherEnds));
			while(!end.contains(token.tokenType));
			return new WeaselCompilerReturn(token, null, null);
		}
	}
	
	public static class BlockMap{
		public WeaselTokenMap tokenMap;
		public WeaselToken token;
		
		public BlockMap(WeaselTokenMap tokenMap, WeaselToken token) {
			this.tokenMap = tokenMap;
			this.token = token;
		}
	}
	
	public BlockMap makeBlockMap(WeaselToken token, WeaselTokenType statementEnd, WeaselTokenType...otherEnds) throws WeaselCompilerException {
		WeaselTokenMap map=null;
		WeaselTokenMap addMap;
		List<WeaselTokenType> end = new ArrayList<WeaselTokenType>();
		end.add(statementEnd);
		end.addAll(Arrays.asList(otherEnds));
		BlockMap bm;
		while(!end.contains(token.tokenType)){
			addMap = null;
			switch(token.tokenType){
			case BOOL:
			case DOUBLE:
			case INTEGER:
			case STRING:
			case IDENT:
			case NULL:
				addMap = new WeaselTokenMapValue(token);
				break;
			case KEYWORD:{
				WeaselKeyWordCompiler compiler = ((WeaselKeyWord)token.param).compiler;
				if(compiler==null){
					throw new WeaselCompilerException(token.line, "Unexpect keyword %s", token.param);
				}
				addMap = new WeaselTokenMapCode(token, compiler.compile(token, this.compiler, this, statementEnd));
				break;
			}
			case OPENBRACKET:
				bm = makeBlockMap(getNextToken(), WeaselTokenType.CLOSEBRACKET);
				addMap = new WeaselTokenMapOperatorBlock(new WeaselToken(WeaselTokenType.OPERATOR, token.line, WeaselOperator.BREAKETS), bm.tokenMap);
				token = bm.token;
				break;
			case OPERATOR:
				addMap = new WeaselTokenMapOperator(token);
				break;
			case QUESTIONMARK:
				bm = makeBlockMap(getNextToken(), WeaselTokenType.COLON);
				addMap = new WeaselTokenMapOperatorBlock(new WeaselToken(WeaselTokenType.OPERATOR, token.line, WeaselOperator.IF), bm.tokenMap);
				token = bm.token;
				break;
			default:
				throw new WeaselCompilerException(token.line, "Unexpect token %s", token);
			}
			if(map==null){
				map = addMap;
			}else{
				map = map.addTokenMap(addMap);
			}
			token = getNextToken();
		}
		System.out.println(map);
		return new BlockMap(map, token);
	}
	
	protected void onException(int line, String message, Object...obj){
		compiler.addWeaselCompilerMessage(new WeaselCompilerMessage(MessageType.ERROR, line, parentClass.getFileName(), String.format(message, obj)));
	}

	@Override
	public WeaselVariableInfo getVariable(String name) {
		return variables.get(name);
	}

	@Override
	public WeaselClass getCompiledClass() {
		return parentClass;
	}

	@Override
	public WeaselMethod getCompiledMethod() {
		return method;
	}
	
}
