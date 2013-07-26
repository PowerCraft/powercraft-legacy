/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/
package powercraft.weasel.jep.function;


import java.util.Stack;

import powercraft.weasel.jep.ParseException;



public class NumForm extends PostfixMathCommand {
	private int base;
	public NumForm(int base) {
		this.base=base;
		numberOfParameters = -1;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		if (curNumberOfParameters < 1) throw new ParseException("No arguments for numFormat");
		
		Object param = inStack.pop();
		
		int num = 0, zf=0;
		

		
		if(curNumberOfParameters == 2) {			
			
			try {
				if(param instanceof String) {
					param = Integer.parseInt((String)param);
				}
				zf = ((Number)param).intValue();	
			}catch(Exception c) {
				throw new ParseException("\""+param+"\" is not a valid number.");
			}
			
			param = inStack.pop();
			try {
				if(param instanceof String) {
					param = Integer.parseInt((String)param);
				}
				num = ((Number)param).intValue();	
			}catch(Exception c) {
				throw new ParseException("\""+param+"\" is not a valid number.");
			}
		}else {
			try {
				if(param instanceof String) {
					param = Integer.parseInt((String)param);
				}
				num = ((Number)param).intValue();	
			}catch(Exception c) {
				throw new ParseException("\""+param+"\" is not a valid number.");
			}
		}
		
		String out = "0";
		String nums = "";
		
		if(base==2) {
			out += "b";
			nums = Integer.toBinaryString(num);
		}
		
		if(base==16) {
			out += "x";
			nums = Integer.toHexString(num);
		}
		
		if(zf > 2000) System.exit(0);
		
		while (nums.length() < zf) {
			
			nums = "0" + nums;
		}
		
		inStack.push(out+nums);//push the result on the inStack
		return;
	}
}
