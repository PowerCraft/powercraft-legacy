package powercraft.itemstorage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import powercraft.api.PC_VecI;
import powercraft.api.inventory.PC_IInventory;

public abstract class PCis_CompressorInventory implements PC_IInventory {

	protected EntityPlayer player;
	protected PC_VecI size;
	protected int equipped;
	protected ItemStack compressor;
	
	public PCis_CompressorInventory(EntityPlayer player, int equipment, PC_VecI size){
		this.player = player;
		equipped = equipment;
		compressor = player.inventory.getStackInSlot(equipped);
		this.size = size;
	}
	
	public PC_VecI getSize(){
		return size.copy();
	}

	@Override
	public String getInvName() {
		return "Compressor";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void onInventoryChanged() {}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openChest() {}

	@Override
	public boolean canPlayerInsertStackTo(int slot, ItemStack stack) {
		return stack.itemID != PCis_App.compressor.itemID;
	}

	@Override
	public boolean canDispenseStackFrom(int slot) {
		return false;
	}

	@Override
	public boolean canDropStackFrom(int slot) {
		return false;
	}
	

	@Override
	public int getSlotStackLimit(int slotIndex) {
		return getInventoryStackLimit();
	}

	@Override
	public boolean canPlayerTakeStack(int slotIndex, EntityPlayer entityPlayer) {
		return true;
	}
	
	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

	@Override
	public int[] getSizeInventorySide(int var1) {
		return null;
	}

	@Override
	public boolean func_102007_a(int i, ItemStack itemstack, int j) {
		return false;
	}

	@Override
	public boolean func_102008_b(int i, ItemStack itemstack, int j) {
		return false;
	}
	
}
