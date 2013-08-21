package weasel.compiler.equationSolverNew;

import weasel.compiler.WeaselToken;

public class WeaselTokenBrackets implements IWeaselTokenTreeElement {

	
	@Override
	public void addSub(IWeaselTokenTreeElement te){
		subs.add(te);
	}
	
	@Override
	public String getName() {
		String target="["+subs.get(0).getName();
		for(int i=1; i<subs.size(); i++){
			target+=","+subs.get(i).getName();
		}
		target+="]";
		return target;
	}

	@Override
	public String toEncryptedString() {
		String target="["+subs.get(0).toEncryptedString();
		for(int i=1; i<subs.size(); i++){
			target+=","+subs.get(i).toEncryptedString();
		}
		target+="]";
		return target;
	}

	@Override
	public String toClassView() {
		String target="["+subs.get(0).toClassView();
		for(int i=1; i<subs.size(); i++){
			target+=","+subs.get(i).toClassView();
		}
		target+="]";
		return target;
	}

	@Override
	public WeaselToken simplify() {
		String target="";
		for(IWeaselTokenTreeElement sub:subs){
			target+=sub.getName();
		}
		return null;
	}

}
