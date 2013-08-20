package weasel.compiler.equationSolver;

import java.util.ArrayList;

public class Equation{
	
	// 12+5*3-2
	// -(+(12,*(5,3)),2)

	// (12+5)*3-2
	// -(*(+(12,5),3),2)

	// 12+5*3+2
	// +(12,*(5,3),2)

	// 12+5*3+5-2
	// -(+(12,*(5,3),5),2)

	// 5/3/5
	// /(5,3,5)

	// 12+5*3+5/3-2
	// -(+(12,*(5,3),/(5,3)),2)
	public static Operand parse(String equ) {
		Operand ops = null;
		{
			ArrayList<Integer> lastOccurences = new ArrayList<Integer>();
			int depth=0, currMinPrio=Integer.MAX_VALUE;
			String lastIdent=null;
			Integer tmpPrio=0;
			for(int i=equ.length()-1; i>=0;i--) {
				if(equ.charAt(i)=='(') {
					depth--;
					
					continue;
				}
				if(equ.charAt(i)==')') {
					depth++;
					continue;
				}
				Operator.Properties properties;
				if((properties=Operator.operators.get(""+equ.charAt(i)))!=null) {
					tmpPrio=properties.priority+depth*Operator.prioRange;
					if(tmpPrio<=currMinPrio) {
						if(lastIdent==null) lastIdent=""+equ.charAt(i);
						
						if(tmpPrio==currMinPrio) {
							if(equ.charAt(i)==lastIdent.charAt(0))
								lastOccurences.add(i);
						}else {
							currMinPrio=tmpPrio;
							lastIdent=""+equ.charAt(i);
							lastOccurences.clear();
							lastOccurences.add(i);
						}
					}
				}
			}
			if(lastIdent==null) {
				return ops=new Operand(equ.replace("(", "").replace(")", ""));
			}
			
			Operand[] steps = new Operand[lastOccurences.size()+1];
			for(int i=0; i<lastOccurences.size();i++) {
				if(i==0)
					steps[i] = parse(equ.substring(0, lastOccurences.get(lastOccurences.size()-i-1)));
				else
					steps[i] = parse(equ.substring(lastOccurences.get(lastOccurences.size()-i)+1,
							lastOccurences.get(lastOccurences.size()-i-1)));
				
			}
			steps[steps.length-1] = parse(equ.substring(lastOccurences.get(0)+1));
			ops=new Operator(lastIdent, steps);
		}
		
		return ops;
	}

}
