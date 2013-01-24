package powercraft.itemstorage;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.inventory.Slot;
import powercraft.management.PC_GresBaseWithInventory;
import powercraft.management.PC_SlotNoPickup;
import powercraft.management.PC_SlotSelective;

public class PCis_ContainerCompressor extends PC_GresBaseWithInventory {

	protected List<Slot> lSlot;
	protected PCis_CompressorInventory inv;
	
	public PCis_ContainerCompressor(EntityPlayer player, Object[] o) {
		super(player, o);
	}

	@Override
	protected void init(Object[] o) {
		int index = thePlayer.inventory.currentItem;
		inventoryPlayerLower[index][0] = new PC_SlotNoPickup(thePlayer.inventory, index);
		inventorySlots.set(index, inventoryPlayerLower[index][0]);
	}

	@Override
	protected List<Slot> getAllSlots(List<Slot> slots) {
		lSlot = new ArrayList<Slot>();
		inv = new PCis_CompressorInventory(thePlayer);
		for(int i=0; i<inv.getSizeInventory(); i++){
			lSlot.add(new PC_SlotSelective(inv, i, 0, 0));
		}
		slots.addAll(lSlot);
		return slots;
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
		inv.closeChest();
		super.onCraftGuiClosed(par1EntityPlayer);
	}
	
}
