package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tools for inventory manipulation.
 * 
 * @author MightyPork
 */
public class PC_InvUtils {

	/**
	 * Get inventory or double chest inventory.
	 * 
	 * @param world world to work with
	 * @param x x
	 * @param y y
	 * @param z z
	 * @return the IInventory of the chest
	 */
	public static IInventory getCompositeInventoryAt(IBlockAccess world, int x, int y, int z) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te == null || !(te instanceof IInventory) || (te instanceof TileEntityBrewingStand)) { return null; }

		IInventory inv = (IInventory) te;

		int id = world.getBlockId(x, y, z);

		if (id == Block.chest.blockID) {

			if (world.getBlockId(x - 1, y, z) == Block.chest.blockID) {
				inv = new InventoryLargeChest("Large chest", (IInventory) world.getBlockTileEntity(x - 1, y, z), (inv));
			}
			if (world.getBlockId(x + 1, y, z) == Block.chest.blockID) {
				inv = new InventoryLargeChest("Large chest", (inv), (IInventory) world.getBlockTileEntity(x + 1, y, z));
			}
			if (world.getBlockId(x, y, z - 1) == Block.chest.blockID) {
				inv = new InventoryLargeChest("Large chest", (IInventory) world.getBlockTileEntity(x, y, z - 1), (inv));
			}
			if (world.getBlockId(x, y, z + 1) == Block.chest.blockID) {
				inv = new InventoryLargeChest("Large chest", (inv), (IInventory) world.getBlockTileEntity(x, y, z + 1));
			}

		}

		return inv;
	}

	/**
	 * Store itemstack into specified slot in an inventory.
	 * Useful for furnaces, brewing stands and that kind of containers.
	 * 
	 * @param inventory target inventory
	 * @param stackToStore stack to store
	 * @param slot id of the slot
	 * @return true if the stack was fully stored
	 */
	public static boolean storeItemInSlot(IInventory inventory, ItemStack stackToStore, int slot) {
		if (stackToStore == null || stackToStore.stackSize == 0) { return false; }
		if (inventory instanceof PC_ISelectiveInventory && !((PC_ISelectiveInventory) inventory).canInsertStackTo(slot, stackToStore)) { return false; }

		ItemStack destination = inventory.getStackInSlot(slot);

		if (destination == null) {
			int numStored = stackToStore.stackSize;
			numStored = Math.min(numStored, stackToStore.getMaxStackSize());
			numStored = Math.min(numStored, inventory.getInventoryStackLimit());
			destination = stackToStore.splitStack(numStored);
			inventory.setInventorySlotContents(slot, destination);
			return true;
		}

		if (destination.itemID == stackToStore.itemID && destination.isStackable()
				&& (!destination.getHasSubtypes() || destination.getItemDamage() == stackToStore.getItemDamage())
				&& destination.stackSize < inventory.getInventoryStackLimit()) {
			int numStored = stackToStore.stackSize;
			numStored = Math.min(numStored, destination.getMaxStackSize() - destination.stackSize);
			numStored = Math.min(numStored, inventory.getInventoryStackLimit() - destination.stackSize);
			destination.stackSize += numStored;
			stackToStore.stackSize -= numStored;
			return (numStored > 0);
		}

		return false;
	}

	/**
	 * Add given stack to an inventory. First fills used slots, then starts
	 * using new slots.
	 * 
	 * @param inv target IInventory
	 * @param itemstack stack to store
	 * @return true if something was stored
	 */
	public static boolean addItemStackToInventory(IInventory inv, ItemStack itemstack) {
		if (!itemstack.isItemDamaged()) {
			int i;
			do {
				i = itemstack.stackSize;
				itemstack.stackSize = storePartialItemStack(inv, itemstack);
			} while (itemstack.stackSize > 0 && itemstack.stackSize < i);
			return itemstack.stackSize < i;
		}
		int j = getFirstEmptyStack(inv);
		if (j >= 0) {
			inv.setInventorySlotContents(j, ItemStack.copyItemStack(itemstack));
			itemstack.stackSize = 0;
			return true;
		}
		return false;
	}

	/**
	 * Add given stack to an inventory. First fills used slots, then starts
	 * using new slots. Returns TRUE only if EVERYTHING was stored, and the entity item can be discarded.
	 * 
	 * @param inv target IInventory
	 * @param itemstack stack to store
	 * @return true if all was stored.
	 */
	public static boolean addWholeItemStackToInventory(IInventory inv, ItemStack itemstack) {
		if (!itemstack.isItemDamaged()) {
			int oldSize;
			do {
				oldSize = itemstack.stackSize;
				itemstack.stackSize = storePartialItemStack(inv, itemstack);
			} while (itemstack.stackSize > 0 && itemstack.stackSize < oldSize);
			return itemstack.stackSize == 0;
		}
		int emptySlot = getFirstEmptyStack(inv);
		if (emptySlot >= 0) {
			inv.setInventorySlotContents(emptySlot, ItemStack.copyItemStack(itemstack));
			itemstack.stackSize = 0;
			return true;
		}
		return false;
	}

	private static int getStackWithFreeSpace(IInventory inv, ItemStack itemstack) {

		for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
			ItemStack stackAt = inv.getStackInSlot(slot);
			if (stackAt != null && stackAt.itemID == itemstack.itemID && stackAt.isStackable()
					&& stackAt.stackSize < stackAt.getMaxStackSize() && stackAt.stackSize < inv.getInventoryStackLimit()
					&& (!stackAt.getHasSubtypes() || stackAt.getItemDamage() == itemstack.getItemDamage())) { return slot; }
		}

		return -1;
	}

	private static int storePartialItemStack(IInventory inv, ItemStack itemstack) {

		int id = itemstack.itemID;
		int size = itemstack.stackSize;

		// not stackable
		if (itemstack.getMaxStackSize() == 1) {
			int firstEmpty = getFirstEmptyStack(inv);
			if (firstEmpty < 0) { return size; }
			if (inv.getStackInSlot(firstEmpty) == null) {
				inv.setInventorySlotContents(firstEmpty, ItemStack.copyItemStack(itemstack));
			}
			return 0;
		}

		int targetSlot = getStackWithFreeSpace(inv, itemstack);
		if (targetSlot < 0) {
			targetSlot = getFirstEmptyStack(inv);
		}

		if (targetSlot < 0) { return size; }

		if (inv.getStackInSlot(targetSlot) == null) {
			inv.setInventorySlotContents(targetSlot, new ItemStack(id, 0, itemstack.getItemDamage()));
		}

		int canStore = size;
		if (canStore > inv.getStackInSlot(targetSlot).getMaxStackSize() - inv.getStackInSlot(targetSlot).stackSize) {
			canStore = inv.getStackInSlot(targetSlot).getMaxStackSize() - inv.getStackInSlot(targetSlot).stackSize;
		}
		if (canStore > inv.getInventoryStackLimit() - inv.getStackInSlot(targetSlot).stackSize) {
			canStore = inv.getInventoryStackLimit() - inv.getStackInSlot(targetSlot).stackSize;
		}
		if (canStore == 0) {
			return size;
		} else {
			size -= canStore;
			inv.getStackInSlot(targetSlot).stackSize += canStore;
			return size;
		}
	}

	private static int getFirstEmptyStack(IInventory inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i) == null) { return i; }
		}

		return -1;
	}

	/**
	 * Check if given inventory is full
	 * 
	 * @param inv the ivnentory
	 * @return is full
	 */
	public static boolean isInventoryFull(IInventory inv) {
		if (inv == null) { return false; }

		if (inv instanceof PC_IStateReportingInventory) { return ((PC_IStateReportingInventory) inv).isContainerFull(); }

		if (inv instanceof TileEntityFurnace) { return inv.getStackInSlot(1) != null
				&& inv.getStackInSlot(1).stackSize == Math.min(inv.getInventoryStackLimit(), inv.getStackInSlot(1).getMaxStackSize()); }

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i) == null
					|| inv.getStackInSlot(i).stackSize < Math.min(inv.getInventoryStackLimit(), inv.getStackInSlot(i).getMaxStackSize())) { return false; }
		}
		return true;
	}

	/**
	 * Check if given inventory is empty
	 * 
	 * @param inv the inventory
	 * @return is empty
	 */
	public static boolean isInventoryEmpty(IInventory inv) {
		if (inv == null) { return true; }

		if (inv instanceof PC_IStateReportingInventory) { return ((PC_IStateReportingInventory) inv).isContainerEmpty(); }

		if (inv instanceof TileEntityFurnace) { return inv.getStackInSlot(1) == null; }

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i) != null) { return false; }
		}
		return true;
	}

	/**
	 * Move stacks from one to another inventory
	 * 
	 * @param from source inventory
	 * @param to target inventory
	 * @return true if From inventory is smaller or equal to To inventory
	 */
	public static boolean moveStacks(IInventory from, IInventory to) {

		int copied = Math.min(from.getSizeInventory(), to.getSizeInventory());

		for (int i = 0; i < copied; i++) {
			to.setInventorySlotContents(i, from.getStackInSlot(i));
			from.setInventorySlotContents(i, null);
		}

		return from.getSizeInventory() <= to.getSizeInventory();
	}



	/**
	 * Get player's armour value.
	 * 
	 * @param player
	 * @return armour value
	 */
	public static int getPlayerArmourValue(EntityPlayerSP player) {
		return player.inventory.getTotalArmorValue();
	}

	/**
	 * Get fuel value of a stack's item
	 * 
	 * @param itemstack the stack
	 * @param strength value multiplier, 1.0 is the standard, furnace strength.
	 * @return fuel value
	 */
	public static int getFuelValue(ItemStack itemstack, double strength) {
		if (itemstack == null) { return 0; }
		int i = itemstack.getItem().shiftedIndex;
		if (i == Block.wood.blockID) { return (int) (4 * 300 * strength); }
		if (i < 256 && Block.blocksList[i].blockMaterial == Material.wood) { return (int) (300 * strength); }
		if (i == Item.stick.shiftedIndex) { return (int) (100 * strength); }
		if (i == Item.paper.shiftedIndex) { return (int) (150 * strength); }
		if (i == Item.coal.shiftedIndex) { return (int) (1600 * strength); }
		if (i == Item.bucketLava.shiftedIndex) { return (int) (20000 * strength); }
		if (i == Block.sapling.blockID) { return (int) (100 * strength); }
		if (i == Item.gunpowder.shiftedIndex) { return (int) (500 * strength); }
		if (i == Item.blazeRod.shiftedIndex) { return (int) (2400 * strength); }

		return (int) (ModLoader.addAllFuel(i, itemstack.getItemDamage()) * strength);
	}

	/**
	 * Count unique Power Crystals in the given inventory
	 * 
	 * @param inventory the inventory to search
	 * @return number of Power Crystals
	 */
	public static int countPowerCrystals(IInventory inventory) {
		boolean[] foundTable = { false, false, false, false, false, false, false, false };

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).itemID == mod_PCcore.powerCrystal.blockID) {
				foundTable[MathHelper.clamp_int(inventory.getStackInSlot(i).getItemDamage(), 0, 7)] = true;
			}
		}

		int cnt = 0;
		for (int i = 0; i < 8; i++) {
			if (foundTable[i]) {
				cnt++;
			}
		}

		return cnt;
	}

	/**
	 * Go through a list of itemstacks and merge them to smallest possible count.
	 * 
	 * @param input original stacks array
	 * @return new stacks array
	 */
	public static ItemStack[] mergeStacks(ItemStack[] input) {

		List<ItemStack> list = stacksToList(input);

		mergeStacks(list);

		return stacksToArray(list);

	}


	/**
	 * Go through a list of itemstacks and merge them to smallest possible count.
	 * 
	 * @param input original list of stacks
	 */
	public static void mergeStacks(List<ItemStack> input) {

		if (input == null) { return; }

		for (ItemStack st1 : input) {

			if (st1 != null) {

				for (ItemStack st2 : input) {

					if (st2 != null && st2.isItemEqual(st1)) {
						int movedToFirst = Math.min(st2.stackSize, st1.getItem().maxStackSize - st1.stackSize);
						if (movedToFirst <= 0) {
							break;
						}

						st1.stackSize += movedToFirst;
						st2.stackSize -= movedToFirst;
					}

				}
			}
		}

		ArrayList<ItemStack> copy = new ArrayList<ItemStack>(input);
		for (int i = copy.size() - 1; i >= 0; i--) {
			if (copy.get(i) == null || copy.get(i).stackSize <= 0) {
				input.remove(i);
			}
		}

	}

	/**
	 * Convert ItemStack[] to List&lt;ItemStack&gt;
	 * 
	 * @param stacks input ItemStack[]
	 * @return output List&lt;ItemStack&gt;
	 */
	public static List<ItemStack> stacksToList(ItemStack[] stacks) {
		ArrayList<ItemStack> myList = new ArrayList<ItemStack>();

		Collections.addAll(myList, stacks);

		return myList;
	}

	/**
	 * Convert List&lt;ItemStack&gt; to ItemStack[]
	 * 
	 * @param stacks input List&lt;ItemStack&gt;
	 * @return output ItemStack[]
	 */
	public static ItemStack[] stacksToArray(List<ItemStack> stacks) {
		return stacks.toArray(new ItemStack[stacks.size()]);
	}
}
