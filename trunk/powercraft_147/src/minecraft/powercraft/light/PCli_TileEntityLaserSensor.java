package powercraft.light;

import powercraft.management.PC_Utils.ValueWriting;
import powercraft.management.annotation.PC_ClientServerSync;
import powercraft.management.tileentity.PC_TileEntity;

public class PCli_TileEntityLaserSensor extends PC_TileEntity {
	
	@PC_ClientServerSync
	private boolean active=false;
	private int coolDown = 2;
	
	public void hitByBeam() {
		coolDown = 2;
		if(!active){
			active = true;
			notifyChanges("active");
			ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
		}
	}

	public boolean isActive() {
		return active;
	}

	@Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void updateEntity() {
    	if(coolDown>0){
    		if(--coolDown==0){
    			active = false;
    			notifyChanges("active");
    			ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
    		}
    	}
    }
    
}
