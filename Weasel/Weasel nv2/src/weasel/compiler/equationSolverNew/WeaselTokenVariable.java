package weasel.compiler.equationSolverNew;

import java.util.Arrays;

import weasel.compiler.WeaselToken;

public class WeaselTokenVariable implements IWeaselTokenTreeElement {

	private final String name;
	
	public WeaselTokenVariable(String name){
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void addSub(IWeaselTokenTreeElement... te) {
		subs.addAll(Arrays.asList(te));
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

}
