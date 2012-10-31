package powercraft.logic;

public class PClo_DelayerType {

	/** Number of all the delayer types */
	public static final int TOTAL_DELAYER_COUNT = 2;

	@SuppressWarnings("javadoc")
	public static final int FIFO = 0, HOLD = 1;

	/**
	 * Delayer names used for localization
	 */
	public static String[] names = new String[TOTAL_DELAYER_COUNT];

	static {
		names[FIFO] = "buffer";
		names[HOLD] = "slowRepeater";
	}
	
	/**
	 * Delayer teyture index
	 */
	public static int[] index = new int[TOTAL_DELAYER_COUNT];

	static {
		index[FIFO] = 84;
		index[HOLD] = 85;
	}
	
}
