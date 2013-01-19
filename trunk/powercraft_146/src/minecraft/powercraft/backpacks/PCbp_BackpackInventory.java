package powercraft.backpacks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_ISpecialAccessInventory;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_Utils.SaveHandler;
import powercraft.management.PC_VecI;

public class PCbp_BackpackInventory implements IInventory, PC_ISpecialAccessInventory {

	private EntityPlayer player;
	private PC_VecI size = new PC_VecI();
	private ItemStack[] is;
	private int equipped;
	
	public PCbp_BackpackInventory(EntityPlayer player){
		this.player = player;
		equipped = player.inventory.currentItem;
		ItemStack backpack = player.inventory.getStackInSlot(equipped);
		NBTTagCompound tag = backpack.getTagCompound();
		if(tag==null){
			backpack.setTagCompound(tag = new NBTTagCompound());
			SaveHandler.saveToNBT(tag, "invSize", size = new PC_VecI(9, 3));
			is = new ItemStack[size.x*size.y];
		}else{
			SaveHandler.loadFromNBT(tag, "invSize", size);
			is = new ItemStack[size.x*size.y];
			PC_InvUtils.loadInventoryFromNBT(tag, "inv", this);
		}
	}
	
	public PC_VecI getSize(){
		return size.copy();
	}
	
	@Override
	public int getSizeInventory() {
		return is.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return is[var1];
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		if (is[var1] != null) {
			if (is[var1].stackSize <= var2) {
				ItemStack itemstack = is[var1];
				is[var1] = null;
				onInventoryChanged();
				return itemstack;
			}
			ItemStack itemstack1 = is[var1].splitStack(var2);
			if (is[var1].stackSize == 0) {
				is[var1] = null;
			}
			onInventoryChanged();
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return is[var1];
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		is[var1] = var2;
	}

	@Override
	public String getInvName() {
		return "Backpack";
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
	public void closeChest() {
		ItemStack backpack = player.inventory.getStackInSlot(equipped);
		if(backpack!=null)
			PC_InvUtils.saveInventoryToNBT(backpack.getTagCompound(), "inv", this);
	}

	@Override
	public boolean insertStackIntoInventory(ItemStack stack) {
		return false;
	}

	@Override
	public boolean needsSpecialInserter() {
		return false;
	}

	@Override
	public boolean canPlayerInsertStackTo(int slot, ItemStack stack) {
		return stack.itemID != PCbp_App.backpack.shiftedIndex;
	}

	@Override
	public boolean canMachineInsertStackTo(int slot, ItemStack stack) {
		return false;
	}

	@Override
	public boolean canDispenseStackFrom(int slot) {
		return false;
	}

	@Override
	public boolean canDropStackFrom(int slot) {
		return false;
	}

}
