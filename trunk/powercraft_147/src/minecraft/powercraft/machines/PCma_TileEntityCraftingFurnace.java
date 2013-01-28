package powercraft.machines;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import powercraft.management.PC_ISpecialAccessInventory;
import powercraft.management.PC_InvUtils;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.GameInfo;

public class PCma_TileEntityCraftingFurnace extends PC_TileEntity implements IInventory, PC_ISpecialAccessInventory {

	private ItemStack inv[] = new ItemStack[22];
	
	@Override
	public boolean insertStackIntoInventory(ItemStack stack) {
		if(GameInfo.isFuel(stack)){
			if(PC_InvUtils.insetItemTo(stack, this, 0, 3)==0)
				return true;
		}
		return PC_InvUtils.insetItemTo(stack, this, 3, 12)==0;
	}

	@Override
	public boolean needsSpecialInserter() {
		return true;
	}

	@Override
	public boolean canPlayerInsertStackTo(int slot, ItemStack stack) {
		if(slot<=2){
			return GameInfo.isFuel(stack);
		}
		if(slot>12){
			return false;
		}
		return true;
	}

	@Override
	public boolean canMachineInsertStackTo(int slot, ItemStack stack) {
		if(slot>12){
			return false;
		}
		return true;
	}

	@Override
	public boolean canDispenseStackFrom(int slot) {
		return true;
	}

	@Override
	public boolean canDropStackFrom(int slot) {
		return true;
	}

	@Override
	public int getSlotStackLimit(int slotIndex) {
		return 64;
	}

	@Override
	public boolean canPlayerTakeStack(int slotIndex, EntityPlayer entityPlayer) {
		return true;
	}

	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return inv[var1];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
        if (inv[i] != null)
        {
            if (inv[i].stackSize <= j)
            {
                ItemStack itemstack = inv[i];
                inv[i] = null;
                onInventoryChanged();
                return itemstack;
            }

            ItemStack itemstack1 = inv[i].splitStack(j);

            if (inv[i].stackSize == 0)
            {
            	inv[i] = null;
            }

            onInventoryChanged();
            return itemstack1;
        }
        else
        {
            return null;
        }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		return inv[var1];
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
		inv[var1] = var2;
	}

	@Override
	public String getInvName() {
		return "Crafting Furnace";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

}
