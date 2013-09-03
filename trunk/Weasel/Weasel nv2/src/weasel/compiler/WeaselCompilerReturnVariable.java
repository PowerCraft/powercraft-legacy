package weasel.compiler;

import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselGenericField;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.bytecode.WeaselInstructionCast;
import weasel.interpreter.bytecode.WeaselInstructionCastPrimitive;
import weasel.interpreter.bytecode.WeaselInstructionPop;

public class WeaselCompilerReturnVariable extends WeaselCompilerReturn {

	private WeaselVariableInfo variableInfo;
	private WeaselGenericField field;
	private WeaselInstructionList instructionList = new WeaselInstructionList();
	private WeaselGenericClass returnType;
	
	public WeaselCompilerReturnVariable(WeaselVariableInfo variableInfo){
		this.variableInfo = variableInfo;
		returnType = variableInfo.type;
	}
	
	public WeaselCompilerReturnVariable(WeaselGenericField field){
		this.field = field;
		returnType = field.getGenericType();
	}
	
	@Override
	public WeaselInstructionList getInstructions(WeaselCompiler compiler, WeaselGenericClass weaselGenericClass) throws WeaselCompilerException {
		if(returnType.getBaseClass()!=compiler.baseTypes.voidClass && weaselGenericClass.getBaseClass()==compiler.baseTypes.voidClass){
			instructionList.addWithoutLine(new WeaselInstructionPop());
			returnType = weaselGenericClass;
			return instructionList;
		}
		if(returnType.getBaseClass().isPrimitive() && weaselGenericClass.getBaseClass().isPrimitive()){
			if(returnType.getBaseClass()!=weaselGenericClass.getBaseClass()){
				boolean canCast = WeaselPrimitive.canCastAutoTo(returnType.getBaseClass(), weaselGenericClass.getBaseClass());
				if(canCast){
					instructionList.addWithoutLine(new WeaselInstructionCastPrimitive(WeaselPrimitive.getPrimitiveID(weaselGenericClass.getBaseClass())));
					returnType = weaselGenericClass;
				}else{
					throw new WeaselCompilerException(instructionList.getLine(), "Types %s and %s are not compatible", returnType, weaselGenericClass);
				}
			}
			return instructionList;
		}
		WeaselGenericClass returnWrapper;
		WeaselGenericClass expectWrapper;
		if(returnType.getBaseClass().isPrimitive()){
			returnWrapper = new WeaselGenericClass(compiler.getWeaselClass(WeaselPrimitive.getWrapper(returnType.getBaseClass())));
		}else{
			returnWrapper = returnType;
		}
		if(weaselGenericClass.getBaseClass().isPrimitive()){
			expectWrapper = new WeaselGenericClass(compiler.getWeaselClass(WeaselPrimitive.getWrapper(weaselGenericClass.getBaseClass())));
		}else{
			expectWrapper = weaselGenericClass;
		}
		
		if(returnType.getBaseClass().isPrimitive()){
			instructionList.addWithoutLine(new WeaselInstructionCast(returnWrapper.getBaseClass().getByteName()));
		}
		
		if(!returnWrapper.getBaseClass().isInterface() && !expectWrapper.getBaseClass().isInterface()){
			if(returnWrapper.canCastTo(expectWrapper) && !expectWrapper.canCastTo(returnWrapper)){
				throw new WeaselCompilerException(instructionList.getLine(), "Types %s and %s are not compatible", returnType, weaselGenericClass);
			}
		}
		
		instructionList.addWithoutLine(new WeaselInstructionCast(expectWrapper.getBaseClass().getRealName()));
		
		if(weaselGenericClass.getBaseClass().isPrimitive()){
			instructionList.addWithoutLine(new WeaselInstructionCastPrimitive(WeaselPrimitive.getPrimitiveID(weaselGenericClass.getBaseClass())));
		}
		returnType = weaselGenericClass;
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

}
