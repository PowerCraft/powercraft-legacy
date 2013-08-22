package weasel.compiler.equationSolverNew;

import java.util.ArrayList;
import java.util.Arrays;

import weasel.compiler.WeaselToken;
import weasel.compiler.equationSolverNew.Solver.String2D;

public abstract class IWeaselTokenTreeElement {
	
	public abstract String getName();
	
	public abstract String toReadableString();
	
	public abstract String toEncryptedString();
	
	public abstract void toAdvancedEncryptedString(String2D str);

	public abstract String toClassView();
	
	public abstract WeaselToken simplify();
}
