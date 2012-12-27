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


public class UMinus extends PostfixMathCommand {
	public UMinus() {
		numberOfParameters = 1;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack

		Object param = inStack.pop();

		inStack.push(umin(param));
		return;
	}

	public Object umin(Object param) throws ParseException {
		if (param instanceof Complex) return ((Complex) param).neg();
		if (param instanceof Number) return new Double(-((Number) param).doubleValue());

		throw new ParseException("Can't change sign of " + param.getClass().getSimpleName());
	}
}
