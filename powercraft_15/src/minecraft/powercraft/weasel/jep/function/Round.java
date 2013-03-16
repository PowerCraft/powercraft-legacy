/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/
package powercraft.weasel.jep.function;


import java.util.Stack;

import powercraft.weasel.jep.ParseException;



/**
 * A PostfixMathCommandI which rounds a number round(pi) finds the closest
 * integer to the argument round(pi,3) rounds the argument to 3 decimal places
 * 
 * @author Richard Morris
 */
public class Round extends PostfixMathCommand {
	public Round() {
		numberOfParameters = -1;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		if (this.curNumberOfParameters == 1) {
			Object param = inStack.pop();
			inStack.push(round(param));//push the result on the inStack
		} else {
			Object r = inStack.pop();
			Object l = inStack.pop();
			inStack.push(round(l, r));//push the result on the inStack

		}
		return;
	}


	private Object round(Object l, Object r) throws ParseException {
		if (l instanceof Number && r instanceof Number) {
			int dp = ((Number) r).intValue();
			double val = ((Number) l).doubleValue();
			double mul = Math.pow(10, dp);
			return new Double(Math.rint(val * mul) / mul);
		}
		throw new ParseException("round() not defined for " + l.getClass().getSimpleName() + " and " + r.getClass().getSimpleName());
	}

	public Object round(Object param) throws ParseException {
		if (param instanceof Number) {
			return new Double(Math.rint(((Number) param).doubleValue()));
		}

		throw new ParseException("round() not defined for " + param.getClass().getSimpleName());
	}

}
