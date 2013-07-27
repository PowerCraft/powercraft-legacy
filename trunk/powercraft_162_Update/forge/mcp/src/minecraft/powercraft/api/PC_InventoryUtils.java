package powercraft.api;


import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;


public class PC_InventoryUtils {

	public static void dropInventoryContent(World world, double x, double y, double z, IInventory inventory) {

		if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				ItemStack itemStack = inventory.getStackInSlot(i);
				if (itemStack != null) {
					inventory.setInventorySlotContents(i, null);
					float f = 0.7F;
					double d0 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5;
					double d1 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5;
					double d2 = (world.rand.nextFloat() * f) + (1.0F - f) * 0.5;
					EntityItem entityitem = new EntityItem(world, x + d0, y + d1, z + d2, itemStack);
					entityitem.delayBeforeCanPickup = 10;
					world.spawnEntityInWorld(entityitem);
				}
			}
		}
	}


	public static void loadInventoryFromNBT(IInventory inventory, NBTTagCompound nbtTagCompound, String key) {

		NBTTagList list = nbtTagCompound.getTagList(key);
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound nbtTagCompound2 = (NBTTagCompound) list.tagAt(i);
			inventory.setInventorySlotContents(nbtTagCompound2.getInteger("slot"), ItemStack.loadItemStackFromNBT(nbtTagCompound2));
		}
	}


	public static void saveInventoryToNBT(IInventory inventory, NBTTagCompound nbtTagCompound, String key) {

		NBTTagList list = new NBTTagList();
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack itemStack = inventory.getStackInSlot(i);
			if (itemStack != null) {
				NBTTagCompound nbtTagCompound2 = new NBTTagCompound();
				nbtTagCompound2.setInteger("slot", i);
				itemStack.writeToNBT(nbtTagCompound2);
				list.appendTag(nbtTagCompound2);
			}
		}
		nbtTagCompound.setTag(key, list);
	}
	
	public static int getInventoryFullSlots(IInventory inv){
		return getInventoryFullSlots(inv, (int[])null);
	}
	
	public static int getInventoryFullSlots(IInventory inv, PC_Direction side){
		return getInventoryFullSlots(inv, getInvIndexesForSide(inv, side));
	}
	
	public static int getInventoryFullSlots(IInventory inv, int[] indexes){
		int fullSlots=0;
		if(indexes==null){
			int size = inv.getSizeInventory();
			for (int i = 0; i < size; i++) {
				ItemStack slot = inv.getStackInSlot(i);
				if (slot != null) {
					fullSlots++;
				}
			}
		}else{
			for (int j = 0; j < indexes.length; j++) {
				int i=indexes[j];
				ItemStack slot = inv.getStackInSlot(i);
				if (slot != null) {
					fullSlots++;
				}
			}
		}
		return fullSlots;
	}
	
	public static int[] getInvIndexesForSide(IInventory inv, PC_Direction side){
		if(side==null)
			return null;
		int sideID = side.getMCDir();
		if(inv instanceof ISidedInventory && sideID>=0){
			return ((ISidedInventory) inv).getAccessibleSlotsFromSide(sideID);
		}
		return null;
	}

}
