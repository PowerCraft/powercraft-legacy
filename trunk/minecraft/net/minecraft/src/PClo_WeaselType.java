package net.minecraft.src;


/** "Enum" for logic gates type */
public class PClo_WeaselType {
	/** Number of all the gate types */
	public static final int WEASEL_DEVICE_COUNT = 2;

	@SuppressWarnings("javadoc")
	public static final int CORE = 0, PORT = 1;

	/**
	 * Gate names used for localization
	 */
	public static String[] names = new String[WEASEL_DEVICE_COUNT];

	static {
		names[CORE] = "core";
		names[PORT] = "port";
	}

}
