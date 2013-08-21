package weasel.compiler.equationSolverNew;

import java.util.ArrayList;
import java.util.Arrays;

import weasel.compiler.WeaselOperator;
import weasel.compiler.WeaselToken;
import weasel.compiler.WeaselTokenType;

public class Operator extends WeaselToken{
	
	public Operator(WeaselToken token) {
		super(WeaselTokenType.OPERATOR, token.line, token.param);
	}
	
	public String getId() {
		return ((Properties)param).name;
	}

	// 12+5*3+5/3-2
	// -(+(12,*(5,3),/(5,3)),2)

	@Override
	public String toString() {
		String ret = "";
		boolean brackets = false;
		for (int i = 0; i < subs.length; i++) {
			if (i != 0)
				ret += getId();
			
			brackets=needsBrackets(i);
			
			if (brackets) ret += "(";
			
			ret += subs[i].toString();
			
			if (brackets) ret += ")";
		}
		return ret;
	}
	
	@Override
	public String toEncryptedString() {

		String ret = getId() + "(";
		for (int i = 0; i < subs.length; i++) {
			if (i != 0)
				ret += ",";
			ret += subs[i].toEncryptedString();
		}
		ret += ")";
		return ret;
	}

	@Override
	public String toClassView() {
		String ret = getId() + "(";
		for (int i = 0; i < subs.length; i++) {
			if (i != 0)
				ret += ",";
			ret += subs[i].toClassView();
		}
		ret += ")";
		return ret;
	}
	
	


	private boolean needsBrackets(int subOp) {
		boolean needs, tmpNeeds;
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
	}
}
