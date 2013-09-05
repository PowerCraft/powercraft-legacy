package powercraft.api.blocks;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.api.PC_IInventory;
import powercraft.api.PC_InventoryUtils;


public abstract class PC_TileEntityWithInventory extends PC_TileEntity implements PC_IInventory {

	protected ItemStack[] inventoryContents;


	protected PC_TileEntityWithInventory(int size) {

		inventoryContents = new ItemStack[size];
	}


	@Override
	public int getSizeInventory() {

		return inventoryContents.length;
	}


	@Override
	public ItemStack getStackInSlot(int i) {

		return inventoryContents[i];
	}


	@Override
	public ItemStack decrStackSize(int i, int j) {

		if (inventoryContents[i] != null) {
			ItemStack itemstack;
			if (inventoryContents[i].stackSize <= j) {
				itemstack = this.inventoryContents[i];
				inventoryContents[i] = null;
				onInventoryChanged();
				return itemstack;
			} 
			itemstack = this.inventoryContents[i].splitStack(j);
			if (this.inventoryContents[i].stackSize == 0) {
				this.inventoryContents[i] = null;
			}
			onInventoryChanged();
			return itemstack;
		}
		return null;
	}


	@Override
	public ItemStack getStackInSlotOnClosing(int i) {

		if (this.inventoryContents[i] != null) {
			ItemStack itemstack = this.inventoryContents[i];
			this.inventoryContents[i] = null;
			return itemstack;
		} 
		return null;
	}


	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {

		inventoryContents[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getSlotStackLimit(i)) {
			itemstack.stackSize = getSlotStackLimit(i);
		}
		onInventoryChanged();
	}


	@Override
	public String getInvName() {

		return null;
	}


	@Override
	public boolean isInvNameLocalized() {

		return false;
	}


	@Override
	public int getInventoryStackLimit() {

		return 64;
	}


	@Override
	public int getSlotStackLimit(int i) {

		return getInventoryStackLimit();
	}


	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {

		return getDistanceFrom(entityplayer.posX, entityplayer.posY, entityplayer.posZ) < 64;
	}


	@Override
	public void openChest() {

	}


	@Override
	public void closeChest() {

	}


	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {

		return itemstack != null && itemstack.stackSize > 0;
	}


	@Override
	public boolean canTakeStack(int i, EntityPlayer entityPlayer) {

		return true;
	}


	@Override
	public void onBlockBreak() {

		PC_InventoryUtils.dropInventoryContent(worldObj, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, this);
	}


	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {

		super.readFromNBT(nbtTagCompound);
		PC_InventoryUtils.loadInventoryFromNBT(this, nbtTagCompound, "Inventory");
	}


	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {

		super.writeToNBT(nbtTagCompound);
		PC_InventoryUtils.saveInventoryToNBT(this, nbtTagCompound, "Inventory");
	}

}
