package net.minecraft.src;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.PC_GresTextEditMultiline.Keyword;
import weasel.Compiler;
import weasel.IWeaselHardware;
import weasel.WeaselEngine;


/**
 * Highlighting helper
 * 
 * @author MightyPork
 */
@SuppressWarnings("javadoc")
public class PC_GresHighlightHelper {


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
	public static int colorDefault;

	/**
	 * Generate config file if needed, and read values from it.
	 */
	public static void checkConfigFile() {

		PC_PropertyManager cfg = new PC_PropertyManager(mod_PCcore.cfgdir + "/highlight.cfg", "Colors used to highlight code in programming screens.\nColor hex format: RRGGBB.");
		cfg.cfgSeparateSections(false);
		cfg.putString("function_hardware", "6666ff");
		cfg.putString("function_math", "ff1050");
		cfg.putString("constant", "990099");
		cfg.putString("keyword", "ff0000");
		cfg.putString("number", "ffff00");
		cfg.putString("variable_hardware", "00BB00");
		cfg.putString("operator", "ff9900");
		cfg.putString("bracket", "ff66ff");
		cfg.putString("brace", "9999ff");
		cfg.putString("semicolon", "ff66ff");
		cfg.putString("comma", "ff66ff");
		cfg.putString("comments", "337ca7");
		cfg.putString("string", "ff3330");
		cfg.putString("background", "000000");
		cfg.putString("default", "ffffff");
		cfg.apply();

		colorHardwareFunction = Integer.parseInt(cfg.str("function_hardware"), 16);
		colorMathFunction = Integer.parseInt(cfg.str("function_math"), 16);
		colorLangKeyword = Integer.parseInt(cfg.str("keyword"), 16);
		colorNumber = Integer.parseInt(cfg.str("number"), 16);
		colorHardwareVar = Integer.parseInt(cfg.str("variable_hardware"), 16);
		colorOperator = Integer.parseInt(cfg.str("operator"), 16);
		colorMathConstant = Integer.parseInt(cfg.str("constant"), 16);
		colorBracket = Integer.parseInt(cfg.str("bracket"), 16);
		colorBrace = Integer.parseInt(cfg.str("brace"), 16);
		colorSemicolon = Integer.parseInt(cfg.str("semicolon"), 16);
		colorComma = Integer.parseInt(cfg.str("comma"), 16);
		colorComments = Integer.parseInt(cfg.str("comments"), 16);
		colorString = Integer.parseInt(cfg.str("string"), 16);
		colorBackground = Integer.parseInt(cfg.str("background"), 16);
		colorDefault = Integer.parseInt(cfg.str("default"), 16);
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
		kw.add(new Keyword("[\\{\\}]", colorBrace, true));
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
		
		if(engine != null) {
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
		return kw;
	}

}
