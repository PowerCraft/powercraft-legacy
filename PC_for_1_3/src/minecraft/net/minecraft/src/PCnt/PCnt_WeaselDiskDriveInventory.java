package net.minecraft.src.PCnt;

import net.minecraft.src.InventoryBasic;
import net.minecraft.src.ItemStack;
import net.minecraft.src.PC_ISpecialAccessInventory;
import net.minecraft.src.mod_PClogic;


/**
 * @author MightyPork
 */
public class PCnt_WeaselDiskDriveInventory extends InventoryBasic implements PC_ISpecialAccessInventory {

	/**
	 * inventory selective for disks
	 * 
	 * @param name
	 * @param slots
	 */
	public PCnt_WeaselDiskDriveInventory(String name, int slots) {
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
		return stack.getItem().shiftedIndex == disk && PCnt_ItemWeaselDisk.getType(stack) != PCnt_ItemWeaselDisk.EMPTY;
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
