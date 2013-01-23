/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/
package weasel.jep.function;


import java.util.Stack;

import weasel.jep.ParseException;
import weasel.jep.type.Complex;


public class SineH extends PostfixMathCommand {
	public SineH() {
		numberOfParameters = 1;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(sinh(param));//push the result on the inStack
		return;
	}

	public Object sinh(Object param) throws ParseException {
		if (param instanceof Complex) {
			return ((Complex) param).sinh();
		} else if (param instanceof Number) {
			double value = ((Number) param).doubleValue();
			return new Double((Math.exp(value) - Math.exp(-value)) / 2);
		}

		throw new ParseException("sinh() not defined for " + param.getClass().getSimpleName());
	}

}
