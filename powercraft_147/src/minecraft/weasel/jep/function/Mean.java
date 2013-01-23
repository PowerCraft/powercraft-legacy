/*****************************************************************************

  JEP 2.4.1, Extensions 1.1.1
       April 30 2007
       (c) Copyright 2007, Nathan Funk and Richard Morris
       See LICENSE-*.txt for license information.

 *****************************************************************************/

package weasel.jep.function;


import java.util.Stack;

import weasel.jep.ParseException;


/**
 * This class serves mainly as an example of a function that accepts any number
 * of parameters. Note that the numberOfParameters is initialized to -1.
 */
public class Mean extends PostfixMathCommand {
	Add add = new Add();
	Divide div = new Divide();

	/**
	 * Constructor.
	 */
	public Mean() {
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

		if (curNumberOfParameters < 1) throw new ParseException("No arguments for Mean");

		// initialize the result to the first argument
		Object sum = stack.pop();

		Object param;
		int i = 1;

		// repeat summation for each one of the current parameters
		while (i < curNumberOfParameters) {
			// get the parameter from the stack
			param = stack.pop();

			// add it to the sum (order is important for String arguments)
			sum = add.add(sum, param);

			i++;
		}

		// push the result on the inStack
		stack.push(div.div(sum, curNumberOfParameters));
	}
}
