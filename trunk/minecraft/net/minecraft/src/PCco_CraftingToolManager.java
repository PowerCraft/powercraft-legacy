package net.minecraft.src;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * The Crafting Tool device container
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCco_CraftingToolManager {

	/**
	 * Crafting tool manager. Make instance to use in Crafting Tool.
	 */
	public PCco_CraftingToolManager() {
		loadStacksToSlots();
	}

	/**
	 * List of stacks for slots, use loadStacksToSlots() to fill with latest
	 * stacks available.
	 */
	public ArrayList<ItemStack> stacklist = new ArrayList<ItemStack>();


	/** Groups of stacks, ordered by index key. */
	private static HashMap<Integer, ItemStack[]> itemLists = new HashMap<Integer, ItemStack[]>();


	/**
	 * Add given stacks into the crafting tool.
	 * 
	 * @param ordering_index index of position; If used, tries one higher until
	 *            it finds free number.
	 * @param stacks array of stacks to add
	 */
	public static void addStacks(int ordering_index, ItemStack[] stacks) {
		if (itemLists.containsKey(ordering_index)) {
			addStacks(ordering_index + 1, stacks);
		} else {
			PC_Logger.finest("Adding items to Crafting Tool at position " + ordering_index);
			itemLists.put(ordering_index, stacks);
		}
	}


	/**
	 * Prepare stack list to fill slots.
	 */
	private void loadStacksToSlots() {
		stacklist.clear();
		// get numeric keys and use them to sort the lists
		Integer[] keys = itemLists.keySet().toArray(new Integer[itemLists.keySet().size()]);
		Arrays.sort(keys);

		// get all lists, ordered by indexes, and load them to the itemstack array.
		for (int key : keys) {

			ItemStack[] list = itemLists.get(key);

			for (ItemStack stack : list) {
				// add to list
				stacklist.add(stack.copy());
			}
		}
	}



	/**
	 * Get item from stack list at given index.
	 * 
	 * @param num absolute index (regardless of pages)
	 * @return the stack or null if not present.
	 */
	public ItemStack getItemForSlotNumber(int num) {
		if (num < stacklist.size()) {
			return stacklist.get(num);
		}
		return null;
	}

}
