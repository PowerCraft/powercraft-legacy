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
					stack.push(new Integer(rand.nextInt((int) Math.round((Double) arg))));
				else if (arg instanceof Float)
					stack.push(new Integer(rand.nextInt(Math.round((Float) arg))));
				else if (arg instanceof Integer)
					stack.push(new Integer(rand.nextInt((Integer) arg)));
				else if (arg instanceof Long)
					stack.push(new Integer(rand.nextInt(Math.round((Long) arg))));
				else if (arg instanceof Vector)
					stack.push(((Vector) arg).get(rand.nextInt(((Vector) arg).size())));
				else
					throw new ParseException("Random() can't work with " + arg.getClass().getSimpleName());
			}
			return;

		} else if (curNumberOfParameters == 2) {

			Object end = stack.pop();
			Object start = stack.pop();

			if (start instanceof Number && end instanceof Number) {


				int st;
				int en;

				if (start instanceof Double)
					st = new Integer((int) Math.round((Double) start));
				else if (start instanceof Float)
					st = new Integer(Math.round((Float) start));
				else if (start instanceof Integer)
					st = new Integer((Integer) start);
				else if (start instanceof Long)
					st = new Integer((Integer) start);
				else
					throw new ParseException("Random(start,end) can't work with " + start.getClass().getSimpleName());

				if (end instanceof Double)
					en = new Integer((int) Math.round((Double) end));
				else if (end instanceof Float)
					en = new Integer(Math.round((Float) end));
				else if (end instanceof Integer)
					en = new Integer((Integer) end);
				else if (end instanceof Long)
					en = new Integer((Integer) end);
				else
					throw new ParseException("Random(start,end) can't work with " + end.getClass().getSimpleName());


				if (st == en) {
					stack.push(new Integer(st));
					return;
				}

				if (st > en) {
					throw new ParseException("Random(start,end) can't have start > end.");
				}

				stack.push(new Integer(st + rand.nextInt((en - st + 1))));
			}
			return;

		}

		throw new ParseException("Random() got invalid number of parameters (" + curNumberOfParameters + ")");
	}
}
