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
 * Converts an object into its boolean value.
 * 
 * @author MightyPork
 */
public class Bool extends PostfixMathCommand {
	public Bool() {
		numberOfParameters = 1;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		Object param = inStack.pop();

		Object back = null;

		try {
			back = Calc.toBoolean(param);
		} catch (Exception e) {
			throw new ParseException(param.getClass().getSimpleName() + " can't be converted to a Boolean.");
		}

		inStack.push(back);
		return;
	}
}
