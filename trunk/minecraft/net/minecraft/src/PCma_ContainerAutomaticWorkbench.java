package net.minecraft.src;


/**
 * Automatic Workbench's container
 * 
 * @author MightyPork
 * @copy (c) 2012
 */
public class PCma_ContainerAutomaticWorkbench extends Container {

	private PCma_TileEntityAutomaticWorkbench storage; // main inventory
	private IInventory craftResult; // crafting product slot

	@Override
	protected void retrySlotClick(int i, int j, boolean flag, EntityPlayer entityplayer) {

	}

	@Override
	public void onCraftGuiClosed(EntityPlayer entityplayer) {
		storage.orderAndCraft();
	}

	/**
	 * @param inventoryplayer player inventory
	 * @param inv autoworkbench's inventory (tile entity)
	 */
	public PCma_ContainerAutomaticWorkbench(InventoryPlayer inventoryplayer, PCma_TileEntityAutomaticWorkbench inv) {
		storage = inv;

		craftResult = new InventoryCraftResult();

		addSlot(new PCma_SlotAutomaticWorkbenchResult(inventoryplayer.player, storage, craftResult, this, 0, 145, 44));

		int cnt = 0;
		for (int l = 0; l < 3; l++) {
			for (int k1 = 0; k1 < 3; k1++) {
				addSlot(new PCma_SlotAutomaticWorkbenchInventory(storage, this, false, cnt++, 8 + k1 * 18, 26 + l * 18));
			}
		}

		for (int l = 0; l < 3; l++) {
			for (int k1 = 0; k1 < 3; k1++) {
				addSlot(new PCma_SlotAutomaticWorkbenchInventory(storage, this, true, cnt++, 71 + k1 * 18, 26 + l * 18));
			}
		}

		for (int i1 = 0; i1 < 3; i1++) {
			for (int l1 = 0; l1 < 9; l1++) {
				addSlot(new Slot(inventoryplayer, l1 + i1 * 9 + 9, 8 + l1 * 18, 104 + i1 * 18));
			}
		}

		for (int j1 = 0; j1 < 9; j1++) {
			addSlot(new Slot(inventoryplayer, j1, 8 + j1 * 18, 162));
		}

		onCraftMatrixChanged(storage);

	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return storage.isUseableByPlayer(entityplayer);
	}

	@Override
	public void onCraftMatrixChanged(IInventory iinventory) {
		// update crafting result.
		craftResult.setInventorySlotContents(0, storage.getRecipeProduct());
	}
}
