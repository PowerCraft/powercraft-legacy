package powercraft.machines;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import powercraft.core.PC_ISpecialAccessInventory;
import powercraft.core.PC_IStateReportingInventory;
import powercraft.core.PC_TileEntity;

public class PCma_TileEntityTransmutabox extends PC_TileEntity implements IInventory, PC_ISpecialAccessInventory, PC_IStateReportingInventory {

	@Override
	public boolean insertStackIntoInventory(ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needsSpecialInserter() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canPlayerInsertStackTo(int slot, ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canMachineInsertStackTo(int slot, ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDispenseStackFrom(int slot) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		
	}

	@Override
	public String getInvName() {
		return "Transmutabox Inventory";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isContainerEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isContainerFull() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasContainerNoFreeSlots() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasInventoryPlaceFor(ItemStack itemStack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isContainerEmptyOf(ItemStack itemStack) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
