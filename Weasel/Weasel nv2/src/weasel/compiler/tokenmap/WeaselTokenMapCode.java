package weasel.compiler.tokenmap;

import java.util.ArrayList;
import java.util.List;

import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.keywords.WeaselKeyWord;
import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselTokenMapCode extends WeaselTokenMap {

	private List<WeaselInstruction> list;
	
	public WeaselTokenMapCode(WeaselToken token, List<WeaselInstruction> list) {
		super(token);
		this.list = list;
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
	public List<WeaselInstruction> compileTokenMap(WeaselKeyWordCompilerHelper weaselCompiler, int access, boolean pushThis) throws WeaselCompilerException {
		List<WeaselInstruction> instructions = new ArrayList<WeaselInstruction>();
		if(pushThis){
			//instructions.add(new WeaselInstructionPushN(token.line));
		}
		instructions.addAll(list);
		if(access==0 && ((WeaselKeyWord)token.param).compiler.letAnythingInStack()){
			//instructions.add(new WeaselInstructionPop(token.line));
		}
		if(access==2){
			throw new WeaselCompilerException(token.line, "Can't write %s", token);
		}
		return instructions;
	}

}
