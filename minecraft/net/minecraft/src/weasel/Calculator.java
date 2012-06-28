package net.minecraft.src.weasel;


import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.minecraft.src.weasel.obj.WeaselBoolean;
import net.minecraft.src.weasel.obj.WeaselInteger;
import net.minecraft.src.weasel.obj.WeaselObject;
import net.minecraft.src.weasel.obj.WeaselVariableMap;


/**
 * Expression evaluation class
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class Calculator {
	private static ScriptEngineManager engineFactory = new ScriptEngineManager();

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

	/**
	 * @param engine the script engine
	 * @param javascript script to evaluate
	 * @return result object
	 * @throws CalcException when evaluation fails.
	 */
	public static Object eval(ScriptEngine engine, String javascript) throws CalcException {
		try {
			return engine.eval(javascript);
		} catch (ScriptException e) {
			throw new CalcException(e);
		}
	}

	/**
	 * Evaluate an expression with variables from VM.
	 * 
	 * @param expression expression, without semicolon. eg. (14+a)/2;
	 * @param engine weasel engine
	 * @return result of the expression.
	 * @throws CalcException when evaluation fails.
	 */
	public static Object eval(String expression, WeaselEngine engine) throws CalcException {

		if (expression.contains(";")) {
			throw new CalcException("Semicolon in a numeric expression. Possible injection attack.");
		}

		ScriptEngine jsEngine = engineFactory.getEngineByName("JavaScript");

		List<String> varsNeeded = new ArrayList<String>();
		Matcher matcher = Pattern.compile("([a-zA-Z_]{1}[a-zA-Z_0-9.]*?)").matcher(expression);
		while(matcher.find()){
		    String name = matcher.group(1);
		    varsNeeded.add(name);
		    System.out.println("varNeeded: "+name);
		    jsEngine.put(name, engine.getVariable(name).get());
		}

		Object out = eval(jsEngine, expression);

		for (String varname : varsNeeded) {
			engine.getVariable(varname).set(jsEngine.get(varname));	
		}

		return out;

	}
	
	/**
	 * Evaluate an expression with variables from VM.
	 * 
	 * @param expression expression, without semicolon. eg. (14+a)/2;
	 * @param varmap map of variables
	 * @return result of the expression.
	 * @throws CalcException when evaluation fails.
	 */
	public static Object eval(String expression, WeaselVariableMap varmap) throws CalcException {

		if (expression.contains(";")) {
			throw new CalcException("Semicolon in a numeric expression. Possible injection attack.");
		}

		ScriptEngine engine = engineFactory.getEngineByName("JavaScript");

		for (Entry<String, WeaselObject> entry : varmap.map.entrySet()) {
			if (expression.contains(entry.getKey())) {
				engine.put(entry.getKey(), entry.getValue().get());
			}
		}

		Object out = eval(engine, expression);

		for (Entry<String, WeaselObject> entry : varmap.map.entrySet()) {
			if (expression.contains(entry.getKey())) {
				entry.getValue().set(engine.get(entry.getKey()));
			}
		}

		return out;

	}

	/**
	 * Exception during evaluating expression
	 * 
	 * @author MightyPork
	 * @copy (c) 2012
	 */
	public static class CalcException extends RuntimeException {

		/** Description why the evaluation failed. */
		public String cause;

		/**
		 * @param e causing expression.
		 */
		public CalcException(ScriptException e) {
			super(e);
		}

		/**
		 * @param cause cause description
		 */
		public CalcException(String cause) {
			this.cause = cause;
		}
	}
}
