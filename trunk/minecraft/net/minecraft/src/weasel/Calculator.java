package net.minecraft.src.weasel;


import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.src.PC_Struct3;
import net.minecraft.src.weasel.exception.WeaselRuntimeException;
import net.minecraft.src.weasel.lang.Instruction;
import net.minecraft.src.weasel.lang.InstructionAssignRetval;
import net.minecraft.src.weasel.lang.InstructionCall;
import net.minecraft.src.weasel.obj.WeaselBoolean;
import net.minecraft.src.weasel.obj.WeaselInteger;
import net.minecraft.src.weasel.obj.WeaselString;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;


/**
 * Expression evaluation class
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class Calculator {

	private static final List<String> parserConstants = new ArrayList<String>();
	private static final List<String> parserFunctions = new ArrayList<String>();

	static {
		JEP jep = JEP.createWeaselParser(false);
		
		for (String str : jep.getConstants()) {
			parserConstants.add(str);
		}
		
		for (String str : jep.getKeywords()) {
			parserFunctions.add(str);
		}
		
		jep = null;
	}


	private static final Pattern variablePattern = Pattern.compile("(?:[^a-zA-Z_0-9]|^)([a-zA-Z_]{1}[a-zA-Z_0-9.]*?)(?:[^a-zA-Z_0-9.(]|$)");
	private static final Pattern stringPattern = Pattern.compile("(\"[^\"]*?\")");

	/**
	 * Evaluate an expression with variables from VM.
	 * 
	 * @param expression expression, without semicolon. eg. (14+a)/2
	 * @param variableContainer weasel variable container (primary ENGINE, but
	 *            sometimes also VariableMap).
	 * @return result of the expression.
	 * @throws CalcException when evaluation fails.
	 */
	public static Object evaluate(String expression, IVariableContainer variableContainer) throws CalcException {

		if (expression == null || expression.length() == 0) return null;

		if (expression.contains(";")) {
			throw new CalcException("CALC evaluate - unexpected \";\" in a numeric expression.");
		}

		expression = correctQuotes(expression);

		Matcher matcher;

		// List of variables needed to evaluate this expression
//		List<String> varsNeeded = new ArrayList<String>();

		JEP jep = JEP.createWeaselParser(false);
		matcher = variablePattern.matcher(expression);

		while (matcher.find()) {
			String name = matcher.group(1);

			if (parserConstants.contains(name)) continue;

//			varsNeeded.add(name);

			try {
				//add variable into JEP
				jep.addVariable(name, variableContainer.getVariable(name).get());
			} catch (NullPointerException npe) {
				throw new WeaselRuntimeException("CALC evaluate - variable \"" + name + "\" not set in this scope.");
			}
		}
		matcher = null;


		//evaluate
		jep.parseExpression(expression);

		Object out = jep.getValueAsObject();

		if (jep.hasError()) {
			throw new CalcException(jep.getErrorInfo());
		}

//		// put variables back into Variable Container
//		for (String name : varsNeeded) {
//			variableContainer.setVariable(name, jep.getVarValue(name));
//		}

		//return expression result
		return out;

	}
	
	private static String correctQuotes(String expression) {
		return expression.replaceAll("'", "\"");
	}
	
	/**
	 * Remove whitespace characters from the string, preserving spaces in strings ("this is string")
	 * @param expression the input expression
	 * @return processed expression
	 */
	public static String removeWhitespaceStringSafe(String expression) {
		
		Matcher matcher;

		// take all strings away to make variable parsing easier		
		// strings are stored in "replace" map, together with replacement keys
		HashMap<String, String> replacedStrings = new HashMap<String, String>();
		int stringReplaceCounter = 0;

		if (expression.contains("\"")) {
			matcher = stringPattern.matcher(expression);
			StringBuffer sb = new StringBuffer();

			while (matcher.find()) {
				String str = matcher.group(1);
				String repl = "[@" + stringReplaceCounter + "]";

				replacedStrings.put(repl, str);
				matcher.appendReplacement(sb, repl);
			}

			matcher.appendTail(sb);
			expression = sb.toString();
		}
		
		expression = expression.replaceAll("\\s", "");	
		
		//put back replaced strings.
		if (replacedStrings.size() > 0) {
			for (Entry<String, String> entry : replacedStrings.entrySet()) {
				expression = expression.replace(entry.getKey(), entry.getValue());
			}
		}
		
		return expression;
		
	}



	/**
	 * Evaluate an expression using variables from a given map.<br>
	 * It is fast, but does not support Weasel variables and does not check what
	 * variables are needed.
	 * 
	 * @param expression the expression
	 * @param vars map of variables (string → variable)
	 * @return result value
	 * @throws CalcException when the evaluation fails
	 */
	public static Object evaluateSimple(String expression, Map<String, Object> vars) {

		JEP jep = JEP.createWeaselParser(false);
		for (Entry<String, Object> entry : vars.entrySet()) {
			jep.addVariable(entry.getKey(), entry.getValue());
		}

		jep.parseExpression(expression);

		if (jep.hasError()) {
			throw new CalcException(jep.getErrorInfo());
		}

		return jep.getValueAsObject();

	}
	

	private static final Pattern functionPattern = Pattern.compile("([a-zA-Z_]{1}[a-zA-Z_0-9.]*?)\\(([^(]*?)\\)");


	/**
	 * Extracts non-math function calls from given expression and creates a list
	 * of instructions needed to calculate final value.
	 * 
	 * @param expression the expression to parse
	 * @param tmpCounter counter used to create names for TMP variables.
	 * @return Structure of:
	 *         <ul>
	 *         <li>Name of the final variable
	 *         <li>State of tmpCounter after processing the expression
	 *         <li>List of instructions needed to call in order to get correct
	 *         result.
	 *         </ul>
	 */
	public static PC_Struct3<String, Integer, List<Instruction>> expandExpression(String expression, int tmpCounter) {

		
		Matcher matcher;

		// take all strings away to make variable parsing easier		
		// strings are stored in "replace" map, together with replacement keys
		HashMap<String, String> replacedStrings = new HashMap<String, String>(5);
		int stringReplaceCounter = 0;

		if (expression.contains("\"")) {
			matcher = stringPattern.matcher(expression);
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
		
		//get rid of whitespace
		expression = expression.replaceAll("\\s", "");		
		
		List<Instruction> instrList = new ArrayList<Instruction>(3);
		
		StringBuffer sb = new StringBuffer();
		
		int functionsFoundThisTurn=-1;
		while(functionsFoundThisTurn != 0) {
			functionsFoundThisTurn = 0;			
			
			Matcher funcMatcher = functionPattern.matcher(expression);
			
			sb.setLength(0);
			while(funcMatcher.find()){
			    String name = funcMatcher.group(1);
				String args = funcMatcher.group(2);
			    
			    if(!parserFunctions.contains(name)) {
				    
				    String tmpvar = "_v" + tmpCounter++;
				    //System.out.print(""+tmpvar+" = "+name+"(");
				    
				    args = args.replace("[§", "(").replace("§]", ")");
				    
					//put back replaced strings.
					if (replacedStrings.size() > 0) {
						for (Entry<String, String> entry : replacedStrings.entrySet()) {
							args = args.replace(entry.getKey(), entry.getValue());
						}
					}
				    
				    String[] params = splitBracketSafe(args,',');
				    
//				    boolean first = true;
//				    for(String a : params){
//				    	if(first) {
//				    		first = false;
//				    	}else {
//				    		System.out.print(" $ ");
//				    	}
//				    	System.out.print("\""+a+"\"");
//				    }
//				    System.out.println(");");
				    
				    instrList.add(new InstructionCall(name,params));
				    instrList.add(new InstructionAssignRetval(tmpvar));
				    
				    funcMatcher.appendReplacement(sb, tmpvar);
				    
			    }else {
			    	funcMatcher.appendReplacement(sb, name+"[§"+args+"§]");
			    }
			    functionsFoundThisTurn++;
			}
			funcMatcher.appendTail(sb);
			
			expression = sb.toString();
		}
		
		//put back replaced strings.
		if (replacedStrings.size() > 0) {
			for (Entry<String, String> entry : replacedStrings.entrySet()) {
				expression = expression.replace(entry.getKey(), entry.getValue());
			}
		}
		expression = expression.replace("[§", "(").replace("§]", ")");
		//System.out.println(expression);
		return new PC_Struct3<String, Integer, List<Instruction>>(expression, tmpCounter, instrList);
	}
	
	
	private static String[] splitBracketSafe(String text, char delim) {
		
		StringReader sr = new StringReader(text);
		
		StringBuilder sb = new StringBuilder();
		
		List<String> pieces = new ArrayList<String>();
		
		int bracketLevel = 0;
		
		try {
			
			int tmp;
			while((tmp = sr.read()) != -1) {
				char letter = (char)tmp;
				
				if(letter == '(') {
					bracketLevel++;
				}else
				if(letter == ')') {
					bracketLevel--;
				}				
				
				if(letter == delim && bracketLevel == 0) {
					pieces.add(sb.toString());
					sb = new StringBuilder();
				}else {
					sb.append(letter);
				}
			}
			
			if(sb.length() > 0) pieces.add(sb.toString());
			
			return pieces.toArray(new String[pieces.size()]);
			
		} catch (IOException e) {
			throw new CalcException(e.getMessage());
		}
	}
	
	




	/**
	 * Convert an object object to a boolean.
	 * 
	 * @param obj object (WeaselInteger, WeaselBoolean, integer, boolean, long,
	 *            float, double)
	 * @return boolean value
	 */
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
			throw new RuntimeException("Unable to convert " + obj + " to Boolean.");
		}

		return (Boolean) obj;
	}



	/**
	 * Convert all 0x007f and 0b100101 to decimal format in give string.
	 * 
	 * @param str string
	 * @return converted string
	 * @throws ParseException if there's a number starting with 0b or 0x, and
	 *             does not have the correct format.
	 */
	public static String convertNumbersToDecimal(String str) throws ParseException {

		Pattern hex = Pattern.compile("0x([0-9a-zA-Z]+)");
		Pattern bin = Pattern.compile("0b([0-9a-zA-Z]+)");

		StringBuffer sb = new StringBuffer();
		Matcher matcher;


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
		sb = new StringBuffer();

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

		return sb.toString();
	}


	/**
	 * Convert an object object to an integer. Booleans are turned into 0 or 1.
	 * 
	 * @param obj object (WeaselInteger, WeaselBoolean, integer, boolean, long,
	 *            float, double)
	 * @return integer value
	 */
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
		if (obj instanceof Boolean) {
			return ((Boolean) obj) ? 1 : 0;
		}

		if (obj == null || !(obj instanceof Integer)) {
			throw new RuntimeException("Unable to convert " + obj + " to Integer.");
		}

		return (Integer) obj;
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
		if (obj instanceof Boolean) {
			return ((Boolean) obj) ? "true" : "false";
		}

		if (obj instanceof Integer) {
			return obj + "";
		}

		if (obj instanceof WeaselString) {
			return ((WeaselString) obj).get();
		}

		if (obj == null || !(obj instanceof String)) {
			throw new RuntimeException("Unable to convert " + obj + " to String.");
		}

		return (String) obj;
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

}
