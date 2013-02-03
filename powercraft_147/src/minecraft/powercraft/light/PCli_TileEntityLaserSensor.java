package powercraft.light;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.ValueWriting;

public class PCli_TileEntityLaserSensor extends PC_TileEntity {

	public static final String ACTIVE = "active";
	
	//private boolean active=false;
	private int coolDown = 2;
	
	public PCli_TileEntityLaserSensor(){
    	setData(ACTIVE, false);
    }
	
	public void hitByBeam() {
		coolDown = 2;
		if(!isActive()){
			setData(ACTIVE, true);
			ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
		}
	}

	public boolean isActive() {
		return (Boolean)getData(ACTIVE);
	}

	@Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void updateEntity() {
    	if(coolDown>0){
    		if(--coolDown==0){
    			setData(ACTIVE, false);
    			ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
    		}
    	}
    }
    
}
