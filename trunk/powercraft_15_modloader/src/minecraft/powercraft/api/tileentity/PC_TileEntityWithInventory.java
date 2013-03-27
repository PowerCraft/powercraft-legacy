package powercraft.api.tileentity;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import powercraft.api.inventory.PC_IInventory;
import powercraft.api.inventory.PC_InventoryUtils;

public class PC_TileEntityWithInventory extends PC_TileEntity implements PC_IInventory {

	protected String inventoryTitle;
	protected int slotsCount;
	protected ItemStack inventoryContents[];
	
	protected PC_TileEntityWithInventory(String title, int size) {
		inventoryTitle = title;
		slotsCount = size;
		inventoryContents = new ItemStack[slotsCount];
	}
	
	@Override
	public int getSizeInventory() {
		return slotsCount;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventoryContents[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (this.inventoryContents[i] != null) {
			ItemStack itemstack;

			if (this.inventoryContents[i].stackSize <= j) {
				itemstack = this.inventoryContents[i];
				this.inventoryContents[i] = null;
				this.onInventoryChanged();
				return itemstack;
			} else {
				itemstack = this.inventoryContents[i].splitStack(j);

				if (this.inventoryContents[i].stackSize == 0) {
					this.inventoryContents[i] = null;
				}

				this.onInventoryChanged();
				return itemstack;
			}
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (this.inventoryContents[i] != null) {
			ItemStack itemstack = this.inventoryContents[i];
			this.inventoryContents[i] = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.inventoryContents[i] = itemstack;

		if (itemstack != null
				&& itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}

		this.onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return inventoryTitle;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}
	
	@Override
	public boolean canPlayerInsertStackTo(int i, ItemStack itemstack) {
		return true;
	}
	
	@Override
	public boolean canPlayerTakeStack(int i, EntityPlayer entityPlayer) {
		return true;
	}

	@Override
	public boolean canDispenseStackFrom(int i) {
		return true;
	}

	@Override
	public boolean canDropStackFrom(int i) {
		return true;
	}

	@Override
	public int getSlotStackLimit(int i) {
		return getInventoryStackLimit();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		PC_InventoryUtils.loadInventoryFromNBT(nbtTagCompound, "inventory", this);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		PC_InventoryUtils.saveInventoryToNBT(nbtTagCompound, "inventory", this);
	}
	
	@Override
	public void onBreakBlock() {
		super.onBreakBlock();
		PC_InventoryUtils.dropInventoryContents(this, worldObj, getCoord());
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int var1, ItemStack var2) {
		return true;
	}

	@Override
	public int[] getSizeInventorySide(int var1) {
		return null;
	}

	@Override
	public boolean func_102007_a(int var1, ItemStack var2, int var3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean func_102008_b(int var1, ItemStack var2, int var3) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
