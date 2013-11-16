package powercraft.api.blocks;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import powercraft.api.PC_Direction;
import powercraft.api.PC_FieldDescription;
import powercraft.api.inventory.PC_IInventory;
import powercraft.api.inventory.PC_InventoryUtils;
import powercraft.api.inventory.PC_Inventory;


public abstract class PC_TileEntityWithInventory extends PC_TileEntity implements PC_IInventory {

	private final String name;
	private final PC_Inventory[] inventories;
	@PC_FieldDescription(withPermissionClientSync=true, sync=true)
	private int side2IdMaper[] = {-1, -1, -1, -1, -1, -1};
	private final int slotsForID[][];
	private final int sideIdOutputs[];
	
	public PC_TileEntityWithInventory(String name, PC_Inventory[] inventories, int[] outputs, int[]... slotsForID){
		this.name = name;
		this.inventories = inventories;
		for(int i=0; i<inventories.length; i++){
			inventories[i].setParentInventory(this);
		}
		this.slotsForID = slotsForID;
		sideIdOutputs = outputs;
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
		int[] sides = getAppliedSides(i);
		if(sides!=null && sides.length>0){
			List<Integer> outSides = new ArrayList<Integer>();
			for(int j=0; j<sideIdOutputs.length; j++){
				for(int k=0; k<sides.length; k++){
					if(sides[k] == sideIdOutputs[j]){
						if(!outSides.contains(sides[k]))
							outSides.add(sides[k]);
					}
				}
			}
			while(itemstack!=null && !outSides.isEmpty()){
				itemstack = PC_InventoryUtils.tryToMove(worldObj, xCoord, yCoord, zCoord, outSides.remove((int)(Math.random()*outSides.size())), itemstack);
			}
			if(itemstack==null)
				return;
		}
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
		int groupID = getIDForSide(side);
		if(groupID==-1){
			return new int[0];
		}
		return slotsForID[groupID];
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
	
	@Override
	public int[] getAppliedSides(int i) {
		List<Integer> sides = new ArrayList<Integer>();
		for(int j=0; j<side2IdMaper.length; j++){
			int groupID = side2IdMaper[j];
			if(groupID!=-1 && !sides.contains(groupID)){
				for(int k=0; k<slotsForID[groupID].length; k++){
					if(slotsForID[groupID][k] == i){
						sides.add(groupID);
						break;
					}
				}
			}
		}
		if(sides.isEmpty())
			return null;
		int[] a = new int[sides.size()];
		for(int j=0; j<a.length; j++){
			a[j] = sides.get(j);
		}
		Arrays.sort(a);
		return a;
	}

	public PC_Inventory getSubInventoryByID(int i){
		return inventories[i];
	}

	public PC_Inventory getSubInventoryByName(String name){
		for(PC_Inventory inv:inventories){
			if(inv.getInvName().equals(name)) return inv;
		}
		return null;
	}
	
	public PC_Inventory getSubInventoryForSlot(int i){
		return getSlotInfoByGlobalSlotNum(i).inventory;
	}
	
	public int getIDForSide(int side){
		return getIDForSide(PC_Direction.getOrientation(side));
	}
	
	public int getIDForSide(PC_Direction side){
		return side2IdMaper[getBlock().getBlockRotation(worldObj, xCoord, yCoord, zCoord, side.ordinal()).ordinal()];
	}
	
	public boolean isSlotCompatibleWithSide(int i, int side){
		int groupID = getIDForSide(side);
		if(groupID==-1)
			return false;
		int[] slotsForSide = slotsForID[groupID];
		for(int j=0; j<slotsForSide.length; j++){
			if(slotsForSide[j]==i)
				return true;
		}
		return false;
	}
	
	@Override
	public final void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		PC_InventoryUtils.loadInventoryFromNBT(this, nbtTagCompound, "Inventory");
	}

	@Override
	public final void writeToNBT(NBTTagCompound nbtTagCompound) {
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

	public int getGroupCount() {
		return slotsForID.length;
	}

	public void setSideGroup(int i, int j) {
		side2IdMaper[i] = j;
		sendToServer();
	}
	
	public int getSideGroup(int i) {
		return side2IdMaper[i];
	}
	
}
