package weasel.compiler.tokenmap;

import java.util.ArrayList;
import java.util.List;

import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselTokenMapValue extends WeaselTokenMap {

	public WeaselTokenMapValue(WeaselToken token) {
		super(token);
	}

	@Override
	public WeaselTokenMap addTokenMap(WeaselTokenMap tokenMap) throws WeaselCompilerException {
		tokenMap.addLeftTokenMap(this);
		return tokenMap;
	}

	@Override
	protected void addLeftTokenMap(WeaselTokenMap tokenMap) throws WeaselCompilerException {
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
		if(access==1){
			switch(token.tokenType){
			case BOOL:
				//instructions.add(new WeaselInstructionPushB(token.line, (Boolean)token.param));
				break;
			case DOUBLE:
				//instructions.add(new WeaselInstructionPushD(token.line, (Double)token.param));
				break;
			case IDENT:
				//instructions.add(new WeaselInstructionLoadV(token.line, (String)token.param));
				break;
			case INTEGER:
				//instructions.add(new WeaselInstructionPushI(token.line, (Integer)token.param));
				break;
			case STRING:
				//instructions.add(new WeaselInstructionPushS(token.line, (String)token.param));
				break;
			case NULL:
				//instructions.add(new WeaselInstructionPushN(token.line));
				break;
			default:
				throw new WeaselCompilerException(token.line, "Unextpect token %s as read value detected", token);
			}
		}else if(access==2){
			switch(token.tokenType){
			case IDENT:
				//instructions.add(new WeaselInstructionOperator(token.line, WeaselOperator.NEW_POINTER, ParamType.PREFIX));
				//instructions.add(new WeaselInstructionSaveV(token.line, (String)token.param));
				break;
			default:
				throw new WeaselCompilerException(token.line, "Unextpect token %s as write value detected", token);
			}
		}
		return instructions;
	}

}
