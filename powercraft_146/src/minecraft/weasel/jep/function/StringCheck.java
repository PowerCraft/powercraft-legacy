/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/
package weasel.jep.function;


import java.util.Stack;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import weasel.jep.ParseException;


/**
 * Converts an object into its string representation. Calls the toString method
 * of the object.
 * 
 * @author Rich Morris Created on 27-Mar-2004
 */
public class StringCheck extends PostfixMathCommand {
	
	private int type;
	public StringCheck(int type) {
		numberOfParameters = 2;
		this.type = type;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		Object param2 = inStack.pop();
		Object param1 = inStack.pop();
		
		String match = param2.toString();
		String text = param1.toString();
		
		boolean flag = false;
		switch(type) {
			case 0://startswith
				flag = text.startsWith(match);
				break;
			case 1://endswith
				flag = text.endsWith(match);
				break;
			case 2://contains
				flag = text.matches("(.|^)"+Pattern.quote(match)+"(.|$)");
				break;
			case 3://matches
				try {
					flag = text.matches(match);
				}catch(PatternSyntaxException e) {
					throw new ParseException("\""+match+"\" is not a valid regexp.");
				}
				break;
				
		}
		
		inStack.push(flag);
		return;		
		
	}
}
