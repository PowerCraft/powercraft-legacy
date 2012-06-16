package net.minecraft.src;


/**
 * Slot in the auto workbench screen (except result slot)
 * 
 * @author MightyPork
 * @copy (c) 2012
 * 
 */
public class PCma_SlotAutomaticWorkbenchInventory extends Slot {
	private Container parentContainer;
	private boolean inrecipe = false;

	/**
	 * @param iinventory the inventory of AW
	 * @param parent paretn container
	 * @param inRecipe is in the recipe part?
	 * @param i index
	 * @param j x
	 * @param k y
	 */
	public PCma_SlotAutomaticWorkbenchInventory(IInventory iinventory, Container parent, boolean inRecipe, int i, int j, int k) {
		super(iinventory, i, j, k);
		this.parentContainer = parent;
		this.inrecipe = inRecipe;

	}

	@Override
	public void onSlotChanged() {
		inventory.onInventoryChanged();
		if (inrecipe) {
			parentContainer.onCraftMatrixChanged(super.inventory);
		}
	}
}
