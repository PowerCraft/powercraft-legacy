package net.minecraft.src;


import org.lwjgl.input.Keyboard;


/**
 * Key filtering and character type utilities
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PC_KeyUtils {
	/**
	 * Test if this key is a text edit control key.<br>
	 * Accepts keys LEFT, RIGHT, HOME, END, BACKSPACE, DEL.
	 * 
	 * @param i key index
	 * @return is control key
	 */
	public static boolean isEditControlKey(int i) {
		return i == Keyboard.KEY_BACK || i == Keyboard.KEY_DELETE || i == Keyboard.KEY_LEFT || i == Keyboard.KEY_RIGHT || i == Keyboard.KEY_HOME
				|| i == Keyboard.KEY_END;
	}

	/**
	 * Test if char is digit.
	 * 
	 * @param ch character
	 * @return is digit
	 */
	public static boolean isCharDigit(char ch) {
		return Character.isDigit(Character.valueOf(ch));
	}

	/**
	 * Test if char is letter or number.
	 * 
	 * @param ch character
	 * @return is alnum
	 */
	public static boolean isCharAlnum(char ch) {
		return Character.isLetterOrDigit(Character.valueOf(ch));
	}

	/**
	 * Check if entered character / key code is valid for text edit.<br>
	 * Checks if valid for decimal number (with dot).
	 * 
	 * @param c character
	 * @param i key code
	 * @return is valid
	 */
	public static boolean filterKeyFloat(char c, int i) {
		return isCharDigit(c) || c == '.' || isEditControlKey(i);
	}

	/**
	 * Check if entered character / key code is valid for text edit.<br>
	 * Checks if valid for integer value without decimal dot.
	 * 
	 * @param c character
	 * @param i key code
	 * @return is valid
	 */
	public static boolean filterKeyInt(char c, int i) {
		return isCharDigit(c) || isEditControlKey(i);
	}

	/**
	 * Check if entered character / key code is valid for text edit.<br>
	 * Checks if valid for (even minus) integer value without decimal dot.
	 * 
	 * @param c character
	 * @param i key code
	 * @return is valid
	 */
	public static boolean filterKeyIntMinus(char c, int i) {
		return c == '-' || isCharDigit(c) || isEditControlKey(i);
	}

	/**
	 * Check if entered character / key code is valid for text edit.<br>
	 * Checks if valid for filename.
	 * 
	 * @param c character
	 * @param i key code
	 * @return is valid
	 */
	public static boolean filterKeyFilename(char c, int i) {
		return isCharAlnum(c) || c == '_' || c == '-' || c == '!' || c == '+' || c == ' ' || c == ',' || isEditControlKey(i);
	}

}
