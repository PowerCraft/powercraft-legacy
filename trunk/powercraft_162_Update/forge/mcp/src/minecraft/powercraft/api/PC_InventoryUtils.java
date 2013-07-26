package powercraft.api;


import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
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

}
