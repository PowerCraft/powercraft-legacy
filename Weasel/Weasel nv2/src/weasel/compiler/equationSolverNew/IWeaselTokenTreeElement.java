package weasel.compiler.equationSolverNew;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.equationSolverNew.Solver.String2D;
import weasel.interpreter.WeaselClass;

public abstract class IWeaselTokenTreeElement {
	
	public abstract String getName();
	
	public abstract String getDisplayName();
	
	public abstract String toReadableString();
	
	public abstract String toEncryptedString();
	
	public abstract void toAdvancedEncryptedString(String2D str);

	public abstract String toClassView();
	
	public abstract WeaselToken simplify();
	
	public abstract WeaselCompileReturn compile(WeaselCompiler compiler, WeaselKeyWordCompilerHelper compilerHelper, WeaselClass write) throws WeaselCompilerException;
	
}
