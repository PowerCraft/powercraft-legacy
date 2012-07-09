package weasel;


import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weasel.exception.WeaselRuntimeException;
import weasel.jep.JEP;
import weasel.jep.ParseException;
import weasel.obj.WeaselBoolean;
import weasel.obj.WeaselInteger;
import weasel.obj.WeaselObject;
import weasel.obj.WeaselString;


/**
 * Expression evaluation class
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class Calc {

	private static JEP jep;

	static {
		jep = JEP.createWeaselParser(false);
	}

	/**
	 * Evaluate an expression with variables from the engine.
	 * 
	 * @param expression expression, without semicolon. eg. (14+a)/2
	 * @param variableContainer weasel variable container (primary ENGINE, but
	 *            sometimes also VariableMap).
	 * @return result of the expression.
	 * @throws CalcException when evaluation fails.
	 */
	public static Object evaluate(String expression, IVariableProvider variableContainer) throws CalcException {

		if (expression == null || expression.length() == 0) return null;

		if (expression.contains(";")) {
			throw new CalcException("CALC evaluate - unexpected \";\" in a numeric expression.");
		}

		//expression = correctQuotes(expression);

		//prepare jep for next run.
		jep.clearErrors();

		//take out the strings
		Matcher matcher;

		HashMap<String, String> replacedStrings = new HashMap<String, String>(5);
		int stringReplaceCounter = 0;

		if (expression.contains("\"")) {
			matcher = Compiler.stringPattern.matcher(expression);
			StringBuffer sb = new StringBuffer(30);

			while (matcher.find()) {
				String str = matcher.group(1);
				String repl = "[@" + stringReplaceCounter + "]";

				replacedStrings.put(repl, str);
				matcher.appendReplacement(sb, repl);
			}

			matcher.appendTail(sb);
			expression = sb.toString();
		}

		matcher = Compiler.variablePattern.matcher(expression);

		while (matcher.find()) {
			String name = matcher.group(1);

			// skip parser's constants, they are built-in
			if (Compiler.parserConstants.contains(name)) {
				continue;
			}

			try {
				//add variable into JEP
				jep.addVariable(name, variableContainer.getVariable(name).get());
			} catch (NullPointerException npe) {
				throw new WeaselRuntimeException("Variable \"" + name + "\" not set in this scope.");
				
			}
		}
		matcher = null;

		//put back replaced strings.
		if (replacedStrings.size() > 0) {
			for (Entry<String, String> entry : replacedStrings.entrySet()) {
				expression = expression.replace(entry.getKey(), entry.getValue());
			}
		}


		//evaluate
		jep.parseExpression(expression);

		Object out = jep.getValueAsObject();

		if (jep.hasError()) {
			throw new CalcException(jep.getErrorInfo());
		}

		//return expression result
		return out;

	}

//	private static String correctQuotes(String expression) {
//		return expression.replaceAll("'", "\"");
//	}



//  // NOT USED ANYWHERE	
//	/**
//	 * Evaluate an expression using variables from a given map.<br>
//	 * It is fast, but does not support Weasel variables and does not check what
//	 * variables are needed.
//	 * 
//	 * @param expression the expression
//	 * @param vars map of variables (string → variable)
//	 * @return result value
//	 * @throws CalcException when the evaluation fails
//	 */
//	public static Object evaluateSimple(String expression, Map<String, Object> vars) {
//
//		JEP jep = JEP.createWeaselParser(false);
//		for (Entry<String, Object> entry : vars.entrySet()) {
//			jep.addVariable(entry.getKey(), entry.getValue());
//		}
//
//		jep.parseExpression(expression);
//
//		if (jep.hasError()) {
//			throw new CalcException(jep.getErrorInfo());
//		}
//
//		return jep.getValueAsObject();
//
//	}


	/**
	 * Convert weasel object array to simple object array
	 * 
	 * @param array weasel object array
	 * @return object array
	 */
	public static Object[] w2s(WeaselObject... array) {
		Object[] out = new Object[array.length];
		int id = 0;
		for (WeaselObject obj : array) {
			out[id++] = obj.get();
		}
		return out;
	}

	/**
	 * Convert simple object array to weasel object array
	 * 
	 * @param array object array
	 * @return weasel object array
	 */
	public static WeaselObject[] s2w(Object... array) {
		WeaselObject[] out = new WeaselObject[array.length];
		int id = 0;
		for (Object obj : array) {
			out[id++] = WeaselObject.getWrapperForValue(obj);
		}
		return out;
	}


	/**
	 * Convert an object object to a boolean.
	 * 
	 * @param obj object (WeaselInteger, WeaselBoolean, integer, boolean, long,
	 *            float, double)
	 * @return boolean value
	 */
	public static boolean toBoolean(Object obj) {

		if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else if (obj instanceof Integer) {
			return ((Integer) obj) != 0;
		} else if (obj instanceof Double) {
			return (int) Math.round((Double) obj) != 0;
		} else if (obj instanceof Float) {
			return Math.round((Float) obj) != 0;
		} else if (obj instanceof WeaselBoolean) {
			return ((WeaselBoolean) obj).get();
		} else if (obj instanceof WeaselInteger) {
			return ((WeaselInteger) obj).get() != 0;
		} else if (obj instanceof Long) {
			return ((Long) obj) != 0;
		} else if (obj instanceof String) {
			return ((String) obj).equalsIgnoreCase("true") || ((String) obj).equalsIgnoreCase("1");
		} else if (obj instanceof WeaselString) {
			return ((WeaselString) obj).get().equalsIgnoreCase("true") || ((WeaselString) obj).get().equalsIgnoreCase("1");
		}

		throw new RuntimeException("Unable to convert " + obj + " to Boolean.");

	}

	private static final Pattern hex = Pattern.compile("0x([0-9a-zA-Z]+)");
	private static final Pattern bin = Pattern.compile("0b([0-9a-zA-Z]+)");


	/**
	 * Convert all 0x007f and 0b100101 to decimal format in give string.
	 * 
	 * @param str string
	 * @return converted string
	 * @throws ParseException if there's a number starting with 0b or 0x, and
	 *             does not have the correct format.
	 */
	public static String convertNumbersToDecimal(String str) throws ParseException {

		StringBuffer sb = new StringBuffer();
		Matcher matcher;

		if (str.contains("0x")) {

			matcher = hex.matcher(str);

			while (matcher.find()) {
				String group = matcher.group(1);
				try {
					Integer out = Integer.parseInt(group, 16);
					matcher.appendReplacement(sb, out.toString());
				} catch (NumberFormatException nfe) {
					throw new ParseException("0x" + group + " is not a valid hex number.");
				}
			}

			matcher.appendTail(sb);

			str = sb.toString();
		}

		if (str.contains("0b")) {
			sb.setLength(0);

			matcher = bin.matcher(str);

			while (matcher.find()) {
				String group = matcher.group(1);
				try {
					Integer out = Integer.parseInt(group, 2);
					matcher.appendReplacement(sb, out.toString());
				} catch (NumberFormatException nfe) {
					throw new ParseException("0b" + group + " is not a valid bin number.");
				}
			}

			matcher.appendTail(sb);

			str = sb.toString();
		}

		return str;
	}


	/**
	 * Convert an object object to an integer. Booleans are turned into 0 or 1.
	 * 
	 * @param obj object (WeaselInteger, WeaselBoolean, integer, boolean, long,
	 *            float, double)
	 * @return integer value
	 */
	public static int toInteger(Object obj) {
		if (obj == null) return 0;

		if (obj instanceof Integer) {
			return ((Integer) obj);
		} else if (obj instanceof Boolean) {
			return ((Boolean) obj) ? 1 : 0;
		} else if (obj instanceof Double) {
			return (int) Math.round((Double) obj);
		} else if (obj instanceof Float) {
			return Math.round((Float) obj);
		} else if (obj instanceof WeaselInteger) {
			return ((WeaselInteger) obj).get();
		} else if (obj instanceof WeaselBoolean) {
			return ((WeaselBoolean) obj).get() ? 1 : 0;
		} else if (obj instanceof Long) {
			return ((Integer) obj);
		} else if (obj instanceof String) {
			try {
				return Integer.parseInt((String) obj);
			} catch (NumberFormatException e) {}
		} else if (obj instanceof WeaselString) {
			try {
				return Integer.parseInt(((WeaselString) obj).get());
			} catch (NumberFormatException e) {}
		}

		throw new RuntimeException("Unable to convert " + obj + " to Integer.");

	}

	/**
	 * Convert an object to String. For weasel objects, converts only the
	 * wrapped value to string.
	 * 
	 * @param obj object (WeaselInteger, WeaselBoolean, WeaselString, integer,
	 *            boolean, long, float, double, string)
	 * @return the string value
	 */
	public static String toString(Object obj) {

		if (obj == null) return "null";

		if (obj instanceof WeaselBoolean) {
			return ((WeaselBoolean) obj).get() ? "true" : "false";
		} else if (obj instanceof WeaselInteger) {
			return ((WeaselInteger) obj).get() + "";
		} else if (obj instanceof WeaselString) {
			return ((WeaselString) obj).get();
		} else if (obj instanceof Boolean) {
			return ((Boolean) obj) ? "true" : "false";
		} else {
			return obj.toString();
		}

	}

	/**
	 * Format an integer (plain or wrapped in WeaselInteger, or a boolean, or
	 * long) using given radix, and output as string.
	 * 
	 * @param obj the integer
	 * @param base 2=binary, 8=octal, 16=hex, 10=decimal.
	 * @return the formatted integer
	 */
	public static String formatIntegerBase(Object obj, int base) {
		Integer i = 0;
		if (obj instanceof Integer) i = (Integer) obj;
		if (obj instanceof Long) i = (Integer) obj;
		if (obj instanceof Float) i = Math.round((Float) obj);
		if (obj instanceof Double) i = (int) Math.round((Double) obj);
		if (obj instanceof Boolean) i = (Boolean) obj ? 1 : 0;
		if (obj instanceof WeaselInteger) i = ((WeaselInteger) obj).get();
		if (obj instanceof WeaselBoolean) i = ((WeaselBoolean) obj).get() ? 1 : 0;

		switch (base) {
			case 2:
				return "0b" + Integer.toBinaryString(i);
			case 8:
				return "0c" + Integer.toOctalString(i);
			case 16:
				return "0x" + Integer.toHexString(i);
			case 10:
				return "0d" + Integer.toString(i);
			default:
				return "0d" + i;
		}
	}

	/**
	 * Exception during evaluating expression
	 * 
	 * @author MightyPork
	 * @copy (c) 2012
	 */
	public static class CalcException extends WeaselRuntimeException {

		/** Description why the evaluation failed. */
		public String cause;

		/**
		 * @param e causing expression.
		 */
		public CalcException(RuntimeException e) {
			super(e);
		}

		/**
		 * @param cause cause description
		 */
		public CalcException(String cause) {
			super(cause);
		}
	}

	/**
	 * Check if number is in given range.
	 * 
	 * @param checked checked number
	 * @param from range lower boundary (included)
	 * @param to range upper boundary (included)
	 * @return is in range
	 */
	public static final boolean isInRange(int checked, int from, int to) {
		return checked >= from && checked <= to;
	}

}
