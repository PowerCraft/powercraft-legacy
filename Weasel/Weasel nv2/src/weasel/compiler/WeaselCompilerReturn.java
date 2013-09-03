package weasel.compiler;

import weasel.interpreter.WeaselClass;
import weasel.interpreter.WeaselGenericClass;

public abstract class WeaselCompilerReturn {

	public abstract WeaselInstructionList getInstructions(WeaselCompiler compiler, WeaselGenericClass weaselGenericClass) throws WeaselCompilerException;

	public abstract WeaselGenericClass getReturnType();

	public abstract boolean isClassAccess();

	public WeaselInstructionList getInstructions(WeaselCompiler compiler, WeaselClass baseClass) throws WeaselCompilerException {
		return getInstructions(compiler, new WeaselGenericClass(baseClass));
	}

	public abstract WeaselInstructionList getInstructions();

}