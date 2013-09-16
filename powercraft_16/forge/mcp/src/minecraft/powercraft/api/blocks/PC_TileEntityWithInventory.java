package powercraft.api.blocks;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.api.inventory.PC_IInventory;
import powercraft.api.inventory.PC_InventoryUtils;
import powercraft.api.inventory.PC_Inventory;


public abstract class PC_TileEntityWithInventory extends PC_TileEntity implements PC_IInventory {

	private final String name;
	private final PC_Inventory[] inventories;
	private final int side2IdMaper[] = new int[6];
	private final int slotsForID[][];
	
	public PC_TileEntityWithInventory(String name, PC_Inventory[] inventories, int[]... slotsForID){
		this.name = name;
		this.inventories = inventories;
		for(int i=0; i<inventories.length; i++){
			inventories[i].setParentInventory(this);
		}
		this.slotsForID = slotsForID;
	}
	
	@Override
	public int getSizeInventory() {
		int size = 0;
		for(int i=0; i<inventories.length; i++){
			size += inventories[i].getSizeInventory();
		}
		return size;
	}

	private SlotData getSlotInfoByGlobalSlotNum(int i){
		if(i<0)
			throw new IndexOutOfBoundsException();
		for(int j=0; j<inventories.length; j++){
			int size = inventories[j].getSizeInventory();
			if(i<size){
				return new SlotData(i, inventories[j]);
			}
			i -= size;
		}
		throw new IndexOutOfBoundsException();
	}
	
	@Override
	public ItemStack getStackInSlot(int i) {
		SlotData sd = getSlotInfoByGlobalSlotNum(i);
		return sd.inventory.getStackInSlot(sd.slot);
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		SlotData sd = getSlotInfoByGlobalSlotNum(i);
		return sd.inventory.decrStackSize(sd.slot, j);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		SlotData sd = getSlotInfoByGlobalSlotNum(i);
		return sd.inventory.getStackInSlotOnClosing(sd.slot);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		SlotData sd = getSlotInfoByGlobalSlotNum(i);
		sd.inventory.setInventorySlotContents(sd.slot, itemstack);
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
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		SlotData sd = getSlotInfoByGlobalSlotNum(i);
		return sd.inventory.isItemValidForSlot(sd.slot, itemstack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return slotsForID[side2IdMaper[side]];
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int side) {
		if(isSlotCompatibleWithSide(i, side)){
			SlotData sd = getSlotInfoByGlobalSlotNum(i);
			return sd.inventory.canInsertItem(sd.slot, itemstack);
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int side) {
		if(isSlotCompatibleWithSide(i, side)){
			SlotData sd = getSlotInfoByGlobalSlotNum(i);
			return sd.inventory.canExtractItem(sd.slot, itemstack);
		}
		return false;
	}

	@Override
	public int getSlotStackLimit(int i) {
		SlotData sd = getSlotInfoByGlobalSlotNum(i);
		return PC_InventoryUtils.getSlotStackLimit(sd.inventory, sd.slot);
	}

	@Override
	public boolean canTakeStack(int i, EntityPlayer entityPlayer) {
		return true;
	}
	
	@Override
	public boolean canDropStack(int i) {
		SlotData sd = getSlotInfoByGlobalSlotNum(i);
		return sd.inventory.canDropStacks();
	}
	
	@Override
	public void onTick(World world) {
		for(int i=0; i<inventories.length; i++){
			PC_InventoryUtils.onTick(inventories[i], world);
		}
	}

	public PC_Inventory getSubInventoryByID(int i){
		return inventories[i];
	}

	public PC_Inventory getSubInventoryByName(String name){
		for(PC_Inventory inv:inventories){
			if(inv.getInvName()==name) return inv;
		}
		return null;
	}
	
	public PC_Inventory getSubInventoryForSlot(int i){
		return getSlotInfoByGlobalSlotNum(i).inventory;
	}
	
	public int getIDForSide(int side){
		return side2IdMaper[side];
	}
	
	public boolean isSlotCompatibleWithSide(int i, int side){
		int[] slotsForSide = slotsForID[side2IdMaper[side]];
		for(int j=0; j<slotsForSide.length; j++){
			if(slotsForSide[j]==i)
				return true;
		}
		return false;
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
	
	@Override
	public void onBlockBreak() {
		PC_InventoryUtils.dropInventoryContent(this, worldObj, xCoord, yCoord, zCoord);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		PC_InventoryUtils.onTick(this, worldObj);
	}

	private static class SlotData{

		public final int slot;
		public final PC_Inventory inventory;
		
		public SlotData(int slot, PC_Inventory inventory) {
			this.slot = slot;
			this.inventory = inventory;
		}
		
	}
	
}
