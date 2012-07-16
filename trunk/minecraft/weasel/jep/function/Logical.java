/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/

package weasel.jep.function;


import java.util.Stack;

import weasel.jep.ParseException;


public class Logical extends PostfixMathCommand {
	int id;
	public static final int AND = 0;
	public static final int OR = 1;

	public Logical(int id_in) {
		id = id_in;
		numberOfParameters = 2;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack

		Object param2 = inStack.pop();
		Object param1 = inStack.pop();

		boolean b1 = false, b2 = false;

		double x, y;
		if ((param1 instanceof Number)) {
			x = ((Number) param1).doubleValue();
		} else if ((param1 instanceof Boolean)) {
			x = ((Boolean) param1).booleanValue() ? 1 : 0;
			b1 = true;
		}

		else {
			throw new ParseException("Logical operation not defined for " + param1.getClass().getName());
		}

		if ((param2 instanceof Number)) {
			y = ((Number) param2).doubleValue();
		} else if ((param2 instanceof Boolean)) {
			y = ((Boolean) param2).booleanValue() ? 1 : 0;
			b2 = true;
		} else {
			throw new ParseException("Logical operation not defined for " + param1.getClass().getName());
		}

		int r;

		// now we have two numbers, representing the inputs.

		// make them positive
		if (x < 0) x = -x;
		if (y < 0) y = -y;

		if (b1 && b2) {

			boolean a = (x != 0d), b = (y != 0d), out = false;

			switch (id) {
				case AND:
					out = (a && b);
					break;
				case OR:
					out = (a || b);
					break;
				default:
					r = 0;
			}

			r = out ? 1 : 0;

			inStack.push(new Double(r)); // push the result on the inStack			

		} else {

			int a = (int) Math.round(x), b = (int) Math.round((y)), out = 0;

			switch (id) {
				case AND:
					out = (a & b);
					break;
				case OR:
					out = (a | b);
					break;
				default:
					out = 0;
			}

			inStack.push(new Integer(out)); // push the result on the inStack	

		}



		return;
	}
}
