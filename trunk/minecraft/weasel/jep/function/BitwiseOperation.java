/*****************************************************************************

 JEP 2.4.1, Extensions 1.1.1
      April 30 2007
      (c) Copyright 2007, Nathan Funk and Richard Morris
      See LICENSE-*.txt for license information.

 *****************************************************************************/

package weasel.jep.function;


import java.util.Stack;

import weasel.jep.ParseException;


/**
 * Bitwise arithmetic operation
 * 
 * @author MightyPork
 */
public class BitwiseOperation extends PostfixMathCommand {

	public enum OpType {
		NOT, AND, NAND, OR, NOR, NXOR, XOR, LSL, LSR;
	}

	private OpType type;

	public BitwiseOperation(OpType type) {
		this.type = type;
		numberOfParameters = (type == OpType.NOT) ? 1 : 2;
	}

	@Override
	public void run(Stack inStack) throws ParseException {
		checkStack(inStack);// check the stack

		if (type == OpType.NOT) {
			Object obj = inStack.pop();
			int num;
			if (obj instanceof Number) {
				num = (int) Math.round(((Number) obj).doubleValue());
			} else if (obj instanceof Boolean) {
				inStack.push(!(Boolean)obj);
				return;
			} else {
				throw new ParseException("NOT not defined for " + obj.getClass().getSimpleName());
			}

			num = Math.abs(num);


			inStack.push(new Integer(~num));
			return;
		} else {

			Object param2 = inStack.pop();
			Object param1 = inStack.pop();

			if ((param1 instanceof Number || param1 instanceof Boolean) && (param2 instanceof Number || param2 instanceof Boolean)) {
				double first=0;
				if(param1 instanceof Number) first = ((Number) param1).doubleValue();
				if(param1 instanceof Boolean) first = (Boolean) param1?1:0;
				double second=0;
				if(param1 instanceof Number) second = ((Number) param2).doubleValue();
				if(param1 instanceof Boolean) second = (Boolean) param2?1:0;

				int a = (int) Math.round(first);
				int b = (int) Math.round(second);
				a = Math.abs(a);
				b = Math.abs(b);

				if (type == OpType.AND) {
					inStack.push(new Integer(a & b));
					return;
				}

				if (type == OpType.NAND) {
					inStack.push(new Integer(~(a & b)));
					return;
				}

				if (type == OpType.OR) {
					inStack.push(new Integer(a | b));
					return;
				}

				if (type == OpType.OR) {
					inStack.push(new Integer(~(a | b)));
					return;
				}

				if (type == OpType.XOR) {
					inStack.push(new Integer(a ^ b));
					return;
				}

				if (type == OpType.XOR) {
					inStack.push(new Integer(~(a ^ b)));
					return;
				}

				if (type == OpType.LSL) {
					inStack.push(new Integer(a << b));
					return;
				}

				if (type == OpType.LSR) {
					inStack.push(new Integer(a >> b));
					return;
				}

			} else {
				throw new ParseException(type + " not defined for " + param1.getClass().getSimpleName() + " and " + param2.getClass().getSimpleName());
			}
		}
		return;
	}
}
