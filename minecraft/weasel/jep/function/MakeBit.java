/*****************************************************************************

  JEP 2.4.1, Extensions 1.1.1
       April 30 2007
       (c) Copyright 2007, Nathan Funk and Richard Morris
       See LICENSE-*.txt for license information.

 *****************************************************************************/

package weasel.jep.function;


import java.util.Stack;

import weasel.Calc;
import weasel.jep.ParseException;


/**
 * Build byte of bits
 */
public class MakeBit extends PostfixMathCommand {
	private Add addFun = new Add();

	/**
	 * Constructor.
	 */
	public MakeBit() {
		// Use a variable number of arguments
		numberOfParameters = 2;
	}

	/**
	 * Calculates the result of summing up all parameters, which are assumed to
	 * be of the Double type.
	 */
	@Override
	public void run(Stack stack) throws ParseException {
		checkStack(stack);// check the stack

		Object par2 = stack.pop();
		Object par1 = stack.pop();
		
		if(par1 instanceof Number && par2 instanceof Number) {
			double bytex = ((Number) par1).doubleValue();
			double bitn = ((Number) par2).doubleValue();
			
			int num = (int) Math.round(bytex);
			int bit = (int) Math.round(bitn);
			
			stack.push((num & (1<<bit)) != 0);
			return;
		}
		
		throw new ParseException("Invalid parameters for BIT: "+par1.getClass().getSimpleName()+" and "+par2.getClass().getSimpleName());
	}
}
