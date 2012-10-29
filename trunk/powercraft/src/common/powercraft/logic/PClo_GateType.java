package powercraft.logic;

public class PClo_GateType {
	/** Number of all the gate types */
	public static final int TOTAL_GATE_COUNT = 13;

	@SuppressWarnings("javadoc")
	public static final int NOT = 0, AND = 1, NAND = 2, OR = 3, NOR = 4, XOR = 5, XNOR = 6, AND3 = 7, NAND3 = 8, OR3 = 9, NOR3 = 10, XOR3 = 11,
			XNOR3 = 12;

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
		return 1;
	}
	
	/*    op
	 *   +--+
	 * i3|  |i1
	 *   +--+
	 *    i2
	 */
	
	public static boolean getGateOutput(int gateType, boolean i1, boolean i2, boolean i3){
		switch(gateType){
		case PClo_GateType.NOT:
			return !i2;
		case PClo_GateType.AND:
			return i1 && i3;
		case PClo_GateType.NAND:
			return !(i1 && i3);
		case PClo_GateType.OR:
			return i1 || i3;
		case PClo_GateType.NOR:
			return !(i1 || i3);
		case PClo_GateType.XOR:
			return i1 != i3;	
		case PClo_GateType.XNOR:
			return i1 == i3;
		case PClo_GateType.AND3:
			return i1 && i2 && i3;
		case PClo_GateType.NAND3:
			return !(i1 && i2 && i3);
		case PClo_GateType.OR3:
			return i1 || i2 || i3;
		case PClo_GateType.NOR3:
			return !(i1 || i2 || i3);
		case PClo_GateType.XOR3:
			return (i1 != i2) || (i2 != i3);	
		case PClo_GateType.XNOR3:
			return (i1 == i2) && (i2 == i3);	
		default:
			return false;
		}
	}
	
}
