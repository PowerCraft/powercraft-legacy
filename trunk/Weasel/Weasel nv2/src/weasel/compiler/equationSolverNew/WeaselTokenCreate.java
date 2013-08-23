package weasel.compiler.equationSolverNew;

import weasel.compiler.WeaselCompiler;
import weasel.compiler.WeaselCompilerException;
import weasel.compiler.WeaselKeyWordCompilerHelper;
import weasel.compiler.WeaselToken;
import weasel.compiler.equationSolverNew.Solver.String2D;

public class WeaselTokenCreate extends IWeaselTokenTreeElement {

	private final WeaselToken name;
	private WeaselToken generics[];
	private WeaselToken params[];
	
	public WeaselTokenCreate(){
		
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toReadableString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toEncryptedString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toAdvancedEncryptedString(String2D str) {
		// TODO Auto-generated method stub

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
	public WeaselCompileReturn compile(WeaselCompiler compiler,
			WeaselKeyWordCompilerHelper compilerHelper)
			throws WeaselCompilerException {
		// TODO Auto-generated method stub
		return null;
	}

}
