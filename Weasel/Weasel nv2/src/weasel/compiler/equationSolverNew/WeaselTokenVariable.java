package weasel.compiler.equationSolverNew;

import java.util.ArrayList;
import java.util.List;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselVariableInfo;
import weasel.compiler.equationSolverNew.Solver.String2D;
import weasel.interpreter.WeaselChecks;
import weasel.interpreter.WeaselClass;
import weasel.interpreter.bytecode.WeaselInstruction;
import weasel.interpreter.bytecode.WeaselInstructionLoadVariable;
import weasel.interpreter.bytecode.WeaselInstructionSaveVariable;

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
			WeaselKeyWordCompilerHelper compilerHelper, WeaselClass write)
			throws WeaselCompilerException {
		WeaselVariableInfo wvi = compilerHelper.getVariable(name);
		if(wvi==null){
			throw new WeaselCompilerException(0, "Variable not declared %s", name);
		}
		List<WeaselInstruction> instructions = new ArrayList<WeaselInstruction>();
		if(write==null){
			instructions.add(new WeaselInstructionLoadVariable(wvi.pos));
		}else{
			WeaselChecks.checkCast(write, wvi.type);
			instructions.add(new WeaselInstructionSaveVariable(wvi.pos));
		}
		return new WeaselCompileReturn(instructions, wvi.type);
	}

}
