package powercraft.core;

import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class PC_SlotSelective extends Slot implements PC_ISlotWithBackground {
	private int realslotnumber = -1; 

	/**
	 * selective slot.
	 * 
	 * @param par1iInventory
	 * @param par2 slotnum
	 * @param par3 x
	 * @param par4 y
	 */
	public PC_SlotSelective(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
		realslotnumber = par2;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {

		if (inventory instanceof PC_ISpecialAccessInventory) {
			return ((PC_ISpecialAccessInventory) inventory).canPlayerInsertStackTo(realslotnumber, par1ItemStack);
		}

		return super.isItemValid(par1ItemStack);
	}
	
	private ItemStack bgStack = null;
	
	@Override
	public ItemStack getBackgroundStack() {
		return bgStack;
	}
	
	@Override
	public PC_SlotSelective setBackgroundStack(ItemStack stack) {
		bgStack = stack.copy();
		return this;
	}

	@Override
	public boolean renderTooltipWhenEmpty() {
		return false;
	}

	@Override
	public boolean renderGrayWhenEmpty() {
		return true;
	}
}
