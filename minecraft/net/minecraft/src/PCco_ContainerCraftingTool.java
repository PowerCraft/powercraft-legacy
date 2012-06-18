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
public class PCco_ContainerCraftingTool /*extends Container*/ {

	private IInventory playerInventory;
	/** Page the screen was closed at. */
	private static int lastPage = 0;
	/** Inventory screen size */
	private static final int width = 13, height = 7;

	public ArrayList<Slot> inventorySlots = new ArrayList<Slot>();
	
	/**
	 * Create crafting tool container for player.
	 * 
	 * @param player the player
	 */
	public PCco_ContainerCraftingTool(EntityPlayer player) {

		loadStacksToSlots();

		playerInventory = player.inventory;

		// crafting slots
		int i = (height - 4) * 18;
		for (int row = 0; row < height; row++) {
			for (int column = 0; column < width; column++) {

				int indexInlist = lastPage * width * height + row * width + column;
				int indexSlot = column + row * width;

				inventorySlots.add(new PCco_SlotDirectCrafting(player, getItemForSlotNumber(indexInlist), indexSlot, 8 + column * 18, 18 + row * 18));
				//addSlot(new PCco_SlotDirectCrafting(player, getItemForSlotNumber(indexInlist), indexSlot, 8 + column * 18, 18 + row * 18));

			}

		}


		// player inv upper part
		for (int k = 0; k < 3; k++) {
			for (int j1 = 0; j1 < 9; j1++) {
				//addSlot(new Slot(playerInventory, j1 + k * 9 + 9, 2 * 18 + 8 + j1 * 18, 103 + k * 18 + i));
			}

		}

		// player inv quickbar
		for (int l = 0; l < 9; l++) {
			//addSlot(new Slot(playerInventory, l, 2 * 18 + 8 + l * 18, 161 + i));
		}

		mouseClicked();
	}

	/**
	 * onMouse clicked - update crafting slots status
	 */
	public void mouseClicked() {
		for (int index = 0; index < width * height; index++) {

			int indexSlot = index;

			((PCco_SlotDirectCrafting) inventorySlots.get(indexSlot)).updateAvailability();

		}
	}


	/**
	 * List of stacks in all slots, used for internal use and for GUI.
	 * Use addStacks method for filling fith custom stacks.
	 */
	public ArrayList<ItemStack> stacklist = new ArrayList<ItemStack>();


	/** Groups of stacks, ordered by index key. */
	private static HashMap<Integer, ItemStack[]> itemLists = new HashMap<Integer, ItemStack[]>();


	/**
	 * Add given stacks into the crafting tool.
	 * 
	 * @param ordering_index index of position; If used, tries one higher until it finds free number.
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
		if (num < stacklist.size()) { return stacklist.get(num); }
		return null;
	}


	/**
	 * Load stacks for given page in crafting tool.
	 * 
	 * @param pagenum page number
	 */
	public void loadStacksForPage(int pagenum) {
		lastPage = pagenum;

		for (int index = 0; index < width * height; index++) {

			int indexInlist = pagenum * width * height + index;
			int indexSlot = index;

			inventorySlots.get(indexSlot).putStack(getItemForSlotNumber(indexInlist));

		}
	}



	/*@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return playerInventory.isUseableByPlayer(entityplayer);
	}

	@Override
	protected void retrySlotClick(int i, int j, boolean flag, EntityPlayer entityplayer) {
		// no SHIFT!
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer entityplayer) {
		super.onCraftGuiClosed(entityplayer);
		playerInventory.closeChest();
	}*/

}
