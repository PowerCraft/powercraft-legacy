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
 * Gets string length
 * 
 * @author MightyPork
 */
public class StrLen extends PostfixMathCommand {
	public StrLen() {
		numberOfParameters = 1;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		Object param = inStack.pop();

		if (param instanceof String) {
			inStack.push(new Integer(((String) param).length()));
			return;
		}

		throw new ParseException("Strlen() can't work with " + param.getClass().getName());
	}
}
