/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/
package org.nfunk.jep.function;


import java.util.Stack;

import org.nfunk.jep.ParseException;
import org.nfunk.jep.type.Complex;


/**
 * The complex conjugate of a number conj(c)
 * 
 * @author Rich Morris Created on 13-Feb-2005
 */
public class Conjugate extends PostfixMathCommand {
	public Conjugate() {
		numberOfParameters = 1;
	}

	@Override
	public void run(Stack inStack) throws ParseException {

		checkStack(inStack);// check the stack
		Object param = inStack.pop();
		inStack.push(conj(param));//push the result on the inStack
		return;
	}

	public Object conj(Object param) throws ParseException {

		if (param instanceof Complex)
			return ((Complex) param).conj();
		else if (param instanceof Number) return param;


		throw new ParseException("Invalid parameter type");
	}

}
