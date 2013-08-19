package weasel.compiler.v2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import weasel.compiler.WeaselBlockCompilerInfo;
import weasel.compiler.WeaselBlockTokenMap;
import weasel.compiler.WeaselClassException;
import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselSyntaxError;
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
		for(int i=0; i<paramNames.size(); i++){
			variables.put(paramNames.get(i), new WeaselVariableInfo(paramModifier.get(i), paramNames.get(i), method.getParamClasses()[i]));
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
		
	}

	@Override
	public WeaselToken getNextToken(){
		return methodTokens.remove(0);
	}
	
	@Override
	public void setNextToken(WeaselToken token) {
		methodTokens.add(0, token);
	}
	
	private List<WeaselInstruction> compileMapGenerating(WeaselToken token, boolean isFirst) throws WeaselCompilerException{
		return null;
	}
	
	public WeaselBlockTokenMap makeBlockMap(List<WeaselTokenType> end, WeaselTokenType seperator, boolean canBeFirst) throws WeaselCompilerException {
		WeaselToken token;
		WeaselTokenMap map=null;
		WeaselTokenMap addMap;
		List<WeaselTokenMap> list = new ArrayList<WeaselTokenMap>();
		while(!end.contains((token=getNextToken()).tokenType)){
			addMap = null;
			if(seperator==token.tokenType){
				list.add(map);
				map = null;
				continue;
			}
			switch(token.tokenType){
			case BOOL:
			case DOUBLE:
			case IDENT:
			case INTEGER:
			case STRING:
			case NULL:
				addMap = new WeaselTokenMapValue(token);
				break;
			case KEYWORD:{
				WeaselKeyWordCompiler compiler = ((WeaselKeyWord)token.param).compiler;
				if(compiler==null){
					onException(new WeaselSyntaxError(token.line, "Unexpect keyword %s", token.param));
				}
				addMap = new WeaselTokenMapCode(token, compiler.compile(token, this, map==null && canBeFirst));
				break;
			}case OPENBRACKET:
				addMap = new WeaselTokenMapOperatorBlock(new WeaselToken(WeaselTokenType.OPERATOR, token.line, WeaselOperator.CALL), makeBlockMap(Arrays.asList(WeaselTokenType.CLOSEBRACKET), WeaselTokenType.COMMA, false).tokenMap);
				break;
			case OPENINDEX:
				addMap = new WeaselTokenMapOperatorBlock(new WeaselToken(WeaselTokenType.OPERATOR, token.line, WeaselOperator.INDEX), makeBlockMap(Arrays.asList(WeaselTokenType.CLOSEINDEX), WeaselTokenType.COMMA, false).tokenMap);
				break;
			case OPENBLOCK:
				addMap = new WeaselTokenMapCode(token, compileMapGenerating(token, map==null && canBeFirst));
				break;
			case OPERATOR:
				addMap = new WeaselTokenMapOperator(token);
				break;
			case COMMA:
				list.add(map);
				map = null;
				continue;
			case QUESTIONMARK:
				addMap = new WeaselTokenMapOperatorBlock(new WeaselToken(WeaselTokenType.OPERATOR, token.line, WeaselOperator.IF), makeBlockMap(Arrays.asList(WeaselTokenType.COLON), null, false).tokenMap);
				break;
			default:
				onException(new WeaselSyntaxError(token.line, "Unexpect token %s", token));
			}
			if(map==null){
				map = addMap;
			}else{
				map = map.addTokenMap(addMap);
			}
		}
		list.add(map);
		return new WeaselBlockTokenMap(list, token.tokenType);
	}
	
	@Override
	public WeaselBlockCompilerInfo compileBlock(List<WeaselTokenType> end, WeaselTokenType seperator, int access) {
		return compileBlock(end, seperator, access, access==0);
	}
	
	public WeaselBlockCompilerInfo compileBlock(List<WeaselTokenType> end, WeaselTokenType seperator, int access, boolean canBeFirst) {
		List<WeaselInstruction> instructions = new ArrayList<WeaselInstruction>();
		WeaselBlockTokenMap block = null;
		List<WeaselTokenType> newEnd = new ArrayList<WeaselTokenType>(end);
		newEnd.add(seperator);
		do{
			try {
				block=makeBlockMap(newEnd, null, canBeFirst);
				if(block.tokenMap.get(0)!=null){
					if(block.endingToken!=seperator && seperator!=null){
						onException(new WeaselSyntaxError(methodTokens.get(0).line, "Expect %s bevore %s", seperator, end));
					}
					if(block.tokenMap.size()==1){
						instructions.addAll(block.tokenMap.get(0).compileTokenMap(this, access, false));
					}else{
						instructions.addAll(block.tokenMap.get(0).compileTokenMap(this, 1, false));
						for(int i=1; i<block.tokenMap.size(); i++){
							instructions.addAll(block.tokenMap.get(i).compileTokenMap(this, 1, false));
							//instructions.add(new WeaselInstructionOperator(block.tokenMap.get(i).token.line, WeaselOperator.COMMA, ParamType.NORMAL));
						}
						if(access==0){
							//instructions.add(new WeaselInstructionPop(block.tokenMap.get(block.tokenMap.size()-1).token.line));
						}
					}
				}
			} catch (WeaselCompilerException e) {
				onException(e);
			}
		}while(block.endingToken==seperator);
		return new WeaselBlockCompilerInfo(instructions, block.endingToken);
	}

	protected void onException(Throwable e){
		compiler.addWeaselCompilerException(new WeaselClassException(parentClass, e));
	}
	
}
