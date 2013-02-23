package powercraft.transport;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_Utils.Inventory;
import powercraft.management.annotation.PC_ClientServerSync;
import powercraft.management.inventory.PC_ISpecialAccessInventory;

public abstract class PCtr_TileEntitySeparationBeltBase extends
		PCtr_TileEntityRedirectionBeltBase implements IInventory, PC_ISpecialAccessInventory{

	protected ItemStack separatorContents[];

	@PC_ClientServerSync
	private boolean group_logs;
	@PC_ClientServerSync
	private boolean group_planks;
	@PC_ClientServerSync
	private boolean group_all;
	
	public boolean isGroupLogs() {
		return group_logs;
	}

	public void setGroupLogs(boolean state) {
		if(group_logs != state){
			group_logs = state;
			notifyChanges("group_logs");
		}
	}

	public boolean isGroupPlanks() {
		return group_planks;
	}

	public void setGroupPlanks(boolean state) {
		if(group_planks != state){
			group_planks = state;
			notifyChanges("group_planks");
		}
	}

	public boolean isGroupAll() {
		return group_all;
	}

	public void setGroupAll(boolean state) {
		if(group_all != state){
			group_all = state;
			notifyChanges("group_all");
		}
	}

	@Override
	public int getSizeInventory() {
	    return separatorContents.length;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public ItemStack getStackInSlot(int i) {
	    return separatorContents[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
	    if (separatorContents[i] != null)
	    {
	        if (separatorContents[i].stackSize <= j)
	        {
	            ItemStack itemstack = separatorContents[i];
	            separatorContents[i] = null;
	            onInventoryChanged();
	            return itemstack;
	        }
	
	        ItemStack itemstack1 = separatorContents[i].splitStack(j);
	
	        if (separatorContents[i].stackSize == 0)
	        {
	            separatorContents[i] = null;
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
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        separatorContents[i] = itemstack;

        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }

        onInventoryChanged();
    }

    @Override
    public String getInvName()
    {
        return "Item Separator";
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        Inventory.loadInventoryFromNBT(nbttagcompound, "Items", this);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        Inventory.saveInventoryToNBT(nbttagcompound, "Items", this);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (separatorContents[par1] != null)
        {
            ItemStack itemstack = separatorContents[par1];
            separatorContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean insertStackIntoInventory(ItemStack stack)
    {
        return false;
    }

    @Override
    public boolean needsSpecialInserter()
    {
        return true;
    }

    @Override
    public boolean canPlayerInsertStackTo(int slot, ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean canMachineInsertStackTo(int slot, ItemStack stack)
    {
        return false;
    }

    @Override
    public boolean canDispenseStackFrom(int slot)
    {
        return false;
    }

	@Override
	public boolean canDropStackFrom(int slot) {
		return true;
	}

	@Override
	public int getSlotStackLimit(int slotIndex) {
		return getInventoryStackLimit();
	}

	@Override
	public boolean canPlayerTakeStack(int slotIndex, EntityPlayer entityPlayer) {
		return true;
	}

}