package powercraft.itemstorage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_ISpecialAccessInventory;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_Utils.SaveHandler;
import powercraft.management.PC_VecI;

public class PCis_CompressorInventory implements IInventory, PC_ISpecialAccessInventory {

	private EntityPlayer player;
	private PC_VecI size = new PC_VecI(9, 3);
	private ItemStack[] is;
	private int equipped;
	private boolean useEnderChest;
	
	public PCis_CompressorInventory(EntityPlayer player){
		this.player = player;
		equipped = player.inventory.currentItem;
		ItemStack compressor = player.inventory.getStackInSlot(equipped);
		useEnderChest = compressor.getItemDamage()==PCis_ItemCompressor.ENDERACCESS;
		if(!useEnderChest){
			NBTTagCompound tag = compressor.getTagCompound();
			if(tag==null){
				compressor.setTagCompound(tag = new NBTTagCompound());
				SaveHandler.saveToNBT(tag, "invSize", size);
				is = new ItemStack[size.x*size.y];
			}else{
				SaveHandler.loadFromNBT(tag, "invSize", size);
				is = new ItemStack[size.x*size.y];
				PC_InvUtils.loadInventoryFromNBT(tag, "inv", this);
			}
		}
	}
	
	public PC_VecI getSize(){
		return size.copy();
	}
	
	@Override
	public int getSizeInventory() {
		if(useEnderChest)
			return player.getInventoryEnderChest().getSizeInventory();
		return is.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		if(useEnderChest)
			return player.getInventoryEnderChest().getStackInSlot(var1);
		return is[var1];
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		if(useEnderChest)
			return player.getInventoryEnderChest().decrStackSize(var1, var2);
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
		if(useEnderChest)
			return player.getInventoryEnderChest().getStackInSlotOnClosing(var1);
		return is[var1];
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		if(useEnderChest)
			player.getInventoryEnderChest().setInventorySlotContents(var1, var2);
		else
			is[var1] = var2;
	}

	@Override
	public String getInvName() {
		return "Compressor";
	}

	@Override
	public int getInventoryStackLimit() {
		if(useEnderChest)
			player.getInventoryEnderChest().getInventoryStackLimit();
		return 64;
	}

	@Override
	public void onInventoryChanged() {}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openChest() {
		if(useEnderChest)
			player.getInventoryEnderChest().openChest();
	}

	@Override
	public void closeChest() {
		if(useEnderChest)
			player.getInventoryEnderChest().closeChest();
		else{
			ItemStack backpack = player.inventory.getStackInSlot(equipped);
			if(backpack!=null)
				PC_InvUtils.saveInventoryToNBT(backpack.getTagCompound(), "inv", this);
		}
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
		return stack.itemID != PCis_App.compressor.itemID;
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
