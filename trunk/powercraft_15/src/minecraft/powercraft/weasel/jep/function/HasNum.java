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
 * Converts an object into its string representation. Calls the toString method
 * of the object.
 * 
 * @author Rich Morris Created on 27-Mar-2004
 */
public class HasNum extends PostfixMathCommand {
	
	public HasNum() {
		numberOfParameters = 1;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		if(param instanceof Number) {
			inStack.push(true);
			return;
		}
		try {
			Integer.valueOf(param.toString());
			inStack.push(true);
			return;
		}catch(NumberFormatException e) {			
			inStack.push(false);
			return;
		}
	}
}
