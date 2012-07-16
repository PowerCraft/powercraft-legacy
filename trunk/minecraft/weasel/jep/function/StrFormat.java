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
 * formats string in some simple ways
 * 
 * @author Rich Morris Created on 27-Mar-2004
 */
public class StrFormat extends PostfixMathCommand {

	private EnumType type;

	public enum EnumType {
		ZEROFILL, CUTFIRST, CUTLAST;
	}

	public StrFormat(EnumType type) {
		numberOfParameters = 2;
		this.type = type;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		Object param2 = inStack.pop();
		Object param1 = inStack.pop();

		String str = Calc.toString(param1);
		int number = Calc.toInteger(param2);


		switch (type) {
			case ZEROFILL:
				while (str.length() < number) {
					str = "0" + str;
				}
				break;
			case CUTFIRST:
				if (number >= str.length()) {
					str = "";
				} else {
					str = str.substring(number);
				}
				break;
			case CUTLAST:
				if (number >= str.length()) {
					str = "";
				} else {
					str = str.substring(0, str.length() + 1 - number);
				}
				break;
		}

		inStack.push(str);//push the result on the inStack
		return;
	}
}
