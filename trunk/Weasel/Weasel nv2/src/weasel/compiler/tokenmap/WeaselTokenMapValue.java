package weasel.compiler.tokenmap;

import java.util.ArrayList;
import java.util.List;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselCompilerReturn;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselVariableInfo;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselField;
import weasel.interpreter.WeaselModifier;
import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstBoolean;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstDouble;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstInteger;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstString;
import weasel.interpreter.bytecode.WeaselInstructionLoadNull;
import weasel.interpreter.bytecode.WeaselInstructionReadField;
import weasel.interpreter.bytecode.WeaselInstructionReadStaticField;

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
	public WeaselCompilerReturn compileTokenMap(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelpher, WeaselClass write) throws WeaselCompilerException {
		List<WeaselInstruction> instructions = new ArrayList<WeaselInstruction>();
		WeaselClass inStack;
		switch(token.tokenType){
		case BOOL:
			if(write!=compiler.baseTypes.voidClass){
				throw new WeaselCompilerException(token.line, "Can't write %s to Boolean value", write);
			}
			instructions.add(new WeaselInstructionLoadConstBoolean((Boolean)token.param));
			inStack = compiler.baseTypes.booleanClass;
			break;
		case DOUBLE:
			if(write!=compiler.baseTypes.voidClass){
				throw new WeaselCompilerException(token.line, "Can't write %s to Double value", write);
			}
			instructions.add(new WeaselInstructionLoadConstDouble((Double)token.param));
			inStack = compiler.baseTypes.doubleClass;
			break;
		case INTEGER:
			if(write!=compiler.baseTypes.voidClass){
				throw new WeaselCompilerException(token.line, "Can't write %s to Integer value", write);
			}
			instructions.add(new WeaselInstructionLoadConstInteger((Integer)token.param));
			inStack = compiler.baseTypes.intClass;
			break;
		case STRING:
			if(write!=compiler.baseTypes.voidClass){
				throw new WeaselCompilerException(token.line, "Can't write %s to String value", write);
			}
			instructions.add(new WeaselInstructionLoadConstString((String)token.param));
			inStack = compiler.baseTypes.getStringClass();
			break;
		case NULL:
			instructions.add(new WeaselInstructionLoadNull());
			inStack = null;
			break;
		case IDENT:
			String varName = (String)token.param;
			WeaselVariableInfo variable = compilerHelpher.getVariable(varName);
			WeaselField field = null;
			if(variable==null){
				field = compilerHelpher.getCompiledClass().getField(varName);
				if(field==null){
					throw new WeaselCompilerException(token.line, "Can't find variable %s", varName);
				}
				if(WeaselModifier.isStatic(field.getModifier())){
					instructions.add(new WeaselInstructionReadStaticField(field.getParentClass().getRealName()+"."+varName+":"+field.getType().getByteName()));
					return new WeaselCompilerReturn(token, instructions, field.getType());
				}else{
					variable = compilerHelpher.getVariable("this");
					if(variable==null){
						throw new WeaselCompilerException(token.line, "Can't access non static field %s form not static method %s", field, compilerHelpher.getCompiledMethod());
					}
				}
				inStack = field.getType();
			}else{
				inStack = variable.type;
			}
			
			if(field!=null){
				instructions.add(new WeaselInstructionReadField(field.getParentClass().getRealName()+"."+varName+":"+field.getType().getByteName()));
			}
			break;
		default:
			throw new WeaselCompilerException(token.line, "Unextpect token %s as value detected", token);
		}

		return new WeaselCompilerReturn(token, instructions, inStack);
	}

}
