/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/
package org.nfunk.jep.function;


import java.util.Stack;

import org.nfunk.jep.ParseException;


/**
 * Gets string's character at position N (one-character string as output)
 * 
 * @author MightyPork
 */
public class StringChar extends PostfixMathCommand {
	public StringChar() {
		numberOfParameters = 2;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack
		Object pos = inStack.pop();
		Object string = inStack.pop();

		int pos_i;
		String string_s;

		if (pos instanceof Double)
			pos_i = new Integer((int) Math.round((Double) pos));
		else if (pos instanceof Float)
			pos_i = new Integer(Math.round((Float) pos));
		else if (pos instanceof Integer)
			pos_i = new Integer((Integer) pos);
		else if (pos instanceof Long)
			pos_i = new Integer((Integer) pos);
		else
			throw new ParseException("CharAt(str,pos) can't work with pos=" + pos.getClass().getSimpleName());

		if (!(string instanceof String)) {
			throw new ParseException("CharAt(str,pos) can't work with str=" + string.getClass().getName());
		}

		string_s = (String) string;

		if (pos_i > string_s.length()) {
			throw new ParseException("CharAt(str,pos): pos (=" + pos_i + ") is too high for str=" + string_s);
		}

		if (pos_i < 0) {
			throw new ParseException("CharAt(str,pos): pos (=" + pos_i + ") must be >= 0.");
		}

		inStack.push(string_s.charAt(pos_i) + "");
	}
}
