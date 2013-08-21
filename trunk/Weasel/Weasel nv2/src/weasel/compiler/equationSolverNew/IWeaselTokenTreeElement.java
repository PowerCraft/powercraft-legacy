package weasel.compiler.equationSolverNew;

import java.util.ArrayList;
import java.util.Arrays;

import weasel.compiler.WeaselToken;

public interface IWeaselTokenTreeElement {
	
	ArrayList<IWeaselTokenTreeElement> subs = new ArrayList<IWeaselTokenTreeElement>();
	
	public String getName();

	public void addSub(IWeaselTokenTreeElement... te);
	
	public String toString();
	
	public String toEncryptedString();

	public String toClassView();
	
	public WeaselToken simplify();
}
