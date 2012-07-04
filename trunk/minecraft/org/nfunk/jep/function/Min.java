/*****************************************************************************

  JEP 2.4.1, Extensions 1.1.1
       April 30 2007
       (c) Copyright 2007, Nathan Funk and Richard Morris
       See LICENSE-*.txt for license information.

 *****************************************************************************/

package org.nfunk.jep.function;


import java.util.Stack;

import org.nfunk.jep.ParseException;


/**
 * Min value
 */
public class Min extends PostfixMathCommand {
	Comparative comp = new Comparative(Comparative.LT);

	/**
	 * Constructor.
	 */
	public Min() {
		// Use a variable number of arguments
		numberOfParameters = -1;
	}

	/**
	 * Calculates the result of summing up all parameters, which are assumed to
	 * be of the Double type.
	 */
	@Override
	public void run(Stack stack) throws ParseException {
		checkStack(stack);// check the stack

		if (curNumberOfParameters < 1) throw new ParseException("No arguments for Min");

		// initialize the result to the first argument
		Object min = stack.pop();

		Object param;
		int i = 1;

		// repeat summation for each one of the current parameters
		while (i < curNumberOfParameters) {
			// get the parameter from the stack
			param = stack.pop();

			// add it to the sum (order is important for String arguments)
			min = comp.lt(min, param) ? min : param;

			i++;
		}

		// push the result on the inStack
		stack.push(min);
	}
}
