package net.minecraft.src;



/**
 * Selective slot. Takes care if the inventory it is attached to, if it is an
 * instance of {@link PC_ISpecialAccessInventory}, asks it if it wants to accept
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PC_SlotSelective extends Slot {

	/**
	 * selective slot.
	 * 
	 * @param par1iInventory
	 * @param par2
	 * @param par3
	 * @param par4
	 */
	public PC_SlotSelective(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack) {

		if (inventory instanceof PC_ISpecialAccessInventory) {
			return ((PC_ISpecialAccessInventory) inventory).canPlayerInsertStackTo(this.slotNumber, par1ItemStack);
		}

		return super.isItemValid(par1ItemStack);
	}

}
