package net.minecraft.src;


import java.util.HashMap;
import java.util.HashSet;


/**
 * TMI and NEI compatibility layer
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PC_InveditManager {

	// these are protected to allow NEI configuration class to access them
	/** list of IDs of hidden items; key: ID */
	protected static HashSet<Integer> hidden = new HashSet<Integer>();
	/**
	 * list of damage ranges for items; range format: Integer[]{start,stop};
	 * key: ID->range array
	 */
	protected static HashMap<Integer, Integer[]> damageRanges = new HashMap<Integer, Integer[]>();
	/** list of lists of allowed damages for blocks; key: ID->damages array */
	protected static HashMap<Integer, Integer[]> damageLists = new HashMap<Integer, Integer[]>();
	/**
	 * item categories; Key: ID -> Category; Prefix "PowerCraft." is added
	 * automatically.
	 */
	protected static HashMap<Integer, String> categories = new HashMap<Integer, String>();

	/**
	 * hide item from invedits
	 * 
	 * @param id item id
	 */
	public static void hideItem(int id) {
		hidden.add(id);
	}

	/**
	 * Set range for item damage values.
	 * 
	 * @param id item id
	 * @param min lower boundary
	 * @param max upper boundary
	 */
	public static void setDamageRange(int id, int min, int max) {
		damageRanges.put(id, new Integer[] { min, max });
	}

	/**
	 * Set list of valid damage values.<br>
	 * Use "setDamageRange" for ranges.
	 * 
	 * @param id item id
	 * @param listOfDamages Integer[] with the allowed damages.
	 */
	public static void setDamageList(int id, Integer[] listOfDamages) {
		damageLists.put(id, listOfDamages);
	}

	/**
	 * Set item's category. If damageRange or damageList is already set,<br>
	 * it will be used to add all valid damage values to the category.<br>
	 * <b>Prefix "PowerCraft." is added automatically!</b>
	 * 
	 * @param id item id
	 * @param category category, eg. "Machines.Laser"
	 */
	public static void setItemCategory(int id, String category) {
		categories.put(id, category);
	}

	/**
	 * Flag whether all mod items were already sent to TMI.
	 */
	private static boolean mod_items_sent_to_TMI = false;

	/**
	 * Send all contained information to TMI editor;<br>
	 * If already sent, do nothing.
	 */
	public static void sendToTMI() {

		if (mod_items_sent_to_TMI) {
			return;
		}
		mod_items_sent_to_TMI = true;

		try {
			Class.forName("net.minecraft.src.TMIItemInfo.class", false, PC_InveditManager.class.getClassLoader());
		} catch (ClassNotFoundException e) {
			PC_Logger.fine("\nInvedit Manager: TMI mod not found.");
			return;
		}


		for (Integer id : hidden) {
			TMI_hideId(id);
		}


		for (Integer id : damageRanges.keySet()) {
			Integer[] range = damageRanges.get(id);

			if (range.length != 2) {
				PC_Logger.severe("Invedit Manager: Invalid damage range for item with ID=" + id);
			}

			TMI_rangeDamage(id, range[0], range[1]);
		}

	}


	/**
	 * Hide item from TMI if TMI is present.
	 * 
	 * @param id item or block id
	 */
	private static void TMI_hideId(int id) {
		try {
			TMIItemInfo.hideItem(id);
		} catch (Throwable t) {}
	}

	/**
	 * Set item metadata range in TMI if TMI is present
	 * 
	 * @param id item ID
	 * @param from lower meta boundary
	 * @param to upper meta boundary
	 */
	private static void TMI_rangeDamage(int id, int from, int to) {
		try {
			TMIItemInfo.showItemWithDamageRange(id, from, to);
		} catch (Throwable t) {}
	}

}
