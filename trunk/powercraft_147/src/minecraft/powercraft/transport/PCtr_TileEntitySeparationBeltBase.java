package powercraft.transport;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_Utils.Inventory;
import powercraft.management.inventory.PC_ISpecialAccessInventory;

public abstract class PCtr_TileEntitySeparationBeltBase extends
		PCtr_TileEntityRedirectionBeltBase implements IInventory, PC_ISpecialAccessInventory{

	public static final String GROUP_LOGS = "group_logs";
	public static final String GROUP_PLANKS = "group_planks";
	public static final String GROUP_ALL = "group_all";
	protected ItemStack separatorContents[];

	public boolean isGroupLogs() {
		return (Boolean)getData(GROUP_LOGS);
	}

	public void setGroupLogs(boolean state) {
		setData(GROUP_LOGS, state);
	}

	public boolean isGroupPlanks() {
		return (Boolean)getData(GROUP_PLANKS);
	}

	public void setGroupPlanks(boolean state) {
		setData(GROUP_LOGS, state);
	}

	public boolean isGroupAll() {
		return (Boolean)getData(GROUP_ALL);
	}

	public void setGroupAll(boolean state) {
		setData(GROUP_LOGS, state);
	}

	@Override
	public int getSizeInventory() {
	    return 18;
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