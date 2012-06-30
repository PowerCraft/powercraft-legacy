/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/

package org.nfunk.jep.function;


import java.util.ArrayList;
import java.util.Stack;

import org.nfunk.jep.ParseException;


/**
 * Extended logical functions
 * 
 * @author MightyPork
 */
public class LogicalFn extends PostfixMathCommand {

	public enum LogicalFnType {
		AND(false), NAND(true), OR(false), NOR(true), XOR(false), NXOR(true), ODD(true), EVEN(false);

		public boolean neg;

		private LogicalFnType(boolean negated) {
			this.neg = negated;
		}
	}

	LogicalFnType type;


	public LogicalFn(LogicalFnType id_in) {
		type = id_in;
		numberOfParameters = -1;
	}

	@Override
	public void run(Stack stack) throws ParseException {
		checkStack(stack);// check the stack

		if (curNumberOfParameters < 2 && type != LogicalFnType.ODD && type != LogicalFnType.EVEN) throw new ParseException("Not enough arguments for " + type.toString().toLowerCase() + "()");

		int i = 0;

		ArrayList<Boolean> params = new ArrayList<Boolean>();

		// repeat summation for each one of the current parameters
		while (i < curNumberOfParameters) {
			// get the parameter from the stack
			Object param = stack.pop();

			double x;
			if ((param instanceof Number))
				x = ((Number) param).doubleValue();
			else if ((param instanceof Boolean))
				x = ((Boolean) param).booleanValue() ? 1 : 0;
			else
				throw new ParseException(type.toString().toLowerCase() + "() requires Numeric or Boolean arguments, got " + param.getClass().getName());

			params.add((x != 0d));

			i++;
		}

		boolean result = false;

		switch (type) {
			case AND:
			case NAND:
				result = true;
				for (boolean b : params) {
					result &= b;
				}
				break;

			case OR:
			case NOR:
				result = false;
				for (boolean b : params) {
					result |= b;
				}
				break;

			case XOR:
			case NXOR:
				result = false;
				boolean firstOne = false;

				boolean first = true;
				for (boolean b : params) {
					if (first) {
						firstOne = b;
					} else {
						result |= (b != firstOne);
					}
				}
				break;

			case ODD:
			case EVEN:
				result = false;
				for (boolean b : params) {
					result ^= b;
				}
				break;

		}

		if (type.neg) result = !result;


		stack.push(new Double(result ? 1 : 0)); // push the result on the inStack
		return;
	}
}
