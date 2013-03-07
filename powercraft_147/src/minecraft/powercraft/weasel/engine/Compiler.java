package powercraft.weasel.engine;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import powercraft.api.PC_Color;
import powercraft.api.PC_Struct2;
import powercraft.weasel.exception.SyntaxError;
import powercraft.weasel.exception.WeaselRuntimeException;
import powercraft.weasel.jep.JEP;
import powercraft.weasel.jep.ParseException;
import powercraft.weasel.lang.Instruction;
import powercraft.weasel.lang.InstructionAssign;
import powercraft.weasel.lang.InstructionAssignRetval;
import powercraft.weasel.lang.InstructionCall;
import powercraft.weasel.lang.InstructionEnd;
import powercraft.weasel.lang.InstructionFunction;
import powercraft.weasel.lang.InstructionGoto;
import powercraft.weasel.lang.InstructionIf;
import powercraft.weasel.lang.InstructionLabel;
import powercraft.weasel.lang.InstructionPause;
import powercraft.weasel.lang.InstructionPop;
import powercraft.weasel.lang.InstructionPush;
import powercraft.weasel.lang.InstructionRestart;
import powercraft.weasel.lang.InstructionReturn;
import powercraft.weasel.lang.InstructionStringCall;


/**
 * Weasel code compiler and parser.<br>
 * Converts source code into a sequence of Weasel's assembly instructions.
 * 
 * @author MightyPork
 */
public class Compiler {

	/** Pattern matching function headers in expressions. */
	//Pattern.compile("([a-zA-Z_]{1}[a-zA-Z_0-9.]*?)[(]((([(][^)]*?[)])*[^(]*)*)[)]");
	protected static final Pattern functionPattern = Pattern.compile("([a-zA-Z_]{1}[a-zA-Z_0-9.]*?)\\(([^(]*?)\\)");

	/**
	 * Pattern matching string constants in expressions.<br>
	 * It is always preferable to remove string constants before other parsing,
	 * as they tend to confuse the other parsers.
	 */
	protected static final Pattern stringPattern = Pattern.compile("(\"[^\"]*?\")");
	/**
	 * Pattern for finding strings, which does not include the quotes in the
	 * string.
	 */
	protected static final Pattern stringPatternNoQuotes = Pattern.compile("\"([^\"]*?)\"");

	private static final String variableInCodePatternRegexp = "([a-zA-Z_]{1}[a-zA-Z_0-9.]*)(?:[^a-zA-Z_0-9.(]|$|\n)";

	private static final String variableAlonePatternRegexp = "([a-zA-Z_]{1}[a-zA-Z_0-9.]*?)(?:[^a-zA-Z_0-9.(]|$|\n)";

//	private static final String mathExpressionRegexp = "[0-9a-zA-Z_.\\-+!(\"]{1}(?:.*?[0-9a-zA-Z_.\\)\"]{1})?";

	/** Pattern matching variables in an expression */
	public static final Pattern variableInCodePattern = Pattern.compile(variableInCodePatternRegexp);


	/**
	 * List of functions defined by Weasel's JEP, for syntax highlighting and
	 * parsing.
	 */
	public static final List<String> parserFunctions = new ArrayList<String>();

	/**
	 * List of constants defined by Weasel's JEP, for syntax highlighting and
	 * parsing.
	 */
	public static final List<String> parserConstants = new ArrayList<String>();

	/** List of language keywords, for syntax highlighting */
	public static final List<String> langKeywords = new ArrayList<String>();

	static {
		JEP jep = JEP.createWeaselParser(false);

		for (String str : jep.getConstants()) {
			Compiler.parserConstants.add(str);
		}

		for (String str : jep.getKeywords()) {
			Compiler.parserFunctions.add(str);
		}

		langKeywords.add("if");
		langKeywords.add("else");
		langKeywords.add("elseif");
		langKeywords.add("end");
		langKeywords.add("return");
		langKeywords.add("restart");
		langKeywords.add("for");
		langKeywords.add("while");
		langKeywords.add("do");
		langKeywords.add("until");
		langKeywords.add("call");
//		langKeywords.add("switch");
//		langKeywords.add("case");
		langKeywords.add("break");
		langKeywords.add("continue");
		langKeywords.add("function");
		langKeywords.add("push");
		langKeywords.add("pop");
		langKeywords.add("pause");
		langKeywords.add("global");

		jep = null;
	}


	/**
	 * Take all strings away to make variable parsing easier. Strings are stored
	 * in "replace" map, with with replacement keys.
	 * 
	 * @param expression expression to process
	 * @return structure of: output string, map of key→replacement pairs
	 */
	private String escapeStringConstants(String expression) {
		Matcher matcher;

		if (expression.contains("\"")) {

			int stringReplaceCounter = 0;
			matcher = Compiler.stringPattern.matcher(expression);
			StringBuffer sb = new StringBuffer(100);

			while (matcher.find()) {
				String str = matcher.group(1);
				String repl = replKeyStringStart + (stringReplaceCounter++) + replKeyStringEnd;

				str = PC_Color.convertMagicColors(str);
				stringReplacements.put(repl, str);
				matcher.appendReplacement(sb, repl);
			}

			matcher.appendTail(sb);
			expression = sb.toString();

		}

		return expression;

	}

	/**
	 * Replace place-holders back by the taken-away strings.
	 * 
	 * @param expression expression to process
	 * @return output string
	 */
	private String unescapeStringConstants(String expression) {
		if (expression.contains(replKeyStringStart) && stringReplacements != null) {
			if (stringReplacements.size() > 0) {
				for (Entry<String, String> entry : stringReplacements.entrySet()) {
					expression = expression.replace(entry.getKey(), entry.getValue());
				}
			}
		}
		return expression;
	}


	/**
	 * Extracts non-math function calls from given expression and creates a list
	 * of instructions needed to calculate final value.<br>
	 * Generated expressions and instructions are fixed to contain real string
	 * constants instead of string replacements.
	 * 
	 * @param expression the expression to parse
	 * @return Structure of:
	 *         <ul>
	 *         <li>Name of the final variable, or a simple math expression
	 *         containing no non-math functions.
	 *         <li>State of tmpCounter after processing the expression
	 *         <li>List of instructions needed to call in order to get correct
	 *         result.
	 *         </ul>
	 * @throws SyntaxError
	 */
	private PC_Struct2<String, List<Instruction>> expandExpression(String expression) throws SyntaxError {

		//get rid of whitespace

		expression = expression.replaceAll("\\s", "");

		List<Instruction> instrList = new ArrayList<Instruction>(3);

		if (expression.contains("(")) {
			StringBuffer sb = new StringBuffer();

			int functionsFoundThisTurn = -1;
			while (functionsFoundThisTurn != 0) {
				functionsFoundThisTurn = 0;

				Matcher funcMatcher = Compiler.functionPattern.matcher(expression);

				sb.setLength(0);
				while (funcMatcher.find()) {
					String name = funcMatcher.group(1);
					String args = funcMatcher.group(2);

					if (!Compiler.parserFunctions.contains(name)) {

						String tmpvar = makeTmpVar();

						args = args.replace(replRoundBracketStart, "(").replace(replRoundBracketEnd, ")");

						args = unescapeStringConstants(args);

						List<String> params = Compiler.splitBracketSafe(args, ',');

//						for (String param : params) {
//							if (!param.matches(mathExpressionRegexp)) {
//								throw new SyntaxError("Not a valid expression: " + param);
//							}
//						}

						if (name.equals("call")) {
							instrList.add(new InstructionStringCall(params.toArray(new String[params.size()])));
						} else {
							instrList.add(new InstructionCall(name, params.toArray(new String[params.size()])));
						}

						instrList.add(new InstructionAssignRetval(false, tmpvar));

						funcMatcher.appendReplacement(sb, tmpvar);

					} else {
						funcMatcher.appendReplacement(sb, name + replRoundBracketStart + args + replRoundBracketEnd);
					}
					functionsFoundThisTurn++;
				}
				funcMatcher.appendTail(sb);

				expression = sb.toString();
			}
		}

		expression = unescapeStringConstants(expression);

		expression = expression.replace(replRoundBracketStart, "(").replace(replRoundBracketEnd, ")");

//		if (!expression.matches(mathExpressionRegexp)) {
//			throw new SyntaxError("Not a valid expression: " + expression);
//		}

		return new PC_Struct2<String, List<Instruction>>(expression, instrList);

	}

	/**
	 * Split a text by given delimiter, making sure bracket blocks are complete
	 * - eg. hey(aa,bb),boo(heeee) → hey(aa,bb) AND boo(heeee)
	 * 
	 * @param text test to split
	 * @param delim delimiter, usually ","
	 * @return string pieces
	 */
	private static List<String> splitBracketSafe(String text, char delim) {

		StringReader sr = new StringReader(text);

		StringBuilder sb = new StringBuilder();

		List<String> pieces = new ArrayList<String>();

		int bracketLevel = 0;
		boolean stringOpen = false;

		try {

			int tmp;
			while ((tmp = sr.read()) != -1) {
				char letter = (char) tmp;

				if (letter == '(') {
					bracketLevel++;
				} else if (letter == ')') {
					bracketLevel--;
				}

				if (letter == '"') {
					stringOpen ^= true;
				}

				if (letter == delim && bracketLevel == 0 && !stringOpen) {
					pieces.add(sb.toString().trim());
					sb = new StringBuilder();
				} else {
					sb.append(letter);
				}
			}

			if (sb.length() > 0) pieces.add(sb.toString().trim());

			return pieces;

		} catch (IOException e) {
			throw new Calc.CalcException(e.getMessage());
		}
	}

	/**
	 * Read chars from a Reader, until given delimiter is read. Inner groups
	 * start-end are ignored. You must read the initial "start" character (if
	 * any) from the Reader before calling "readUntil", otherwise whole rest of
	 * the reader's string will be read.
	 * 
	 * @param reader the reader. Starting at the first character of the group
	 *            you want to read.
	 * @param start starting char - start of inner groups, ending with end too.
	 * @param end ending char
	 * @param blockName name of the processed block, used in thrown errors.
	 * @return the inner string between start and end. The end delimiter is
	 *         consumed.
	 * @throws SyntaxError
	 */
	private static String readUntil(Reader reader, Character start, Character end, String blockName) throws SyntaxError {


		try {

			StringBuilder sb = new StringBuilder();
			int level = 0;
			int tmpi;

			while ((tmpi = reader.read()) != -1) {

				char ch = (char) tmpi;

				if (ch == end) {
					level--;
					if (level == -1) break;
				}

				if (start != null && ch == start) {
					level++;
				}

				sb.append(ch);
			}

			if (tmpi == -1) {
				throw new SyntaxError("Reached end of block while reading " + blockName + ".");
			}

			return sb.toString();

		} catch (IOException e) {
			// An impossible error
			throw new WeaselRuntimeException(e);
		}

	}



	private static final String replKeyStringStart = "\u2192";
	private static final String replKeyStringEnd = "\u2190";
	private static final String replRoundBracketStart = "\u2191";
	private static final String replRoundBracketEnd = "\u2193";

	private static final String tmpVarPrefix = "_v";
	private static final String tmpLabelPrefix = "_";

	/**
	 * Map of string replacements, must be replaced back each time an
	 * instruction is generated.
	 */
	private Map<String, String> stringReplacements = new HashMap<String, String>();

	/** Stack of labels callable to continue a loop */
	private Stack<String> loopLabelsContinue = new Stack<String>();

	/** Stack of labels callable to break a loop. */
	private Stack<String> loopLabelsBreak = new Stack<String>();


	private boolean isLib = false;

	/**
	 * Compile source code to a list of instructions.<br>
	 * It is not legal to concatenate lists from multiple "compile" calls,
	 * unless they were called on the same instance of Compiler.
	 * 
	 * @param source source code
	 * @param tmpIdentifiersMoreUnique set to false to count 0,1,2, to true for timestamp based keys.
	 * @return list of instructions to run in the engine
	 * @throws SyntaxError when there's a syntax error in given code. Use
	 *             getMessage() to get user-friendly info.
	 */
	public List<Instruction> compile(String source, boolean tmpIdentifiersMoreUnique) throws SyntaxError {
		isLib = tmpIdentifiersMoreUnique;
		
		
		
		// DO NOT! There may be It's or doesn't, and we need these apostrophes!
		//source = source.replace("'", "\"");

		source = escapeStringConstants(source);

		source = source.replaceAll("//.*?\n", "");
		source = source.replaceAll("#.*?\n", "");
		source = source.replace("\n", " ");
		source = source.replaceAll("/\\*.*?\\*/", "");

		// convert & to && and | to ||
		source = source.replaceAll("([^&])[&]([^&])", "$1&&$2");
		source = source.replaceAll("([^|])[|]([^|])", "$1||$2");

		// add missing semicolon to for
		source = source.replaceAll("([^a-zA-Z0-9._]|^)for[(](.*?)[)][{]", "$1for($2;){");

		// ++ and --, but it works only outside other expressions.
		source = source.replaceAll("([^a-zA-Z0-9._]|^)([a-zA-Z_.]{1}[a-zA-Z0-9_.]*?)\\+\\+\\s*;", "$1$2 = ($2) + 1;");
		source = source.replaceAll("([^a-zA-Z0-9._]|^)([a-zA-Z_.]{1}[a-zA-Z0-9_.]*?)\\-\\-\\s*;", "$1$2 = ($2) - 1;");

		// short operators, *=, +=, -=, /=, *=, ^=
		source = source.replaceAll("([^a-zA-Z0-9._]|^)([a-zA-Z_.]{1}[a-zA-Z0-9_.]*?)\\s*([+\\-*/%^]{1})=(.*?);", "$1$2 = $2 $3 ($4);");

		// short bitwise, &= and |=
		source = source.replaceAll("([^a-zA-Z0-9._]|^)([a-zA-Z_.]{1}[a-zA-Z0-9_.]*?)\\s*&=(.*?);", "$1$2 = bw.and($2,$3);");
		source = source.replaceAll("([^a-zA-Z0-9._]|^)([a-zA-Z_.]{1}[a-zA-Z0-9_.]*?)\\s*\\|=(.*?);", "$1$2 = bw.or($2,$3);");

		// enclose arguments in brackets for return, push, pop and unset
		source = source.replaceAll("([^a-zA-Z0-9._]|^)(return|push|pop|unset)\\s*(.*?)\\s*;", "$1$2($3);");

		//remove brackets from break and continue - they cant have any calculations there anyway
		source = source.replaceAll("([^a-zA-Z0-9._]|^)(break|continue)\\s*[(](.*?)[)];", "$1$2 $3;");


		// remove () from end and pause
		source = source.replaceAll("([^a-zA-Z0-9._]|^)(end|pause|restart)\\s*[(]\\s*[)]\\s*;", "$1$2;");

//		 add quotes to isset
//		source = source.replaceAll("([^a-zA-Z0-9._]|^)(isset)\\s*[(]\\s*([a-zA-Z_.]{1}[a-zA-Z0-9_.]*?)\\s*[)]\\s*", "$1$2(\"$3\")");


		try {
			source = Calc.convertNumbersToDecimal(source);
		} catch (ParseException e1) {
			throw new SyntaxError(e1.getMessage());
		}


		// Replace string constants by replacement marks, each time 
		// an instruction is created, replace it back.


		source = source.replaceAll("[\\s\\n]+", " ");

		try {

			return parseCodeBlock(source);

		} catch (IOException e) {
			// an impossible error.
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Compile block of source code to instructions.
	 * 
	 * @param source source code
	 * @return list of instructions
	 * @throws IOException impossible, yet throws by reader
	 * @throws SyntaxError error in source code syntax
	 */
	private List<Instruction> parseCodeBlock(String source) throws IOException, SyntaxError {

		//empty block; return empty List to prevent null pointers and other funny things
		if (source.trim().length() == 0) return new ArrayList<Instruction>(0);

		// List of program instructions
		List<Instruction> instructionList = new ArrayList<Instruction>(20);

		// source code reader
		StringReader reader = new StringReader(source);


		StringBuilder sb = new StringBuilder();

		int chi;
		while ((chi = reader.read()) != -1) {
			char ch = (char) chi;


			// assignment.
			if (ch == '=') {

				String lhs = sb.toString().trim();

				// get global modifier if present
				boolean global = false;

				// allow global modifier
				// also some stupid modifiers are skipped.
				//
				// let global var stupidVar = true;

				if (lhs.startsWith("let ")) {
					lhs = lhs.substring("let ".length()).trim();
				}

				if (lhs.startsWith("set ")) {
					lhs = lhs.substring("set ".length()).trim();
				}

				if (lhs.startsWith("global ")) {
					global = true;
					lhs = lhs.substring("global ".length()).trim();
				}

				if (lhs.startsWith("var ")) {
					lhs = lhs.substring("var ".length()).trim();
				}

				if (!lhs.matches(variableAlonePatternRegexp)) {
					throw new SyntaxError("Invalid variable name \"" + lhs + "\"");
				}


				//read the rest
				String expression = readUntil(reader, null, ';', "value assigned to variable " + lhs);

				PC_Struct2<String, List<Instruction>> expanded = expandExpression(expression.trim());

				instructionList.addAll(expanded.b);

				instructionList.add(new InstructionAssign(global, lhs, expanded.a));

				//reset string builder.
				sb.setLength(0);

				continue;

			} else if (ch == ';') {
				// end of a statement, this could be break, end or continue;

				String statement = sb.toString().trim();

				if (statement.startsWith("break")) {
					// a BREAK statement.

					statement = statement.substring("break".length()).trim();

					if (statement.length() > 0) {
						// break number;

						try {
							int stepsBack = Integer.parseInt(statement);

							if (stepsBack <= 0) throw new SyntaxError("Number of loops to break must be > 0 at \"break " + stepsBack + ";\".");

							String breakLabel = loopLabelsBreak.get(loopLabelsBreak.size() - stepsBack);

							instructionList.add(new InstructionGoto(breakLabel));

						} catch (NumberFormatException nfe) {
							throw new SyntaxError("Invalid or not constant number of loops to break at \"break " + statement + ";\".");
						} catch (EmptyStackException ese) {
							throw new SyntaxError("There are no open loops to break at \"break " + statement + ";\".");
						} catch (ArrayIndexOutOfBoundsException e) {
							throw new SyntaxError("Trying to break more loops than exist at \"break " + statement + ";\".");
						}

					} else {
						try {
							String breakLabel = loopLabelsBreak.peek();
							instructionList.add(new InstructionGoto(breakLabel));
						} catch (EmptyStackException ese) {
							throw new SyntaxError("There are no loops to break at \"break;\".");
						}
					}

				} else if (statement.startsWith("continue")) {
					// a CONTINUE statement.

					statement = statement.substring("continue".length()).trim();

					if (statement.length() > 0) {
						// continue number;

						try {
							int stepsBack = Integer.parseInt(statement);

							if (stepsBack <= 0) throw new SyntaxError("Number of loops affected must be > 0 at \"continue " + stepsBack + ";\".");

							String continueLabel = loopLabelsContinue.get(loopLabelsContinue.size() - stepsBack);

							instructionList.add(new InstructionGoto(continueLabel));

						} catch (NumberFormatException nfe) {
							throw new SyntaxError("Invalid or not constant number of affected loops at \"continue " + statement + ";\".");
						} catch (EmptyStackException ese) {
							throw new SyntaxError("There are no open loops to affect at \"continue " + statement + "\".");
						} catch (ArrayIndexOutOfBoundsException e) {
							throw new SyntaxError("Trying to affect more open loops than exist at \"continue " + statement + ";\".");
						}

					} else {
						try {
							String continueLabel = loopLabelsContinue.peek();
							instructionList.add(new InstructionGoto(continueLabel));
						} catch (EmptyStackException ese) {
							throw new SyntaxError("There are no open loops to continue at \"continue;\".");
						}
					}

				} else if (statement.equalsIgnoreCase("end")) {
					// END command.

					instructionList.add(new InstructionEnd());

				} else if (statement.equalsIgnoreCase("pause")) {
					// PAUSE command.

					instructionList.add(new InstructionPause());

				} else if (statement.equalsIgnoreCase("restart")) {
					// RESTART command.

					instructionList.add(new InstructionRestart());

				} else {
					if (!statement.equals("")) throw new SyntaxError("Invalid statement at \"" + statement + ";\".");
				}

				//reset string builder.
				sb.setLength(0);

				continue;



				// opening bracket after some statement
			} else if (ch == '(') {

				// IF block
				if (sb.toString().trim().equalsIgnoreCase("if")) {

					// if(

					//discard current contents of the buffer
					sb.setLength(0);

					//read the condition
					String condition = readUntil(reader, '(', ')', "IF condition").trim();

					//get character after condition, skip white
					chi = readNextBlack(reader);
					if (chi == -1) throw new SyntaxError("Reached end of block while reading IF body.");
					ch = (char) chi;


					if (ch == '{') {
						//if(condition){statement}

						// read the rest of block:
						// {...}
						String trueBranch = readUntil(reader, '{', '}', "IF body").trim();

						String endLabel = makeTmpLabel();
						String elseLabel = makeTmpLabel();

						PC_Struct2<String, List<Instruction>> tmp = expandExpression(condition);

						instructionList.addAll(tmp.b);

						// condition
						instructionList.add(new InstructionIf(tmp.a, "", elseLabel));

						instructionList.addAll(parseCodeBlock(trueBranch));

						instructionList.add(new InstructionGoto(endLabel));

						instructionList.add(new InstructionLabel(elseLabel));

						// mark the position, we may return back to this point if there are no valid else statements!

						reader.mark(0);

						StringBuilder sb1 = new StringBuilder();

						elseread:
						while (true) {

							int chi1;



							//clear buffer
							sb1.setLength(0);

							while ((chi1 = readNextBlack(reader)) != -1) {

								char ch1 = (char) chi1;

								if (ch1 == '(') {
									// opening bracket, it could be start of else if condition!

									// is it else if?
									if (sb1.toString().equalsIgnoreCase("elseif") || sb1.toString().equalsIgnoreCase("else if")) {

										// else if(condition){...}

										//read the else if condition
										String condition1 = readUntil(reader, '(', ')', "ELSE IF condition");

//										int chi2 = readNextBlack(reader);
//										if (chi2 == -1) throw new SyntaxError("Reached end of block while reading ELSE IF body.");
//										char ch2 = (char) chi2;

										assertNextBlack(reader, '{', "ELSE IF body", "ELSE IF condition must be followed by \"{\".");

										// body of else if
										String trueBranch1 = readUntil(reader, '{', '}', "ELSE IF body");

										String elseLabel1 = makeTmpLabel();

										tmp = expandExpression(condition1);

										// if with it's required tmp calls
										instructionList.addAll(tmp.b);
										instructionList.add(new InstructionIf(tmp.a, "", elseLabel1));

										// parse stuff in the true branch
										instructionList.addAll(parseCodeBlock(trueBranch1));

										// end of if, go to the very end
										instructionList.add(new InstructionGoto(endLabel));

										// possible start of next else branch
										instructionList.add(new InstructionLabel(elseLabel1));

										// add mark, there does not have to be other branch
										reader.mark(0);

										continue elseread;



									} else {
										// opening bracket was not a start of else if, rewind back to the last } and end this IF structure
										reader.reset();
										break elseread;
									}

								} else if (ch1 == '{') {

									// it might be an else branch.
									if (sb1.toString().equalsIgnoreCase("else")) {
										// else{ ... }


										String trueBranch1 = readUntil(reader, '{', '}', "ELSE body");

										instructionList.addAll(parseCodeBlock(trueBranch1));

										break elseread;

									} else {
										// opening {, but not with "else" beforehand.
										// It could be DO or something other.

										// rewind back to the last } and end this IF structure
										reader.reset();
										break elseread;

									}


								} else if (ch1 == ';') {

									// end of some non-if statement, which followed after a } of the last IF's branch.
									// rewind back to the last } and end this IF structure

									reader.reset();
									break elseread;

								} else {

									// another character of else, if, or else if
									// append to tmp buffer
									sb1.append(ch1);

								}
							}

							if (chi1 == -1) {
								// end of block was reached while parsing IF's ELSE branches.
								// rewind back to the last }, as we've probably run into some non-if code.
								// end this IF.
								reader.reset();
								break elseread;
							}
						}

						// end of the IF structure
						instructionList.add(new InstructionLabel(endLabel));


					} else {

						// IF condition not followed by {

						// if(condition) statement;

						// read the rest of the line up to ";"
						String statement = ch + readUntil(reader, null, ';', "inline-IF body");

						String endLabel = makeTmpLabel();

						PC_Struct2<String, List<Instruction>> tmp = expandExpression(condition);

						instructionList.addAll(tmp.b);

						instructionList.add(new InstructionIf(tmp.a, "", endLabel));
						instructionList.addAll(parseCodeBlock(statement + ";"));
						instructionList.add(new InstructionLabel(endLabel));

					}

				} else if (sb.toString().trim().equalsIgnoreCase("while")) {
					//simple while loop

					// while(condition){ ... }
					sb.setLength(0);

					String inBracket = readUntil(reader, '(', ')', "WHILE condition");

					String labelBegin = makeTmpLabel();
					String labelEnd = makeTmpLabel();

					loopLabelsContinue.push(labelBegin);
					loopLabelsBreak.push(labelEnd);

					instructionList.add(new InstructionLabel(labelBegin));

					PC_Struct2<String, List<Instruction>> tmp = expandExpression(inBracket);
					instructionList.addAll(tmp.b);
					instructionList.add(new InstructionIf(tmp.a, "", labelEnd));

					assertNextBlack(reader, '{', "WHILE body", "WHILE condition must be followed by \"{\".");

					instructionList.addAll(parseCodeBlock(readUntil(reader, '{', '}', "WHILE body")));

					instructionList.add(new InstructionGoto(labelBegin));
					instructionList.add(new InstructionLabel(labelEnd));

					loopLabelsContinue.pop();
					loopLabelsBreak.pop();

				} else if (sb.toString().trim().equalsIgnoreCase("for")) {
					// the almighty FOR loop

					// for(initial code block; condition; iteration code block ){ ... }
					sb.setLength(0);

					String inBracket = readUntil(reader, '(', ')', "WHILE condition");


					List<String> args = splitBracketSafe(inBracket, ';');

					String labelBegin = makeTmpLabel();
					String labelPreiter = makeTmpLabel();
					String labelEnd = makeTmpLabel();

					loopLabelsContinue.push(labelPreiter);
					loopLabelsBreak.push(labelEnd);

					if (args.size() == 1) {

						String header = args.get(0);

						String init;
						String var;
						String start;
						String stop;
						if (header.matches("\\s*[a-zA-Z_.][a-zA-Z0-9_.]*?\\s*[=]\\s*[^:]+[:][^:]+")) {
							init = header.substring(0, header.indexOf(":"));

							var = header.substring(0, header.indexOf("="));
							start = header.substring(header.indexOf("=") + 1, header.indexOf(":"));
							stop = header.substring(header.indexOf(":") + 1);
						} else if (header.matches("[^:]+[:][^:]+")) {
							var = makeTmpVar();
							start = header.substring(0, header.indexOf(":"));
							stop = header.substring(header.indexOf(":") + 1);
							init = var + "=" + start;

						} else {
							throw new SyntaxError("Invalid condition block in FOR loop, at \"" + header + "\"");
						}

						// the initializer.
						instructionList.addAll(parseCodeBlock(init + ";"));
						String direction = makeTmpVar();
						instructionList.add(new InstructionAssign(false, direction, "-1+((" + start + ")<(" + stop + "))*2"));

						instructionList.add(new InstructionLabel(labelBegin));

						instructionList.add(new InstructionIf(var + "==(" + stop + ")+" + direction, labelEnd, ""));

						assertNextBlack(reader, '{', "FOR body", "FOR condition must be followed by \"{\".");
						instructionList.addAll(parseCodeBlock(readUntil(reader, '{', '}', "FOR body")));


						instructionList.add(new InstructionLabel(labelPreiter));
						instructionList.add(new InstructionAssign(false, var, var + " + " + direction));

					} else {
						String init = args.get(0);
						String cond = args.get(1);
						String iter = args.get(2);

						instructionList.addAll(parseCodeBlock(init + ";"));

						instructionList.add(new InstructionLabel(labelBegin));
						instructionList.add(new InstructionIf(cond, "", labelEnd));

						assertNextBlack(reader, '{', "FOR body", "FOR condition must be followed by \"{\".");
						instructionList.addAll(parseCodeBlock(readUntil(reader, '{', '}', "FOR body")));

						instructionList.add(new InstructionLabel(labelPreiter));
						instructionList.addAll(parseCodeBlock(iter + ";"));

					}

					instructionList.add(new InstructionGoto(labelBegin));
					instructionList.add(new InstructionLabel(labelEnd));

					loopLabelsContinue.pop();
					loopLabelsBreak.pop();

				} else if (sb.toString().trim().equalsIgnoreCase("until")) {

					//simple until loop, WHILE with negated condition

					// until(condition){ ... }
					sb.setLength(0);

					String inBracket = readUntil(reader, '(', ')', "UNTIL condition");

					String labelBegin = makeTmpLabel();
					String labelEnd = makeTmpLabel();

					loopLabelsContinue.push(labelBegin);
					loopLabelsBreak.push(labelEnd);

					instructionList.add(new InstructionLabel(labelBegin));

					PC_Struct2<String, List<Instruction>> tmp = expandExpression(inBracket);
					instructionList.addAll(tmp.b);
					instructionList.add(new InstructionIf(tmp.a, labelEnd, ""));

					assertNextBlack(reader, '{', "UNTIL body", "UNTIL condition must be followed by \"{\".");

					instructionList.addAll(parseCodeBlock(readUntil(reader, '{', '}', "UNTIL body")));

					instructionList.add(new InstructionGoto(labelBegin));
					instructionList.add(new InstructionLabel(labelEnd));

					loopLabelsContinue.pop();
					loopLabelsBreak.pop();

				} else if (sb.toString().trim().startsWith("function ")) {

					//function declaration

					// function funcName(a,b,c){ ... }

					String funcName = sb.toString().trim().substring("function ".length());
					sb.setLength(0);

					if (!funcName.matches("^[a-zA-Z_]{1}[a-zA-Z_0-9.]*?$")) {
						throw new SyntaxError("Invalid declared function name: \"" + funcName + "\"");
					}

					String inBracket = readUntil(reader, '(', ')', "function \"" + funcName + "\" declaration");

					List<String> parameters = splitBracketSafe(inBracket, ',');

					for (int i = 0; i < parameters.size(); i++) {
						if (!parameters.get(i).matches(variableInCodePatternRegexp)) {
							throw new SyntaxError("Invalid argument name \"" + parameters.get(i) + "\" for declared function \"" + funcName + "\".");
						} else {
							parameters.set(i, parameters.get(i).trim());
						}

					}

					// prevent running into function body from the surrounding code - skip it
					String labelAfter = makeTmpLabel();
					instructionList.add(new InstructionGoto(labelAfter));

					instructionList.add(new InstructionFunction(funcName, parameters.toArray(new String[parameters.size()])));
					assertNextBlack(reader, '{', "function \"" + funcName + "\" body", "Function declaration must be followed by \"{\" at \""
							+ funcName + "\"");
					List<Instruction> funcbody = parseCodeBlock(readUntil(reader, '{', '}', "function body"));

					if (funcbody.size() > 0 && (funcbody.get(funcbody.size() - 1) instanceof InstructionReturn)) {
						instructionList.addAll(funcbody);
					} else {
						instructionList.addAll(funcbody);

						// add void return to ensure it ends properly.
						instructionList.add(new InstructionReturn(""));
					}

					instructionList.add(new InstructionLabel(labelAfter));


				} else {
					//function call
					//funcname_to_call(arg, arg, arg);

					String funcName = sb.toString().trim();
					sb.setLength(0);

					if (!funcName.matches("^[a-zA-Z_]{1}[a-zA-Z_0-9.]*?$")) {
						throw new SyntaxError("Invalid function name to call: \"" + funcName + "\"");
					}

					String inBracket = readUntil(reader, '(', ')', "function \"" + funcName + "\" call arguments").trim();

					assertNextBlack(reader, ';', "function \"" + funcName + "\" call", "Function call must be followed by \";\" at \"" + funcName + "("
							+ inBracket + ")\".");

					if (funcName.equalsIgnoreCase("return")) {
						// a RETURN statement

						if (inBracket.length() > 0) {
							// return(something);
							PC_Struct2<String, List<Instruction>> expanded = expandExpression(inBracket);
							instructionList.addAll(expanded.b);
							instructionList.add(new InstructionReturn(expanded.a));
						} else {
							// return;
							instructionList.add(new InstructionReturn(""));
						}

					} else if (funcName.equalsIgnoreCase("call")) {
						// a _CALL statement
						List<String> parameters = splitBracketSafe(inBracket, ',');

						for (int i = 0; i < parameters.size(); i++) {
							PC_Struct2<String, List<Instruction>> tmp = expandExpression(parameters.get(i));
							instructionList.addAll(tmp.b);
							parameters.set(i, tmp.a);
						}

						instructionList.add(new InstructionStringCall(parameters.toArray(new String[parameters.size()])));

					} else if (funcName.equalsIgnoreCase("push")) {
						// a PUSH statement

						if (inBracket.length() > 0) {
							// push(something);
							PC_Struct2<String, List<Instruction>> expanded = expandExpression(inBracket);
							instructionList.addAll(expanded.b);
							instructionList.add(new InstructionPush(expanded.a));
						} else {
							throw new SyntaxError("Can not push \"null\" on stack at \"push(" + inBracket + ");\".");
						}

					} else if (funcName.equalsIgnoreCase("pop")) {
						// a POP statement

						if (inBracket.length() > 0) {
							// push(something);

							if (inBracket.matches(variableInCodePatternRegexp)) {
								instructionList.add(new InstructionPop(inBracket));
							} else {
								throw new SyntaxError("Invalid variable name at \"pop(" + inBracket + ");\".");
							}
						} else {
							instructionList.add(new InstructionPop(""));
						}

					} else {
						// yes! finally! a real function! XD

						List<String> parameters = splitBracketSafe(inBracket, ',');

						for (int i = 0; i < parameters.size(); i++) {
							PC_Struct2<String, List<Instruction>> tmp = expandExpression(parameters.get(i));
							instructionList.addAll(tmp.b);
							parameters.set(i, tmp.a);
						}

						instructionList.add(new InstructionCall(funcName, parameters.toArray(new String[parameters.size()])));
					}

				}

			} else if (ch == '{' && sb.toString().trim().equalsIgnoreCase("do")) {

				// do{...}while(condition);

				sb.setLength(0);

				String labelBegin = makeTmpLabel();
				String labelEnd = makeTmpLabel();

				loopLabelsContinue.push(labelBegin);
				loopLabelsBreak.push(labelEnd);

				instructionList.add(new InstructionLabel(labelBegin));

				instructionList.addAll(parseCodeBlock(readUntil(reader, '{', '}', "DO body")));

				String whileString = readUntil(reader, null, '(', "DO condition");

				boolean whileType;

				if (whileString.equalsIgnoreCase("while")) {
					whileType = true;
				} else if (whileString.equalsIgnoreCase("until")) {
					whileType = false;
				} else {
					throw new SyntaxError("DO body must be followed by \"while\" or \"until\", found \"" + whileString + "\".");
				}

				String inBracket = readUntil(reader, '(', ')', "DO condition");
				PC_Struct2<String, List<Instruction>> tmp = expandExpression(inBracket);
				instructionList.addAll(tmp.b);
				instructionList.add(new InstructionIf(tmp.a, whileType ? labelBegin : "", whileType ? "" : labelBegin));
				instructionList.add(new InstructionLabel(labelEnd));

				loopLabelsContinue.pop();
				loopLabelsBreak.pop();

				assertNextBlack(reader, ';', "DO condition", "DO condition must be followed by \";\".");


			} else {
				sb.append(ch);
			}

		}

		if (!sb.toString().trim().equals("")) {
			throw new SyntaxError("Invalid statement: \"" + sb.toString().trim() + "\"");
		}

		return instructionList;

	}

	private int readNextBlack(StringReader reader) throws IOException {

		while (true) {
			int ch = reader.read();
			if (ch == ' ' || ch == '\n') continue;

			return ch;
		}

	}

	private void assertNextBlack(StringReader reader, char required, String errStructName, String errMissing) throws IOException, SyntaxError {
		int chi = readNextBlack(reader);
		if (chi == -1) throw new SyntaxError("Reached end of block while reading " + errStructName);
		char ch = (char) chi;
		if (ch != required) {
			throw new SyntaxError(errMissing);
		}
	}
	
	private long vc = 0, lc = 0;	

	private String makeTmpVar() {
		return tmpVarPrefix + (!isLib?(vc++):Calc.generateSuperUniqueName());
	}

	private String makeTmpLabel() {
		return tmpLabelPrefix +  (!isLib?(lc++):Calc.generateSuperUniqueName());
	}

}
