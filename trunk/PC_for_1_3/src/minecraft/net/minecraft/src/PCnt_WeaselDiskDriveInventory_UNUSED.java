package net.minecraft.src;



/**
 * @author MightyPork
 */
public class PCnt_WeaselDiskDriveInventory_UNUSED extends InventoryBasic implements PC_ISpecialAccessInventory {

	/**
	 * inventory selective for disks
	 * 
	 * @param name
	 * @param slots
	 */
	public PCnt_WeaselDiskDriveInventory_UNUSED(String name, int slots) {
		super(name, slots);
	}

	@Override
	public boolean insertStackIntoInventory(ItemStack stack) {
		return false;
	}

	@Override
	public boolean needsSpecialInserter() {
		return false;
	}

	@Override
	public boolean canPlayerInsertStackTo(int slot, ItemStack stack) {
		if (stack == null) return false;
		int disk = mod_PCnet.weaselDisk.shiftedIndex;
		return stack.getItem().shiftedIndex == disk && PCnt_ItemWeaselDisk_UNUSED.getType(stack) != PCnt_ItemWeaselDisk_UNUSED.EMPTY;
	}

	@Override
	public boolean canMachineInsertStackTo(int slot, ItemStack stack) {
		return false;
	}

	@Override
	public boolean canDispenseStackFrom(int slot) {
		return false;
	}

}
