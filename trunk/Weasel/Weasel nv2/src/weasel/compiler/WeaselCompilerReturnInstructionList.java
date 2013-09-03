package weasel.compiler;

import weasel.interpreter.WeaselGenericClass;
import weasel.interpreter.WeaselPrimitive;
import weasel.interpreter.bytecode.WeaselInstructionCast;
import weasel.interpreter.bytecode.WeaselInstructionCastPrimitive;
import weasel.interpreter.bytecode.WeaselInstructionPop;

public class WeaselCompilerReturnInstructionList extends WeaselCompilerReturn {

	WeaselInstructionList instructions;
	WeaselGenericClass returnType;
	boolean isClassAccess;
	
	public WeaselCompilerReturnInstructionList(WeaselInstructionList instructions, WeaselGenericClass returnType) {
		this.instructions = instructions;
		this.returnType = returnType;
		isClassAccess = false;
	}
	
	public WeaselCompilerReturnInstructionList(WeaselInstructionList instructions, WeaselGenericClass returnType, boolean isClassAccess) {
		this.instructions = instructions;
		this.returnType = returnType;
		this.isClassAccess = isClassAccess;
	}

	@Override
	public WeaselInstructionList getInstructions(WeaselCompiler compiler, WeaselGenericClass weaselGenericClass) throws WeaselCompilerException {
		if(returnType.getBaseClass()!=compiler.baseTypes.voidClass && weaselGenericClass.getBaseClass()==compiler.baseTypes.voidClass){
			instructions.addWithoutLine(new WeaselInstructionPop());
			returnType = weaselGenericClass;
			return instructions;
		}
		if(returnType.getBaseClass().isPrimitive() && weaselGenericClass.getBaseClass().isPrimitive()){
			if(returnType.getBaseClass()!=weaselGenericClass.getBaseClass()){
				boolean canCast = WeaselPrimitive.canCastAutoTo(returnType.getBaseClass(), weaselGenericClass.getBaseClass());
				if(canCast){
					instructions.addWithoutLine(new WeaselInstructionCastPrimitive(WeaselPrimitive.getPrimitiveID(weaselGenericClass.getBaseClass())));
					returnType = weaselGenericClass;
				}else{
					throw new WeaselCompilerException(instructions.getLine(), "Types %s and %s are not compatible", returnType, weaselGenericClass);
				}
			}
			return instructions;
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
			instructions.addWithoutLine(new WeaselInstructionCast(returnWrapper.getBaseClass().getByteName()));
		}
		
		if(!returnWrapper.getBaseClass().isInterface() && !expectWrapper.getBaseClass().isInterface()){
			if(returnWrapper.canCastTo(expectWrapper) && !expectWrapper.canCastTo(returnWrapper)){
				throw new WeaselCompilerException(instructions.getLine(), "Types %s and %s are not compatible", returnType, weaselGenericClass);
			}
		}
		
		instructions.addWithoutLine(new WeaselInstructionCast(expectWrapper.getBaseClass().getRealName()));
		
		if(weaselGenericClass.getBaseClass().isPrimitive()){
			instructions.addWithoutLine(new WeaselInstructionCastPrimitive(WeaselPrimitive.getPrimitiveID(weaselGenericClass.getBaseClass())));
		}
		returnType = weaselGenericClass;
		return instructions;
	}
	
	public WeaselGenericClass getReturnType() {
		return returnType;
	}

	public boolean isClassAccess() {
		return isClassAccess;
	}

	public WeaselInstructionList getInstructions() {
		return instructions;
	}
	
}
