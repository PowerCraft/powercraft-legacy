package weasel.compiler.equationSolverNew;

import java.util.ArrayList;

import weasel.compiler.WeaselToken;

public interface IWeaselTokenTreeElement {
	
	final ArrayList<IWeaselTokenTreeElement> subs = new ArrayList<IWeaselTokenTreeElement>();
	
	public String getName();

	public void addSub(IWeaselTokenTreeElement... te);
	
	public String toString();
	
	public String toEncryptedString();

	public String toClassView();
	
	public WeaselToken simplify();
}
