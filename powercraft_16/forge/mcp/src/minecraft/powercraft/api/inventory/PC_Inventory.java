package powercraft.api.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class PC_Inventory implements IInventory {

	public static final int USEABLEBYPLAYER = 1;
	public static final int SIDEINSERTABLE = 2;
	public static final int SIDEEXTRACTABLE = 4;
	public static final int DROPSTACKS = 8;
	
	private final String name;
	private final ItemStack[] inventoryContents;
	private final int stackLimit;
	private final int flags;
	private IInventory parentInventory;
	
	public PC_Inventory(String name, int size, int stackLimit, int flags){
		this.name = name;
		inventoryContents = new ItemStack[size];
		this.stackLimit = stackLimit;
		this.flags = flags;
	}
	
	public void setParentInventory(IInventory parentInventory){
		this.parentInventory = parentInventory;
	}
	
	public IInventory getParentInventory(){
		return parentInventory;
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
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return name;
	}

	@Override
	public boolean isInvNameLocalized() {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return stackLimit;
	}

	@Override
	public void onInventoryChanged() {
		if(parentInventory!=null)
			parentInventory.onInventoryChanged();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if((flags & USEABLEBYPLAYER)==0){
			return false;
		}
		if(parentInventory!=null)
			return parentInventory.isUseableByPlayer(entityplayer);
		return true;
	}

	@Override
	public void openChest() {
		if(parentInventory!=null)
			parentInventory.openChest();
	}

	@Override
	public void closeChest() {
		if(parentInventory!=null)
			parentInventory.closeChest();
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
	
	public boolean canInsertItem(int i, ItemStack itemstack) {
		if(!isItemValidForSlot(i, itemstack))
			return false;
		return (flags & SIDEINSERTABLE)!=0;
	}

	public boolean canExtractItem(int i, ItemStack itemstack) {
		if(!isItemValidForSlot(i, itemstack))
			return false;
		return (flags & SIDEEXTRACTABLE)!=0;
	}

	public boolean canDropStacks() {
		return (flags & DROPSTACKS)!=0;
	}
	
}
