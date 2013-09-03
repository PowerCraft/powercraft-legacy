package weasel.compiler;

import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselNativeException;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.bytecode.WeaselInstructionCast;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstBoolean;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstByte;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstChar;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstDouble;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstFloat;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstInteger;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstLong;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstShort;
import weasel.interpreter.bytecode.WeaselInstructionLoadConstString;
import weasel.interpreter.bytecode.WeaselInstructionLoadNull;

public class WeaselCompilerReturnConstant extends WeaselCompilerReturn {

	private Object constant;
	private WeaselGenericClass returnType;
	private WeaselInstructionList instructionList = new WeaselInstructionList();
	
	public WeaselCompilerReturnConstant(WeaselCompiler compiler, int line, Object constant) throws WeaselCompilerException{
		this.constant = constant;
		if(constant instanceof Boolean){
			returnType = new WeaselGenericClass(compiler.baseTypes.booleanClass);
			instructionList.add(line, new WeaselInstructionLoadConstBoolean((Boolean)constant));
		}else if(constant instanceof Character){
			returnType = new WeaselGenericClass(compiler.baseTypes.charClass);
			instructionList.add(line, new WeaselInstructionLoadConstChar((Character)constant));
		}else if(constant instanceof Byte){
			returnType = new WeaselGenericClass(compiler.baseTypes.byteClass);
			instructionList.add(line, new WeaselInstructionLoadConstByte((Byte)constant));
		}else if(constant instanceof Short){
			returnType = new WeaselGenericClass(compiler.baseTypes.shortClass);
			instructionList.add(line, new WeaselInstructionLoadConstShort((Short)constant));
		}else if(constant instanceof Integer){
			returnType = new WeaselGenericClass(compiler.baseTypes.intClass);
			instructionList.add(line, new WeaselInstructionLoadConstInteger((Integer)constant));
		}else if(constant instanceof Long){
			returnType = new WeaselGenericClass(compiler.baseTypes.longClass);
			instructionList.add(line, new WeaselInstructionLoadConstLong((Long)constant));
		}else if(constant instanceof Float){
			returnType = new WeaselGenericClass(compiler.baseTypes.floatClass);
			instructionList.add(line, new WeaselInstructionLoadConstFloat((Float)constant));
		}else if(constant instanceof Double){
			returnType = new WeaselGenericClass(compiler.baseTypes.doubleClass);
			instructionList.add(line, new WeaselInstructionLoadConstDouble((Double)constant));
		}else if(constant instanceof String){
			returnType = new WeaselGenericClass(compiler.baseTypes.getStringClass());
			instructionList.add(line, new WeaselInstructionLoadConstString((String)constant));
		}else if(constant==null){
			returnType = null;
			instructionList.add(line, new WeaselInstructionLoadNull());
		}else{
			throw new WeaselCompilerException(line, "Unknow constant %s", constant);
		}
	}
	
	@Override
	public WeaselInstructionList getInstructions(WeaselCompiler compiler, WeaselGenericClass weaselGenericClass) throws WeaselCompilerException {
		if(returnType.getBaseClass()!=compiler.baseTypes.voidClass && weaselGenericClass.getBaseClass()==compiler.baseTypes.voidClass){
			return new WeaselInstructionList();
		}
		int primitiveID = WeaselPrimitive.getUnwrapped(weaselGenericClass.getBaseClass());
		if(primitiveID==WeaselPrimitive.getPrimitiveID(returnType.getBaseClass())){
			if(WeaselPrimitive.getPrimitiveID(weaselGenericClass.getBaseClass())==0){
				instructionList.addWithoutLine(new WeaselInstructionCast(weaselGenericClass.getBaseClass().getByteName()));
			}
			returnType = weaselGenericClass;
			return instructionList;
		}
		WeaselInstructionList instructionList = new WeaselInstructionList();
		int line = this.instructionList.getLine();
		try{
			switch(primitiveID){
			case WeaselPrimitive.BOOLEAN:
				instructionList.add(line, new WeaselInstructionLoadConstBoolean(WeaselPrimitive.getBoolean(constant)));
				break;
			case WeaselPrimitive.CHAR:
				instructionList.add(line, new WeaselInstructionLoadConstChar(WeaselPrimitive.getChar(constant)));
				break;
			case WeaselPrimitive.BYTE:
				instructionList.add(line, new WeaselInstructionLoadConstByte(WeaselPrimitive.getByte(constant)));
				break;
			case WeaselPrimitive.SHORT:
				instructionList.add(line, new WeaselInstructionLoadConstShort(WeaselPrimitive.getShort(constant)));
				break;
			case WeaselPrimitive.INT:
				instructionList.add(line, new WeaselInstructionLoadConstInteger(WeaselPrimitive.getInteger(constant)));
				break;
			case WeaselPrimitive.LONG:
				instructionList.add(line, new WeaselInstructionLoadConstLong(WeaselPrimitive.getLong(constant)));
				break;
			case WeaselPrimitive.FLOAT:
				instructionList.add(line, new WeaselInstructionLoadConstFloat(WeaselPrimitive.getFloat(constant)));
				break;
			case WeaselPrimitive.DOUBLE:
				instructionList.add(line, new WeaselInstructionLoadConstDouble(WeaselPrimitive.getDouble(constant)));
				break;
			}
		}catch(WeaselNativeException e){
			throw new WeaselCompilerException(this.instructionList.getLine(), e.getMessage());
		}
		if(WeaselPrimitive.getPrimitiveID(weaselGenericClass.getBaseClass())==0){
			instructionList.addWithoutLine(new WeaselInstructionCast(weaselGenericClass.getBaseClass().getByteName()));
		}
		returnType = weaselGenericClass;
		this.instructionList = instructionList;
		return instructionList;
	}

	@Override
	public WeaselGenericClass getReturnType() {
		return returnType;
	}

	@Override
	public boolean isClassAccess() {
		return false;
	}

	@Override
	public WeaselInstructionList getInstructions() {
		return instructionList;
	}

	public Object getConstant(){
		return constant;
	}
	
}
