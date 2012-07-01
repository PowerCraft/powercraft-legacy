/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/

package org.nfunk.jep.function;


import java.util.Stack;

import org.nfunk.jep.ParseException;


public class Modulus extends PostfixMathCommand {
	public Modulus() {
		numberOfParameters = 2;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		Object param2 = inStack.pop();
		Object param1 = inStack.pop();

		if ((param1 instanceof Number) && (param2 instanceof Number)) {
			double divisor = ((Number) param2).doubleValue();
			double dividend = ((Number) param1).doubleValue();

			double result = dividend % divisor;

			inStack.push(new Double(result));
		} else {
			throw new ParseException("mod() not defined for "+param1.getClass().getSimpleName()+" and " + param2.getClass().getSimpleName());
		}
		return;
	}
}
