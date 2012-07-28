package net.minecraft.src;


/**
 * @author MightyPork
 */
public class PClo_WeaselDiskDriveInventory extends InventoryBasic implements PC_ISpecialAccessInventory {

	/**
	 * inventory selective for disks
	 * 
	 * @param name
	 * @param slots
	 */
	public PClo_WeaselDiskDriveInventory(String name, int slots) {
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
		int disk = mod_PClogic.weaselDisk.shiftedIndex;
		return stack.getItem().shiftedIndex == disk && PClo_ItemWeaselDisk.getType(stack) != PClo_ItemWeaselDisk.EMPTY;
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
