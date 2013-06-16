package powercraft.itemstorage;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import powercraft.api.inventory.PC_IInventoryClickHandler;
import powercraft.api.inventory.PC_InventoryUtils;
import powercraft.api.utils.PC_VecI;

public class PCis_HightCompressorInventory extends PCis_NormalCompressorInventory implements PC_IInventoryClickHandler {
	
	public PCis_HightCompressorInventory(EntityPlayer player, int equipment) {
		super(player, equipment, new PC_VecI(3, 3));
		NBTTagCompound tag = compressor.getTagCompound();
		if(tag.hasKey("size")){
			int[] size = tag.getIntArray("size");
			for(int i=0; i<is.length; i++){
				if(is[i]!=null){
					is[i].stackSize = size[i];
				}
			}
		}
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return is[var1];
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		is[var1]=var2;
	}
	
	@Override
	public ItemStack decrStackSize(int var1, int var2) {
		if(var2>64)
			var2 = 64;
		return super.decrStackSize(var1, var2);
	}

	@Override
	public int getInventoryStackLimit() {
		return 512;
	}
	
	@Override
	public int getSlotStackLimit(int slotIndex) {
		if(is[slotIndex]==null)
			return 512;
		else
			return is[slotIndex].getMaxStackSize()*8;
	}

	@Override
	public ItemStack slotClick(int slot, int mouseKey, int par3, EntityPlayer entityPlayer) {
		ItemStack var5 = null;
        InventoryPlayer var6 = entityPlayer.inventory;
        ItemStack var8;
        int var10;
        ItemStack var11;

        if (slot < 0) {
            return null;
        }
        
        var8 = is[slot];
        ItemStack var13 = var6.getItemStack();

        if (var8 != null) {
        	var8 = var8.copy();
        	if(var8.stackSize>var8.getMaxStackSize())
        		var8.stackSize=var8.getMaxStackSize();
            var5 = var8.copy();
        }

        if (var8 == null){
            if (var13 != null && canPlayerInsertStackTo(slot, var13)){
            	var10 = mouseKey == 0 ? var13.stackSize : 1;
                if (var10 > getSlotStackLimit(slot)){
                    var10 = getSlotStackLimit(slot);
                }

                is[slot] = var13.splitStack(var10);

                if (var13.stackSize == 0){
                    var6.setItemStack((ItemStack)null);
                }
            }
        }else{
            if (var13 == null){
                var10 = mouseKey == 0 ? var8.stackSize : (var8.stackSize + 1) / 2;
                var11 = decrStackSize(slot, var10);
                var6.setItemStack(var11);
                if (var8.stackSize == 0) {
                	is[slot] = null;
                }
            }else{
                if (var8.itemID == var13.itemID && var8.getItemDamage() == var13.getItemDamage() && ItemStack.areItemStackTagsEqual(var8, var13)){
                    var10 = mouseKey == 0 ? var13.stackSize : 1;
                    if (var10 > getSlotStackLimit(slot) - is[slot].stackSize){
                        var10 = getSlotStackLimit(slot) - is[slot].stackSize;
                    }
                    
                    var13.splitStack(var10);

                    if (var13.stackSize == 0)
                    {
                        var6.setItemStack((ItemStack)null);
                    }
                    is[slot].stackSize += var10;
                }
            }
        }

        return var5;
	}
	
	@Override
	public void closeChest() {
		ItemStack backpack = player.inventory.getStackInSlot(equipped);
		if(backpack!=null){
			int[] size = new int[is.length];
			for(int i=0; i<is.length; i++){
				if(is[i]!=null){
					size[i] = is[i].stackSize;
					is[i].stackSize = 1;
				}
			}
			backpack.getTagCompound().setIntArray("size", size);
			PC_InventoryUtils.saveInventoryToNBT(backpack.getTagCompound(), "inv", this);
		}
	}
	
}
