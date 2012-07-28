package net.minecraft.src;


/** "Enum" for logic gates type */
public class PClo_GateType {
	/** Number of all the gate types */
	public static final int TOTAL_GATE_COUNT = 31;

	@SuppressWarnings("javadoc")
	public static final int NOT = 0, AND = 1, NAND = 2, OR = 3, NOR = 4, XOR = 5, XNOR = 6, AND3 = 7, NAND3 = 8, OR3 = 9, NOR3 = 10, XOR3 = 11,
			XNOR3 = 12, D = 13, RS = 14, T = 15, DAY = 16, RAIN = 17, CHEST_EMPTY = 18, CHEST_FULL = 19, SPECIAL = 20, FIFO_DELAYER = 21,
			HOLD_DELAYER = 22, CROSSING = 23, RANDOM = 24, SPLITTER_I = 25, REPEATER_STRAIGHT = 26, REPEATER_CORNER = 27, REPEATER_STRAIGHT_I = 28,
			REPEATER_CORNER_I = 29, NIGHT = 30;

	/**
	 * Gate names used for localization
	 */
	public static String[] names = new String[TOTAL_GATE_COUNT];

	static {
		names[NOT] = "not";
		names[AND] = "and";
		names[NAND] = "nand";
		names[OR] = "or";
		names[NOR] = "nor";
		names[XOR] = "xor";
		names[XNOR] = "xnor";
		names[AND3] = "and3";
		names[NAND3] = "nand3";
		names[OR3] = "or3";
		names[NOR3] = "nor3";
		names[XOR3] = "xor3";
		names[XNOR3] = "xnor3";
		names[D] = "d";
		names[RS] = "rs";
		names[T] = "t";
		names[DAY] = "day";
		names[RAIN] = "rain";
		names[CHEST_EMPTY] = "chestEmpty";
		names[CHEST_FULL] = "chestFull";
		names[SPECIAL] = "special";
		names[FIFO_DELAYER] = "buffer";
		names[HOLD_DELAYER] = "slowRepeater";
		names[CROSSING] = "crossing";
		names[RANDOM] = "random";
		names[SPLITTER_I] = "splitter";
		names[REPEATER_STRAIGHT] = "repeaterStraight";
		names[REPEATER_CORNER] = "repeaterCorner";
		names[REPEATER_STRAIGHT_I] = "repeaterStraightInstant";
		names[REPEATER_CORNER_I] = "repeaterCornerInstant";
		names[NIGHT] = "night";
	}

	/**
	 * Get number of "corner sides". Corner side is usually used to determine
	 * input sides.
	 * 
	 * @param gateType
	 * @return max variants
	 */
	public static int getMaxCornerSides(int gateType) {
		if (gateType == AND || gateType == OR) return 3;
		if (gateType == SPLITTER_I) return 4;
		if (gateType == REPEATER_CORNER || gateType == REPEATER_CORNER_I) return 2;
		return 1;
	}

	/**
	 * @param type gate type
	 * @return true if this type can change light level based on active state
	 */
	public static boolean canChangeState(int type) {
		return !(type == REPEATER_CORNER_I || type == REPEATER_STRAIGHT_I || type == CROSSING);
	}

}
