/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/
package weasel.jep.function;


import java.util.Stack;
import java.util.Vector;

import weasel.jep.ParseException;


/**
 * Encapsulates and improves the Math.random() function.
 */
public class Random extends PostfixMathCommand {

	private java.util.Random rand = new java.util.Random();

	/**
	 * random function
	 */
	public Random() {
		numberOfParameters = -1;

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void run(Stack stack) throws ParseException {
		checkStack(stack);// check the stack

		if (curNumberOfParameters > 2) throw new ParseException("Random() takes 0 to 2 parameters.");

		if (curNumberOfParameters == 0) {

			stack.push(new Double(rand.nextDouble()));

		} else if (curNumberOfParameters == 1) {

			Object arg = stack.pop();

			if (arg instanceof Number) {
				if (arg instanceof Double)
					stack.push(new Double(rand.nextDouble()*((Double) arg)));
				else if (arg instanceof Float)
					stack.push(new Double(rand.nextDouble()*((Float) arg)));
				else if (arg instanceof Integer)
					stack.push(new Double(rand.nextInt((Integer) arg)));
				else if (arg instanceof Long)
					stack.push(new Double(rand.nextInt((int)(long)(Long) arg)));
				else if (arg instanceof Vector)
					stack.push(new Double((Double)((Vector) arg).get(rand.nextInt(((Vector) arg).size()))));
				else
					throw new ParseException("Random() can't work with " + arg.getClass().getSimpleName());
			}
			return;

		} else if (curNumberOfParameters == 2) {

			Object end = stack.pop();
			Object start = stack.pop();

			if (start instanceof Number && end instanceof Number) {


				double st;
				double en;

				if (start instanceof Double)
					st = new Double((Double) start);
				else if (start instanceof Float)
					st = new Double((Float) start);
				else if (start instanceof Integer)
					st = new Double((Integer) start);
				else if (start instanceof Long)
					st = new Double((Long) start);
				else
					throw new ParseException("Random(start,end) can't work with " + start.getClass().getSimpleName());

				if (start instanceof Double)
					en = new Double((Double) end);
				else if (start instanceof Float)
					en = new Double((Float) end);
				else if (start instanceof Integer)
					en = new Double((Integer) end);
				else if (start instanceof Long)
					en = new Double((Long) end);
				else
					throw new ParseException("Random(start,end) can't work with " + end.getClass().getSimpleName());


				if (st == en) {
					stack.push(new Double(st));
					return;
				}

				if (st > en) {
					throw new ParseException("Random(start,end) can't have start > end.");
				}

				stack.push(new Double(st + rand.nextDouble()*(en - st)));
			}
			return;

		}

		throw new ParseException("Random() got invalid number of parameters (" + curNumberOfParameters + ")");
	}
}
