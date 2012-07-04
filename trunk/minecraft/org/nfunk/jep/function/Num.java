/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/
package org.nfunk.jep.function;


import java.util.Stack;

import net.minecraft.src.weasel.Calculator;

import org.nfunk.jep.ParseException;


/**
 * Converts an object into its numeric representation.
 * 
 * @author MightyPork
 */
public class Num extends PostfixMathCommand {
	public Num() {
		numberOfParameters = 1;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		
		Object back = null;
		
		if(param instanceof Number) {
			back = param;
		}else if(param instanceof Boolean) {
			back = new Integer((Boolean) param?1:0);
		}else if(param instanceof String) {
			try {
				back = Double.parseDouble((String) param);
			}catch(NumberFormatException e) {
				throw new ParseException(param+" can't be converted to a Number.");
			}
		}		
		
		inStack.push(back);
		return;
	}
}
