package net.minecraft.src;

public class PCma_TileEntityReplacer extends TileEntity implements IInventory, PC_ISelectiveInventory {

	public ItemStack buildBlock;
	public static final int MAXSTACK = 1;
	public static final int SIZE = 1;
	public int coordOffset[] = {0, 1, 0};
	
	@Override
	public boolean canInsertStackTo(int slot, ItemStack stack) {
		if(stack.getItem() instanceof ItemBlock)
			return true;
		return false;
	}

	@Override
	public int getSizeInventory() {
		return SIZE;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return buildBlock;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(j>0){
			ItemStack itemStack = buildBlock;
			buildBlock = null;
			return itemStack;
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (buildBlock != null) {
			ItemStack itemstack = buildBlock;
			buildBlock = null;
			return itemstack;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		buildBlock = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return "Block Replacer";
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
		
		if(nbttaglist.tagCount()>0){
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(0);
			buildBlock = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		}
		for(int i=0; i<3; i++)
			coordOffset[i] = nbttagcompound.getInteger("coordOffset["+i+"]");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		NBTTagList nbttaglist = new NBTTagList();
		if (buildBlock != null) {
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			buildBlock.writeToNBT(nbttagcompound1);
			nbttaglist.appendTag(nbttagcompound1);
		}

		nbttagcompound.setTag("Items", nbttaglist);
		for(int i=0; i<3; i++)
			nbttagcompound.setInteger("coordOffset["+i+"]", coordOffset[i]);
	}
	
	@Override
	public int getInventoryStackLimit() {
		return MAXSTACK;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return false;
	}

	@Override
	public void openChest() {

	}

	@Override
	public void closeChest() {

	}

}
