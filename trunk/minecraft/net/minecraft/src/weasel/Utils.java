package net.minecraft.src.weasel;


/**
 * Weasel utils
 * 
 * @author MightyPork
 */
public final class Utils {
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
