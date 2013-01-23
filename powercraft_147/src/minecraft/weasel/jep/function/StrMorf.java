/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/
package weasel.jep.function;


import java.util.Stack;

import weasel.Calc;
import weasel.jep.ParseException;


public class StrMorf extends PostfixMathCommand {
	private int morf;

	/**
	 * @param morf 0 lower 1 upper 2 reverse
	 */
	public StrMorf(int morf) {
		
		numberOfParameters = 1;
		this.morf = morf;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		Object param = inStack.pop();

		if (param instanceof String) {
			String ss = Calc.toString(param);
			if(morf == 0) ss = ss.toLowerCase();
			if(morf == 1) ss = ss.toUpperCase();
			if(morf == 2) {
				String ss2 = "";
				for(int i=0; i<ss.length(); i++) {
					ss2 = ss.charAt(i)+ss2;
				}
				ss = ss2;
			}
			inStack.push(ss);
			return;
		}

		throw new ParseException("Strlen() can't work with " + param.getClass().getName());
	}
}
