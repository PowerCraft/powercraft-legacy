package net.minecraft.src;


/** "Enum" for logic gates type */
public class PClo_WeaselType {
	/** Number of all the gate types */
	public static final int WEASEL_DEVICE_COUNT = 9;

	@SuppressWarnings("javadoc")
	public static final int CORE = 0, PORT = 1, DISPLAY = 2, SPEAKER = 3, TOUCHSCREEN = 4;
	@SuppressWarnings("javadoc")
	public static final int DISK_MANAGER = 5, DISK_DRIVE = 6, TERMINAL = 7, SLAVE = 8;

	/**
	 * Gate names used for localization
	 */
	public static String[] names = new String[WEASEL_DEVICE_COUNT];

	static {
		names[CORE] = "core";
		names[PORT] = "port";
		names[DISPLAY] = "display";
		names[SPEAKER] = "sound";
		names[TOUCHSCREEN] = "touchscreen";
		names[DISK_MANAGER] = "diskManager";
		names[DISK_DRIVE] = "diskDrive";
		names[TERMINAL] = "terminal";
		names[SLAVE] = "slave";
	}

}
