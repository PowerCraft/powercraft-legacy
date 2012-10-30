package powercraft.logic;

public class PClo_SpecialType {
	/** Number of all the special types */
	public static final int TOTAL_SPECIAL_COUNT = 6;

	@SuppressWarnings("javadoc")
	public static final int DAY = 0, NIGHT = 1, RAIN = 2, CHEST_EMPTY = 3, CHEST_FULL = 4, SPECIAL = 5;

	/**
	 * Special names used for localization
	 */
	public static String[] names = new String[TOTAL_SPECIAL_COUNT];

	static {
		names[DAY] = "day";
		names[NIGHT] = "night";
		names[RAIN] = "rain";
		names[CHEST_EMPTY] = "chestEmpty";
		names[CHEST_FULL] = "chestFull";
		names[SPECIAL] = "special";
	}

	/**
	 * Special teyture index
	 */
	public static int[] index = new int[TOTAL_SPECIAL_COUNT];

	static {
		index[DAY] = 80;
		index[NIGHT] = 93;
		index[RAIN] = 81;
		index[CHEST_EMPTY] = 82;
		index[CHEST_FULL] = 83;
		index[SPECIAL] = 84;
	}
	
}
