package powercraft.api.inventory;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import powercraft.api.PC_Utils;
import powercraft.api.items.PC_Item;


public class PC_InventoryUtils {

	public static void dropInventoryContent(IInventory inventory, World world, double x, double y, double z) {
		if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
			int size = inventory.getSizeInventory();
			for (int i = 0; i < size; i++) {
				if(inventory instanceof PC_IInventory){
					if(!((PC_IInventory)inventory).canDropStack(i))
						continue;
				}
				ItemStack itemStack = inventory.getStackInSlot(i);
				if (itemStack != null) {
					inventory.setInventorySlotContents(i, null);
					PC_Utils.spawnItem(world, x, y, z, itemStack);
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
		int size = inventory.getSizeInventory();
		for (int i = 0; i < size; i++) {
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

	public static int getSlotStackLimit(IInventory inventory, int i){
		if(inventory instanceof PC_IInventory){
			((PC_IInventory)inventory).getSlotStackLimit(i);
		}
		return inventory.getInventoryStackLimit();
	}

	public static void onTick(IInventory inventory, World world) {
		int size = inventory.getSizeInventory();
		for(int i=0; i<size; i++){
			ItemStack itemStack = inventory.getStackInSlot(i);
			if(itemStack!=null){
				Item item = itemStack.getItem();
				if(item instanceof PC_Item){
					((PC_Item)item).onTick(itemStack, world, inventory, i);
				}
			}
		}
	}

	public static IInventory getInventoryFromEntity(Entity entity) {
		if(entity instanceof PC_IInventoryProvider){
			return ((PC_IInventoryProvider)entity).getInventory();
		}else if(entity instanceof IInventory){
			return (IInventory)entity;
		}else if(entity instanceof EntityPlayer){
			return ((EntityPlayer)entity).inventory;
		}else if(entity instanceof EntityLiving){
			return new PC_WrapperInventory(((EntityLiving)entity).getLastActiveItems());
		}
		return null;
	}
	
}
