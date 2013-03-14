package powercraft.api.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.PC_VecI;
import powercraft.api.registry.PC_RecipeRegistry;

public class PC_InventoryUtils {

	public static void loadInventoryFromNBT(NBTTagCompound outerTag, String invTagName, IInventory inventory) {
		NBTTagList nbttaglist = outerTag.getTagList(invTagName);

		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			NBTBase nbtTag = nbttagcompound1.getTag("Slot");
			int j = -1;
			if (nbtTag instanceof NBTTagByte) {
				j = ((NBTTagByte) nbtTag).data & 0xff;
			} else if (nbtTag instanceof NBTTagInt) {
				j = ((NBTTagInt) nbtTag).data;
			}

			if (j >= 0 && j < inventory.getSizeInventory()) {
				inventory.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound1));
			}
		}
	}

	public static void saveInventoryToNBT(NBTTagCompound outerTag, String invTagName, IInventory inventory) {
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (inventory.getStackInSlot(i) != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setInteger("Slot", i);
				inventory.getStackInSlot(i).writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		outerTag.setTag(invTagName, nbttaglist);
	}

	public static IInventory getBlockInventoryAt(IBlockAccess world, PC_VecI pos) {
		TileEntity te = GameInfo.getTE(world, pos);

		if (te == null) {
			return null;
		}

		if (te instanceof PC_IInventoryWrapper) {
			return ((PC_IInventoryWrapper) te).getInventory();
		}

		if (!(te instanceof IInventory)) {
			return null;
		}

		IInventory inv = (IInventory) te;
		int id = GameInfo.getBID(world, pos);

		if (id == Block.chest.blockID) {
			if (GameInfo.getBID(world, pos.offset(-1, 0, 0)) == Block.chest.blockID) {
				inv = new InventoryLargeChest("Large chest", (IInventory) GameInfo.getTE(world, pos.offset(-1, 0, 0)), inv);
			}

			if (GameInfo.getBID(world, pos.offset(1, 0, 0)) == Block.chest.blockID) {
				inv = new InventoryLargeChest("Large chest", inv, (IInventory) GameInfo.getTE(world, pos.offset(1, 0, 0)));
			}

			if (GameInfo.getBID(world, pos.offset(0, 0, -1)) == Block.chest.blockID) {
				inv = new InventoryLargeChest("Large chest", (IInventory) GameInfo.getTE(world, pos.offset(0, 0, -1)), inv);
			}

			if (GameInfo.getBID(world, pos.offset(0, 0, 1)) == Block.chest.blockID) {
				inv = new InventoryLargeChest("Large chest", inv, (IInventory) GameInfo.getTE(world, pos.offset(0, 0, 1)));
			}
		}
		
		return inv;
	}

	public static IInventory getInventoryAt(World world, int x, int y, int z) {
		IInventory invAt = getBlockInventoryAt(world, new PC_VecI(x, y, z));

		if (invAt != null) {
			return invAt;
		}

		List<IInventory> list = world.getEntitiesWithinAABB(
				IInventory.class,
				AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1)
						.expand(0.6D, 0.6D, 0.6D));

		if (list.size() >= 1) {
			return list.get(0);
		}

		List<PC_IInventoryWrapper> list2 = world.getEntitiesWithinAABB(
				PC_IInventoryWrapper.class,
				AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1)
						.expand(0.6D, 0.6D, 0.6D));

		if (list2.size() >= 1) {
			return list2.get(0).getInventory();
		}

		return null;
	}
	
	public static int getInvStartIndexForSide(IInventory inv, int side){
		if(inv instanceof ISidedInventory && side>=0){
			return ((ISidedInventory) inv).func_94127_c(side);
		}
		return 0;
	}
	
	public static int getInvSizeForSide(IInventory inv, int side){
		if(inv instanceof ISidedInventory && side>=0){
			return ((ISidedInventory) inv).func_94128_d(side);
		}
		return inv.getSizeInventory();
	}
	
	public static int getSlotStackLimit(IInventory inv, int i){
		if(inv instanceof PC_IInventory){
			return ((PC_IInventory) inv).getSlotStackLimit(i);
		}
		return inv.getInventoryStackLimit();
	}
	
	public static int getFirstEmptySlot(IInventory inv, ItemStack itemstack, int side){
		return getFirstEmptySlot(inv, itemstack, getInvStartIndexForSide(inv, side), getInvSizeForSide(inv, side));
	}
	
	public static int getFirstEmptySlot(IInventory inv, ItemStack itemstack, int startIndex, int size){
		for (int i = startIndex; i < size; i++) {
			if (inv.getStackInSlot(i) == null) {
				if(inv.func_94041_b(i, itemstack))
					return i;
			}
		}
		return -1;
	}
	
	public static int getSlotWithPlaceFor(IInventory inv, ItemStack itemstack, int side){
		return getSlotWithPlaceFor(inv, itemstack, getInvStartIndexForSide(inv, side), getInvSizeForSide(inv, side));
	}
	
	public static int getSlotWithPlaceFor(IInventory inv, ItemStack itemstack, int startIndex, int size){
		for (int i = startIndex; i < size; i++) {
			ItemStack slot = inv.getStackInSlot(i);
			if (slot != null) {
				if(slot.isItemEqual(itemstack) && slot.getMaxStackSize()>slot.stackSize && getSlotStackLimit(inv, i)>slot.stackSize){
					if(inv.func_94041_b(i, itemstack))
						return i;
				}
			}
		}
		return getFirstEmptySlot(inv, itemstack, startIndex, size);
	}
	
	public static boolean storeItemStackToInventoryFrom(IInventory inv, ItemStack itemstack, int side){
		return storeItemStackToInventoryFrom(inv, itemstack, getInvStartIndexForSide(inv, side), getInvSizeForSide(inv, side));
	}
	
	public static boolean storeItemStackToInventoryFrom(IInventory inv, ItemStack itemstack, int startIndex, int size){
		while(itemstack.stackSize>0){
			int slot = getSlotWithPlaceFor(inv, itemstack, startIndex, size);
			if(slot<0)
				break;
			storeItemStackToSlot(inv, itemstack, slot);
		}
		return itemstack.stackSize==0;
	}
	
	public static boolean storeItemStackToSlot(IInventory inv, ItemStack itemstack, int i){
		ItemStack slot = inv.getStackInSlot(i);
		if (slot == null) {
			slot = itemstack.copy();
		}else{
			if(slot.isItemEqual(itemstack)){
				int store = getSlotStackLimit(inv, i);
				if(store>slot.getMaxStackSize()){
					store = slot.getMaxStackSize();
				}
				store -= slot.stackSize;
				if(store>0){
					if(store>itemstack.stackSize){
						store = itemstack.stackSize;
					}
					itemstack.stackSize -= store;
					slot.stackSize += store;
				}
			}
		}
		inv.setInventorySlotContents(i, slot);
		return itemstack.stackSize==0;
	}
	
	public static int getInventorySpaceFor(IInventory inv, ItemStack itemstack, int side){
		return getInventorySpaceFor(inv, itemstack, getInvStartIndexForSide(inv, side), getInvSizeForSide(inv, side));
	}
	
	public static int getInventorySpaceFor(IInventory inv, ItemStack itemstack, int startIndex, int size){
		int space=0;
		for (int i = startIndex; i < size; i++) {
			ItemStack slot = inv.getStackInSlot(i);
			int slotStackLimit = getSlotStackLimit(inv, i);
			if(itemstack==null){
				if (slot == null) {
					space += slotStackLimit;
				}
			}else{
				if(slotStackLimit>itemstack.getMaxStackSize()){
					slotStackLimit = itemstack.getMaxStackSize();
				}
				if (slot != null) {
					if(slot.isItemEqual(itemstack) && slotStackLimit>slot.stackSize){
						if(inv.func_94041_b(i, itemstack)){
							space += slotStackLimit-slot.stackSize;
						}
					}
				}else{
					space += slotStackLimit;
				}
			}
		}
		return space;
	}
	
	public static int getInventoryCountOf(IInventory inv, ItemStack itemstack, int side){
		return getInventoryCountOf(inv, itemstack, getInvStartIndexForSide(inv, side), getInvSizeForSide(inv, side));
	}
	
	public static int getInventoryCountOf(IInventory inv, ItemStack itemstack, int startIndex, int size){
		int count=0;
		for (int i = startIndex; i < size; i++) {
			ItemStack slot = inv.getStackInSlot(i);
			if (slot != null) {
				if(itemstack==null){
					count += slot.stackSize;
				}else{
					if(slot.isItemEqual(itemstack)){
						count += slot.stackSize;
					}
				}
			}
		}
		return count;
	}
	
	public static int getInventoryFreeSlots(IInventory inv, int side){
		return getInventoryFreeSlots(inv, getInvStartIndexForSide(inv, side), getInvSizeForSide(inv, side));
	}
	
	public static int getInventoryFreeSlots(IInventory inv, int startIndex, int size){
		int freeSlots=0;
		for (int i = startIndex; i < size; i++) {
			ItemStack slot = inv.getStackInSlot(i);
			if (slot == null) {
				freeSlots++;
			}
		}
		return freeSlots;
	}
	
	public static int getInventoryFullSlots(IInventory inv, int side){
		return getInventoryFullSlots(inv, getInvStartIndexForSide(inv, side), getInvSizeForSide(inv, side));
	}
	
	public static int getInventoryFullSlots(IInventory inv, int startIndex, int size){
		int fullSlots=0;
		for (int i = startIndex; i < size; i++) {
			ItemStack slot = inv.getStackInSlot(i);
			if (slot != null) {
				fullSlots++;
			}
		}
		return fullSlots;
	}
	
	public static void moveStacks(IInventory from, IInventory to) {
		for (int i = 0; i < from.getSizeInventory(); i++) {
			if (from.getStackInSlot(i) != null) {
				
				storeItemStackToInventoryFrom(to, from.getStackInSlot(i), -1);

				if (from.getStackInSlot(i) != null && from.getStackInSlot(i).stackSize <= 0) {
					from.setInventorySlotContents(i, null);
				}
			}
		}
	}
	
	public static ItemStack[] groupStacks(ItemStack[] input) {
		List<ItemStack> list = stacksToList(input);
		groupStacks(list);
		return stacksToArray(list);
	}

	public static void groupStacks(List<ItemStack> input) {
		if (input == null) {
			return;
		}

		for (ItemStack st1 : input) {
			if (st1 != null) {
				for (ItemStack st2 : input) {
					if (st2 != null && st2.isItemEqual(st1)) {
						int movedToFirst = Math.min(st2.stackSize, st1
								.getItem().getItemStackLimit()
								- st1.stackSize);

						if (movedToFirst <= 0) {
							break;
						}

						st1.stackSize += movedToFirst;
						st2.stackSize -= movedToFirst;
					}
				}
			}
		}

		ArrayList<ItemStack> copy = new ArrayList<ItemStack>(input);

		for (int i = copy.size() - 1; i >= 0; i--) {
			if (copy.get(i) == null || copy.get(i).stackSize <= 0) {
				input.remove(i);
			}
		}
	}

	public static List<ItemStack> stacksToList(ItemStack[] stacks) {
		ArrayList<ItemStack> myList = new ArrayList<ItemStack>();
		Collections.addAll(myList, stacks);
		return myList;
	}

	public static ItemStack[] stacksToArray(List<ItemStack> stacks) {
		return stacks.toArray(new ItemStack[stacks.size()]);
	}

	public static void dropInventoryContents(IInventory inventory, World world, PC_VecI pos) {
		Random random = new Random();
		if (inventory != null) {
			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				if (inventory instanceof PC_IInventory) {
					if (!((PC_IInventory) inventory).canDropStackFrom(i))
						continue;
				}
				ItemStack itemstack = inventory.getStackInSlot(i);
				inventory.setInventorySlotContents(i, null);
				ValueWriting.dropItemStack(world, itemstack, pos);
			}
		}
	}
	
	public static int useFuel(IInventory inv, int start, int end, World world, PC_VecI pos) {
		for (int i = start; i < end; i++) {
			ItemStack is = inv.getStackInSlot(i);
			int fuel = PC_RecipeRegistry.getFuelValue(is);
			if (fuel > 0) {
				inv.decrStackSize(i, 1);
				ItemStack container = GameInfo.getContainerItemStack(is);
				if (container != null) {
					storeItemStackToInventoryFrom(inv, container, start, end);
					if (container.stackSize > 0) {
						ValueWriting.dropItemStack(world, container, pos);
					}
				}
				return fuel;
			}
		}
		return 0;
	}
	
	
	
}
