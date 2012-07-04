/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/
package org.nfunk.jep.function;


import java.util.Stack;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.type.Complex;


public class TanH extends PostfixMathCommand {
	public TanH() {
		numberOfParameters = 1;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(tanh(param));//push the result on the inStack
		return;
	}

	public Object tanh(Object param) throws ParseException {
		if (param instanceof Complex) {
			return ((Complex) param).tanh();
		} else if (param instanceof Number) {
			double value = ((Number) param).doubleValue();
			return new Double((Math.exp(value) - Math.exp(-value)) / (Math.pow(Math.E, value) + Math.pow(Math.E, -value)));
		}
		throw new ParseException("tanh() not defined for " + param.getClass().getSimpleName());
	}

}
