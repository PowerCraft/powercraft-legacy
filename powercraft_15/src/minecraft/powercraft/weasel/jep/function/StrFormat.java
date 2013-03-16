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
 * formats string in some simple ways
 * 
 * @author Rich Morris Created on 27-Mar-2004
 */
public class StrFormat extends PostfixMathCommand {

	private EnumType type;

	public enum EnumType {
		ZEROFILL, CUTFIRST, CUTLAST, GETFIRST, GETLAST;
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
		String str;
		int number;
		try {
			str = Calc.toString(param1);
			number = Calc.toInteger(param2);
		} catch (ClassCastException e) {
			throw new ParseException(type + " got invalid parameter types. Needs String and Integer");
		}

		try {

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
						str = str.substring(0, str.length() - number);
					}
					break;
				case GETFIRST:
					if (number >= str.length()) {
					} else {
						str = str.substring(0,number);
					}
					break;
				case GETLAST:
					if (number >= str.length()) {
					} else {
						str = str.substring(str.length() - number);
					}
					break;
			}
		} catch (StringIndexOutOfBoundsException e) {
			throw new ParseException(type + " cannot work with '" + str + "' and " + number + ".");
		}

		inStack.push(str);//push the result on the inStack
		return;
	}
}
