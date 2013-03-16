package powercraft.weasel;


import java.util.ArrayList;
import java.util.List;

import powercraft.launcher.PC_Property;
import powercraft.api.gres.PC_GresTextEditMultiline;
import powercraft.api.gres.PC_GresTextEditMultiline.AutoAdd;
import powercraft.api.gres.PC_GresTextEditMultiline.Keyword;
import powercraft.api.gres.PC_GresTextEditMultiline.StringAdd;
import powercraft.weasel.engine.Compiler;
import powercraft.weasel.engine.IWeaselHardware;
import powercraft.weasel.engine.WeaselEngine;

/**
 * Highlighting helper
 * 
 * @author MightyPork
 */
@SuppressWarnings("javadoc")
public class PCws_WeaselHighlightHelper {

	/** autoadd for program in weasel */
	public static AutoAdd autoAdd = new AutoAdd() {

		private int getFirstFreeSpace(String s) {
			for (int i = 0; i < s.length(); i++) {
				if (s.charAt(i) != '\t') return i;
			}
			return s.length();
		}

		private char getFirstNotOf(String s, char c) {
			for (int i = 0; i < s.length(); i++) {
				if (s.charAt(i) != ' ') return s.charAt(i);
			}
			return 0;
		}

		@Override
		public StringAdd charAdd(PC_GresTextEditMultiline te, char c, PC_GresTextEditMultiline.Keyword kw, int blocks, String textBefore,
				String textBehind) {
			if (kw == null || kw.end == null) {
				if (c == '{') return new StringAdd("}", false);
				if (c == '(') return new StringAdd(")", false);
				if (c == '"') return new StringAdd("\"", false);
				if (c == '\n') {
					int tab = blocks;
					int ffs = getFirstFreeSpace(textBehind);
					if (getFirstNotOf(textBehind, ' ') == '}') tab -= 1;
					tab -= ffs;
					String s = "";
					for (int i = 0; i < tab; i++) {
						s += "\t";
					}
					return new StringAdd(s, true);
				}
			}
			return null;
		}

	};


	public static int colorHardwareFunction;
	public static int colorMathFunction;
	public static int colorLangKeyword;
	public static int colorNumber;
	public static int colorHardwareVar;
	public static int colorOperator;
	public static int colorMathConstant;
	public static int colorBracket;
	public static int colorBrace;
	public static int colorSemicolon;
	public static int colorComma;
	public static int colorComments;
	public static int colorString;
	public static int colorBackground;
	public static int colorUserFunction;
	public static int colorDefault;

	/**
	 * Generate config file if needed, and read values from it.
	 */
	public static void checkConfigFile(PC_Property config) {

		config.getProperty("highlight", null, new String[]{"Colors used to highlight code in programming screens.", "Color hex format: RRGGBB."});
		colorHardwareFunction = Integer.parseInt(config.getString("highlight.function_hardware", "6666ff"), 16);
		colorMathFunction = Integer.parseInt(config.getString("highlight.function_math", "ff1050"), 16);
		colorLangKeyword = Integer.parseInt(config.getString("highlight.keyword", "ff0000"), 16);
		colorNumber = Integer.parseInt(config.getString("highlight.number", "ffff00"), 16);
		colorHardwareVar = Integer.parseInt(config.getString("highlight.variable_hardware", "00BB00"), 16);
		colorOperator = Integer.parseInt(config.getString("highlight.operator", "ff9900"), 16);
		colorMathConstant = Integer.parseInt(config.getString("highlight.constant", "990099"), 16);
		colorBracket = Integer.parseInt(config.getString("highlight.bracket", "ff66ff"), 16);
		colorBrace = Integer.parseInt(config.getString("highlight.brace", "9999ff"), 16);
		colorSemicolon = Integer.parseInt(config.getString("highlight.semicolon", "ff66ff"), 16);
		colorComma = Integer.parseInt(config.getString("highlight.comma", "ff66ff"), 16);
		colorComments = Integer.parseInt(config.getString("highlight.comments", "337ca7"), 16);
		colorString = Integer.parseInt(config.getString("highlight.string", "ff3330"), 16);
		colorBackground = Integer.parseInt(config.getString("highlight.background", "000000"), 16);
		colorUserFunction = Integer.parseInt(config.getString("highlight.function_user", "4332ff"), 16);
		colorDefault = Integer.parseInt(config.getString("highlight.default", "ffffff"), 16);
	}

	/**
	 * Get list of highlighting for a weasel hardware.
	 * 
	 * @param hardware hardware this is generated for.
	 * @return the list
	 */
	public static ArrayList<Keyword> weasel(IWeaselHardware hardware, WeaselEngine engine) {
		ArrayList<Keyword> kw = new ArrayList<Keyword>();


		kw.add(new Keyword("[+\\-*/&|^\\*!%<>=]", colorOperator, true));

		kw.add(new Keyword("[\\(\\)\\[\\]]", colorBracket, true));
		kw.add(new Keyword("{", colorBrace, false, true, false));
		kw.add(new Keyword("}", colorBrace, false, false, true));
		kw.add(new Keyword("[;]", colorSemicolon, true));
		kw.add(new Keyword("[,]", colorComma, true));

		kw.add(new Keyword("[0-9]+[\\.]?[0-9]*", colorNumber, true));
		kw.add(new Keyword("0x[0-9a-fA-F]+", colorNumber, true));
		kw.add(new Keyword("0b[0-1]+", colorNumber, true));

		kw.add(new Keyword("#", "\n", colorComments, false));
		kw.add(new Keyword("//", "\n", colorComments, false));
		kw.add(new Keyword("/*", "*/", colorComments, false));
		kw.add(new Keyword("\"", "\"", colorString, false));
		//kw.add(new Keyword("'", "'", colorString, false));

		if (hardware != null) {
			// gate functions
			List<String> gateFunc = hardware.getProvidedFunctionNames();
			for (String str : gateFunc) {
				kw.add(new Keyword(str, colorHardwareFunction, false));
			}

			// gate input variables
			List<String> gateVar = hardware.getProvidedVariableNames();
			for (String str : gateVar) {
				kw.add(new Keyword(str, colorHardwareVar, false));
			}
		}

		if (engine != null) {
			// gate functions
			List<String> gateFunc = engine.getProvidedFunctionNames();
			for (String str : gateFunc) {
				kw.add(new Keyword(str, colorLangKeyword, false));
			}

			// gate input variables
			List<String> gateVar = engine.getProvidedVariableNames();
			for (String str : gateVar) {
				kw.add(new Keyword(str, colorLangKeyword, false));
			}
		}

		// math functions
		List<String> jepFunc = Compiler.parserFunctions;

		for (String str : jepFunc) {
			kw.add(new Keyword(str, colorMathFunction, false));
		}

		// math constants
		List<String> jepVar = Compiler.parserConstants;

		for (String str : jepVar) {
			kw.add(new Keyword(str, colorMathConstant, false));
		}

		// lang functions
		List<String> weaselFunc = Compiler.langKeywords;

		for (String str : weaselFunc) {
			kw.add(new Keyword(str, colorLangKeyword, false));
		}

		for (Keyword k : kw) {
			if (k.word == "function") {
				k.nextWordKeywordColor = colorUserFunction;
				break;
			}
		}
		return kw;
	}

}
