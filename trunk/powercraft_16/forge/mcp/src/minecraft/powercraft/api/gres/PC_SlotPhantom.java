package powercraft.api.gres;

import net.minecraft.inventory.IInventory;

public class PC_SlotPhantom extends PC_Slot {

	public PC_SlotPhantom(IInventory inventory, int slotIndex) {
		super(inventory, slotIndex);
	}

	@Override
	public int getSlotStackLimit() {
		return 0;
	}

	@Override
	public boolean canDragIntoSlot() {
		return false;
	}
	
}
