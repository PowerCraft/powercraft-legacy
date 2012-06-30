package net.minecraft.src.weasel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.src.weasel.exception.WeaselRuntimeException;
import net.minecraft.src.weasel.obj.WeaselBoolean;
import net.minecraft.src.weasel.obj.WeaselInteger;
import net.minecraft.src.weasel.obj.WeaselString;

import org.nfunk.jep.JEP;


/**
 * Expression evaluation class
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class Calculator {
	private static Pattern variablePattern = Pattern.compile("([a-zA-Z_]{1}[a-zA-Z_0-9.]*?)(?:[^a-zA-Z_0-9.(]|$)");

	/**
	 * Format an integer (plain or wrapped in WeaselInteger, or a boolean, or
	 * long) using given radix, and output as string.
	 * 
	 * @param obj the integer
	 * @param radix radix. 2=binary,8=octal,16=hex,10=decimal.
	 * @return the formatted integer
	 */
	public static String formatInteger(Object obj, int radix) {
		Integer i = 0;
		if (obj instanceof Integer) i = (Integer) obj;
		if (obj instanceof Long) i = (Integer) obj;
		if (obj instanceof Float) i = Math.round((Float) obj);
		if (obj instanceof Double) i = (int) Math.round((Double) obj);
		if (obj instanceof Boolean) i = (Boolean) obj ? 1 : 0;
		if (obj instanceof WeaselInteger) i = ((WeaselInteger) obj).get();
		if (obj instanceof WeaselBoolean) i = ((WeaselBoolean) obj).get() ? 1 : 0;

		switch (radix) {
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

	public static boolean toBoolean(Object obj) {
		if (obj instanceof WeaselInteger) {
			return ((WeaselInteger) obj).get() != 0;
		}

		if (obj instanceof Double) {
			return (int) Math.round((Double) obj) != 0;
		}

		if (obj instanceof Float) {
			return Math.round((Float) obj) != 0;
		}

		if (obj instanceof Long) {
			return ((Long) obj) != 0;
		}
		if (obj instanceof WeaselBoolean) {
			return ((WeaselBoolean) obj).get();
		}

		if (obj instanceof Integer) {
			return ((Integer) obj) != 0;
		}

		if (obj == null || !(obj instanceof Boolean)) {
			throw new RuntimeException("Trying to convert " + obj + " to boolean.");
		}

		return (Boolean) obj;
	}

	public static int toInteger(Object obj) {
		if (obj instanceof WeaselInteger) {
			return ((WeaselInteger) obj).get();
		}

		if (obj instanceof Double) {
			return (int) Math.round((Double) obj);
		}

		if (obj instanceof Float) {
			return Math.round((Float) obj);
		}

		if (obj instanceof Long) {
			return ((Integer) obj);
		}
		if (obj instanceof WeaselBoolean) {
			return ((WeaselBoolean) obj).get() ? 1 : 0;
		}

		if (obj instanceof Integer) {
			return ((Integer) obj);
		}

		if (obj == null || !(obj instanceof Integer)) {
			throw new RuntimeException("Trying to convert " + obj + " to integer.");
		}

		return (Integer) obj;
	}

	public static String toString(Object obj) {
		if (obj instanceof WeaselInteger) {
			return ((WeaselInteger) obj).get() + "";
		}

		if (obj instanceof Double) {
			return obj + "";
		}

		if (obj instanceof Float) {
			return obj + "";
		}

		if (obj instanceof Long) {
			return obj + "";
		}
		if (obj instanceof WeaselBoolean) {
			return ((WeaselBoolean) obj).get() ? "true" : "false";
		}

		if (obj instanceof Integer) {
			return obj + "";
		}

		if (obj instanceof WeaselString) {
			return ((WeaselString) obj).get();
		}

		if (obj == null || !(obj instanceof String)) {
			throw new RuntimeException("Trying to convert " + obj + " to integer.");
		}

		return (String) obj;
	}



	public static Object evalSimple(String expression, Map<String, Object> vars) {

		JEP jep = new JEP();
		for (Entry<String, Object> entry : vars.entrySet()) {
			jep.addVariable(entry.getKey(), entry.getValue());
		}

		jep.parseExpression(expression);
		if (jep.hasError()) {
			System.out.println(jep.getErrorInfo());
			throw new CalcException(jep.getErrorInfo());
		}

		return jep.getValueAsObject();

	}



	/**
	 * Evaluate an expression with variables from VM.
	 * 
	 * @param expression expression, without semicolon. eg. (14+a)/2;
	 * @param variableContainer weasel variable container (primary ENGINE, but
	 *            sometimes also VariableMap).
	 * @return result of the expression.
	 * @throws CalcException when evaluation fails.
	 */
	public static Object eval(String expression, IVariableContainer variableContainer) throws CalcException {

		if (expression == null || expression.length() == 0) return null;

		if (expression.contains(";")) {
			throw new CalcException("Semicolon in a numeric expression. Possible injection attack.");
		}

		expression = expression.replaceAll("\\s", "");

		// List of variables needed to evaluate this expression
		List<String> varsNeeded = new ArrayList<String>();

		// List of renamed variables (. -> __)
		Map<String, String> rename = new HashMap<String, String>();

		JEP jep = new JEP();
		jep.setAllowAssignment(true);

		Matcher matcher = variablePattern.matcher(expression);

		while (matcher.find()) {

			String name = matcher.group(1);
			String real = name;
			varsNeeded.add(name);

			//if name contains dot, replace it by __ to prevent "no such object" errors in JS
			if (name.contains(".")) {
				real = name;
				name = name.replaceAll("\\.", "__");
				rename.put(real, name);
				expression = expression.replace(real, name);
			}

			//add the variable into JS engine
			try {
				jep.addVariable(name, variableContainer.getVariable(real).get());
				//jsEngine.put(name, variableContainer.getVariable(real).get());
			} catch (NullPointerException npe) {
				throw new WeaselRuntimeException("Variable " + real + " not set in this scope.");
			}
		}

		jep.parseExpression(expression);

		// execute JS
		Object out = jep.getValueAsObject(); //eval(jsEngine, expression);

		if (jep.hasError()) {
			System.out.println(jep.getErrorInfo());
			throw new CalcException(jep.getErrorInfo());
		}

		// put variables back into Variable Map in Engine
		for (String varname : varsNeeded) {
			String real = varname;
			if (rename.containsKey(varname)) real = rename.get(varname);

			variableContainer.getVariable(real).set(jep.getVarValue(varname));
		}

		return out;

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
}
