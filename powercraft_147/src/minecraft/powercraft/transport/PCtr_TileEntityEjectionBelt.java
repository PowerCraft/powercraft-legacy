package powercraft.transport;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_TileEntity;

public class PCtr_TileEntityEjectionBelt extends PC_TileEntity
{
	public static final String ACTIONTYPE = "actionType", NUMSTACKSEJECTED = "numStacksEjected", NUMITEMSEJECTED = "numItemsEjected", ITEMSELECTMODE = "itemSelectMode";
	
    public static Random rand = new Random();

    //public int actionType = 0;

    //public int numStacksEjected = 1;

    //public int numItemsEjected = 1;

   // public int itemSelectMode = 0;

	public boolean isActive = false;
    
    public PCtr_TileEntityEjectionBelt() {
    	setData(ACTIONTYPE, 0);
    	setData(NUMSTACKSEJECTED, 1);
    	setData(NUMITEMSEJECTED, 1);
    	setData(ITEMSELECTMODE, 0);
    }

    
    
    public int getActionType() {
		return (Integer)getData(ACTIONTYPE);
	}



	public void setActionType(int actionType) {
		setData(ACTIONTYPE, actionType);
	}



	public int getNumStacksEjected() {
		return (Integer)getData(NUMSTACKSEJECTED);
	}



	public void setNumStacksEjected(int numStacksEjected) {
		setData(NUMSTACKSEJECTED, numStacksEjected);
	}



	public int getNumItemsEjected() {
		return (Integer)getData(NUMITEMSEJECTED);
	}



	public void setNumItemsEjected(int numItemsEjected) {
		setData(NUMITEMSEJECTED, numItemsEjected);
	}



	public int getItemSelectMode() {
		return (Integer)getData(ITEMSELECTMODE);
	}



	public void setItemSelectMode(int itemSelectMode) {
		setData(ITEMSELECTMODE, itemSelectMode);
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
