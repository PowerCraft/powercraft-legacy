package powercraft.itemstorage;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import powercraft.api.gres.PC_GresBaseWithInventory;
import powercraft.api.inventory.PC_Slot;
import powercraft.api.inventory.PC_SlotNoPickup;
import powercraft.api.tileentity.PC_TileEntity;

public class PCis_ContainerCompressor extends PC_GresBaseWithInventory<PC_TileEntity> {

	protected PCis_CompressorInventory inv;
	protected int index;
	
	public PCis_ContainerCompressor(EntityPlayer player, PC_TileEntity te, Object[] o) {
		super(player, te, o);
	}

	public ItemStack getItem(){
		return thePlayer.inventory.getStackInSlot(index);
	}
	
	@Override
	protected void init(Object[] o) {
		index = thePlayer.inventory.currentItem;
		inventoryPlayerLower[index][0] = new PC_SlotNoPickup(thePlayer.inventory, index);
		inventorySlots.set(index, inventoryPlayerLower[index][0]);
		inventoryPlayerLower[index][0].slotNumber = index;
	}

	@Override
	protected PC_Slot[] getAllSlots() {
		inv = PCis_ItemCompressor.getInventoryFor(thePlayer, -1);
		invSlots = new PC_Slot[inv.getSizeInventory()];
		for(int i=0; i<invSlots.length; i++){
			invSlots[i] = new PC_Slot(inv, i);
		}
		return invSlots;
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
		inv.closeChest();
		super.onCraftGuiClosed(par1EntityPlayer);
	}

	@Override
	protected boolean canShiftTransfer() {
		return true;
	}
	
}
