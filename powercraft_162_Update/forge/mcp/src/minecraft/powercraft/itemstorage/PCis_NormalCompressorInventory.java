package powercraft.itemstorage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.SaveHandler;
import powercraft.api.inventory.PC_InventoryUtils;
import powercraft.api.utils.PC_Utils;
import powercraft.api.utils.PC_VecI;

public class PCis_NormalCompressorInventory extends PCis_CompressorInventory {

	protected ItemStack[] is;
	
	protected PCis_NormalCompressorInventory(EntityPlayer player, int equipment, PC_VecI size) {
		super(player, equipment, size);
		NBTTagCompound tag = compressor.getTagCompound();
		if(tag==null){
			compressor.setTagCompound(tag = new NBTTagCompound());
			PC_Utils.saveToNBT(tag, "invSize", size);
			is = new ItemStack[size.x*size.y];
		}else{
			PC_Utils.loadFromNBT(tag, "invSize", size);
			is = new ItemStack[size.x*size.y];
			PC_InventoryUtils.loadInventoryFromNBT(tag, "inv", this);
		}
	}
	
	public PCis_NormalCompressorInventory(EntityPlayer player, int equipment) {
		this(player, equipment, new PC_VecI(9, 3));
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
			ItemStack itemstack1 = splitStack(is[var1], var2);
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
	public void closeChest() {
		ItemStack backpack = player.inventory.getStackInSlot(equipped);
		if(backpack!=null)
			PC_InventoryUtils.saveInventoryToNBT(backpack.getTagCompound(), "inv", this);
	}

	public ItemStack splitStack(ItemStack is, int par1)
    {
        ItemStack var2 = new ItemStack(is.itemID, par1, is.getItemDamage());

        if (is.stackTagCompound != null)
        {
            var2.stackTagCompound = (NBTTagCompound)is.stackTagCompound.copy();
        }

        is.stackSize -= par1;
        return var2;
    }
	
}
