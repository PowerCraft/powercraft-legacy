package powercraft.transport;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_TileEntity;
import powercraft.management.annotation.PC_ClientServerSync;

public class PCtr_TileEntityEjectionBelt extends PC_TileEntity
{
	public static Random rand = new Random();

    @PC_ClientServerSync
    public int actionType = 0;
    @PC_ClientServerSync
    public int numStacksEjected = 1;
    @PC_ClientServerSync
    public int numItemsEjected = 1;
    @PC_ClientServerSync
    public int itemSelectMode = 0;
    
	public boolean isActive = false;
    
    public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		if(this.actionType != actionType){
			this.actionType = actionType;
			notifyChanges("actionType");
		}
	}

	public int getNumStacksEjected() {
		return numStacksEjected;
	}

	public void setNumStacksEjected(int numStacksEjected) {
		if(this.numStacksEjected != numStacksEjected){
			this.numStacksEjected = numStacksEjected;
			notifyChanges("numStacksEjected");
		}
	}

	public int getNumItemsEjected() {
		return numItemsEjected;
	}

	public void setNumItemsEjected(int numItemsEjected) {
		if(this.numItemsEjected != numItemsEjected){
			this.numItemsEjected = numItemsEjected;
			notifyChanges("numItemsEjected");
		}
	}

	public int getItemSelectMode() {
		return itemSelectMode;
	}

	public void setItemSelectMode(int itemSelectMode) {
		if(this.itemSelectMode != itemSelectMode){
			this.itemSelectMode = itemSelectMode;
			notifyChanges("itemSelectMode");
		}
	}

	@Override
    public final boolean canUpdate()
    {
        return false;
    }

    @Override
    public final void updateEntity() {}

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setBoolean("isActive", isActive);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        isActive = tag.getBoolean("isActive");
    }

}
