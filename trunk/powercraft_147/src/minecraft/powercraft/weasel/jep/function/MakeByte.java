/*****************************************************************************

  JEP 2.4.1, Extensions 1.1.1
       April 30 2007
       (c) Copyright 2007, Nathan Funk and Richard Morris
       See LICENSE-*.txt for license information.

 *****************************************************************************/

package powercraft.weasel.jep.function;


import java.util.Stack;

import powercraft.weasel.engine.Calc;
import powercraft.weasel.jep.ParseException;



/**
 * Build byte of bits
 */
public class MakeByte extends PostfixMathCommand {
	private Add addFun = new Add();

	/**
	 * Constructor.
	 */
	public MakeByte() {
		// Use a variable number of arguments
		numberOfParameters = -1;
	}

	private int pow(int what) {
		if (what == 0) return 1;
		int out = 2;
		what--;
		int p = 2;

		while (what > 0) {
			out += p;
			p *= 2;
			what--;
		}
		return out;
	}

	/**
	 * Calculates the result of summing up all parameters, which are assumed to
	 * be of the Double type.
	 */
	@Override
	public void run(Stack stack) throws ParseException {
		checkStack(stack);// check the stack

		if (curNumberOfParameters < 1) throw new ParseException("No arguments for Byte");

		int exponent = curNumberOfParameters - 1;

		// initialize the result to the first argument
		Integer sum = Calc.toInteger(stack.pop());

		int byteout = Calc.toBoolean(sum) ? 1 : 0;

		Object param;
		int i = 1;

		// repeat summation for each one of the current parameters
		while (i < curNumberOfParameters) {
			// get the parameter from the stack
			param = stack.pop();

			boolean bit = Calc.toBoolean(param);

			// add it to the sum (order is important for String arguments)
			if (bit) sum = (int) Math.round(addFun.add(pow(i), sum));

			i++;
		}

		// push the result on the inStack
		stack.push(sum);
	}
}
