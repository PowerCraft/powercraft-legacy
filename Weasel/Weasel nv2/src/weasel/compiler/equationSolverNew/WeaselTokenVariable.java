package weasel.compiler.equationSolverNew;

import java.util.Arrays;
import java.util.List;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.equationSolverNew.Solver.String2D;
import weasel.interpreter.bytecode.WeaselInstruction;

public class WeaselTokenVariable extends IWeaselTokenTreeElement {

	private final String name;
	
	public WeaselTokenVariable(String name){
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString(){
		return toReadableString();
	}
	
	@Override
	public String toReadableString() {
		return getName();
	}
	
	@Override
	public String toEncryptedString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toClassView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WeaselToken simplify() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toAdvancedEncryptedString(String2D str) {

	}

	@Override
	public WeaselCompileReturn compile(WeaselCompiler compiler,
			WeaselKeyWordCompilerHelper compilerHelper)
			throws WeaselCompilerException {
		// TODO Auto-generated method stub
		return null;
	}

}
