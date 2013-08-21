package weasel.compiler.equationSolverNew;

import java.util.ArrayList;
import java.util.Arrays;

import weasel.compiler.WeaselOperator.Properties;
import weasel.compiler.WeaselToken;
import weasel.compiler.equationSolverNew.IWeaselTokenTreeElement;

public class WeaselTokenOperator implements IWeaselTokenTreeElement{
	
	Properties operator;
	
	public WeaselTokenOperator(Properties op) {
		operator = op;
	}
	
	public String getId() {
		return operator.operator;
	}
	
	@Override
	public void addSub(IWeaselTokenTreeElement te){
		subs.add(te);
	}

	@Override
	public String getName() {
		return operator.fullName;
	}

	@Override
	public String toString() {
		String ret = "";
		boolean brackets = false;
		for (int i = 0; i < subs.size(); i++) {
			if (i != 0)
				ret += getId();
			
			brackets=needsBrackets(i);
			
			if (brackets) ret += "(";
			
			ret += subs.get(i).toString();
			
			if (brackets) ret += ")";
		}
		return ret;
	}
	
	@Override
	public String toEncryptedString() {

		String ret = getId() + "(";
		for (int i = 0; i < subs.size(); i++) {
			if (i != 0)
				ret += ",";
			ret += subs.get(i).toEncryptedString();
		}
		ret += ")";
		return ret;
	}
	
	@Override
	public String toClassView() {
		String ret = getId() + "(";
		for (int i = 0; i < subs.size(); i++) {
			if (i != 0)
				ret += ",";
			ret += subs.get(i).toClassView();
		}
		ret += ")";
		return ret;
	}

	@Override
	public WeaselToken simplify() {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean needsBrackets(int subOp) {
		return true;
	}
/*		boolean needs, tmpNeeds;
		if(!((subs[subOp] instanceof Operator))) return false;
		Operator subOP = (Operator)subs[subOp];
		needs = (operators.get(getId()).priority >
			operators.get(subOP.getId()).priority);
		
		tmpNeeds = !(Operator.operators.get(getId()).isCommutative);
		tmpNeeds &= (operators.get(getId()).priority ==
				operators.get(subOP.getId()).priority);
		tmpNeeds &= (subOp!=0);
		needs |= tmpNeeds;
		return needs;				
	}
	
	@Override
	public WeaselToken simplify() {
		Operator curInner;
		ArrayList<WeaselToken> al = new ArrayList<WeaselToken>();
		for(int i=0;i<subs.length;i++) {
			subs[i] = subs[i].simplify();
			if(i==0 && subs[i] instanceof Operator) {
				curInner = (Operator)subs[i];
				if(curInner.getId().equals(getId()) &&
						Operator.operators.get(getId()).isSimplifyPossible) {
					al.addAll(Arrays.asList(curInner.subs));
				}
				else {
					al.add(subs[i]);
				}
			}else {
				al.add(subs[i]);
			}
		}
		subs = al.toArray(subs);
		return this;
	}*/
}
