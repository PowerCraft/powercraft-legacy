package weasel.compiler.equationSolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Operator extends Operand {

	public static final HashMap<String, Properties> operators = new HashMap<String, Properties>();
	public static class Properties{
		public int priority;
		public boolean isCommutative;
		public boolean isSimplifyPossible;
		
		public Properties(int priority, boolean isCommutative,
				boolean simplifyPossible) {
			this.priority = priority;
			this.isCommutative = isCommutative;
			this.isSimplifyPossible = simplifyPossible;
		}
	}
	public static final int prioRange;
	static {
		operators.put("+", new Properties(1, true, true));
		operators.put("-", new Properties(1, false, true));
		operators.put("*", new Properties(2, true, true));
		operators.put("/", new Properties(2, false, true));
		operators.put("/", new Properties(2, false, true));

		int max = 0, tmpPrio;
		for (Properties prio : operators.values()) {
			tmpPrio = prio.priority;
			if (tmpPrio > max)
				max = tmpPrio;
		}
		prioRange = max;
	}

	private String id;
	private Operand[] ops;

	public Operator(String identifier, Operand... operands) {
		id = identifier;
		ops = operands;
	}

	public String getId() {
		return id;
	}

	public Operand[] getOps() {
		return ops;
	}

	@Override
	public String toString() {
		String ret = "";
		boolean brackets = false;
		for (int i = 0; i < getOps().length; i++) {
			if (i != 0)
				ret += getId();
			
			brackets=needsBrackets(i);
			
			if (brackets) ret += "(";
			
			ret += getOps()[i].toString();
			
			if (brackets) ret += ")";
		}
		return ret;
	}

	@Override
	public String toEncryptedString() {

		String ret = getId() + "(";
		for (int i = 0; i < getOps().length; i++) {
			if (i != 0)
				ret += ",";
			ret += getOps()[i].toEncryptedString();
		}
		ret += ")";
		return ret;
	}

	@Override
	public String toClassView() {
		String ret = getId() + "(";
		for (int i = 0; i < getOps().length; i++) {
			if (i != 0)
				ret += ",";
			ret += getOps()[i].toClassView();
		}
		ret += ")";
		return ret;
	}
	
	


	private boolean needsBrackets(int subOp) {
		boolean needs, tmpNeeds;
		if(!((getOps()[subOp] instanceof Operator))) return false;
		Operator subOP = (Operator)getOps()[subOp];
		needs = (operators.get(getId()).priority >
			operators.get(subOP.getId()).priority);
		
		tmpNeeds = !(Operator.operators.get(getId()).isCommutative);
		tmpNeeds &= (operators.get(getId()).priority ==
				operators.get(subOP.getId()).priority);
		tmpNeeds &= (subOp!=0);
		needs |= tmpNeeds;
		return needs;				
	}
	
public Operand simplify() {
		Operator curInner;
		ArrayList<Operand> al = new ArrayList<Operand>();
		for(int i=0;i<ops.length;i++) {
			ops[i] = ops[i].simplify();
			if(i==0 && ops[i] instanceof Operator) {
				curInner = (Operator)ops[i];
				if(curInner.getId().equals(getId()) &&
						Operator.operators.get(getId()).isSimplifyPossible) {
					al.addAll(Arrays.asList(curInner.getOps()));
				}
				else {
					al.add(ops[i]);
				}
			}else {
				al.add(ops[i]);
			}
		}
		ops = al.toArray(ops);
		return this;
	}
}