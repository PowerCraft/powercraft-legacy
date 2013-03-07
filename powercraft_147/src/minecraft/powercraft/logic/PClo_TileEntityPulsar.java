package powercraft.logic;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.api.PC_Utils.Communication;
import powercraft.api.PC_Utils.Converter;
import powercraft.api.PC_Utils.GameInfo;
import powercraft.api.PC_Utils.ValueWriting;
import powercraft.api.annotation.PC_ClientServerSync;
import powercraft.api.registry.PC_LangRegistry;
import powercraft.api.registry.PC_SoundRegistry;
import powercraft.api.tileentity.PC_TileEntity;

public class PClo_TileEntityPulsar extends PC_TileEntity{
	
	private int delayTimer = 0;
	@PC_ClientServerSync
    private int delay = 10;
	@PC_ClientServerSync
    private int holdtime = 4;
	@PC_ClientServerSync
    private boolean paused = false;
	@PC_ClientServerSync
    private boolean silent = false;

    private boolean should = true;

    public void setTimes(int delay, int holdTime){
        if (delay < 2){
        	delay = 2;
        }

        this.delay = delay;

        if (holdTime >= delay)
        {
        	holdTime = delay - 1;
        }
        
        holdtime = holdTime;
        notifyChanges("delay", "holdtime");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("delayTimer", delayTimer);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        delayTimer = nbttagcompound.getInteger("delayTimer");
    }

    public void printDelay()
    {
        Communication.chatMsg(PC_LangRegistry.tr("pc.pulsar.clickMsg", new String[] {getDelay() + "", Converter.ticksToSecs(getDelay()) + "" }), true);
    }

    public void printDelayTime()
    {
        Communication.chatMsg(PC_LangRegistry.tr("pc.pulsar.clickMsgTime", new String[] {getDelay() + "", Converter.ticksToSecs(getDelay()) + "", (getDelay() - delayTimer) + "" }), true);
    }

    public boolean isActive()
    {
        return GameInfo.getBID(worldObj, xCoord, yCoord, zCoord) == PClo_BlockPulsar.on.blockID;
    }

    @Override
    public void updateEntity()
    {
        if (isPaused() || worldObj.isRemote)
        {
            return;
        }
        
        if (delayTimer < 0 && !isActive())
        {
        	ValueWriting.setBlockState(worldObj, xCoord, yCoord, zCoord, true);
        }

        delayTimer++;

        if (delayTimer >= getHold() && isActive())
        {
        	ValueWriting.setBlockState(worldObj, xCoord, yCoord, zCoord, false);
        }

        if (delayTimer >= getDelay())
        {
            delayTimer = -1;
        }
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }

    public boolean isSilent()
    {
        return silent;
    }

    public int getDelay()
    {
        return delay;
    }

    public int getHold()
    {
        return holdtime;
    }

    public boolean isPaused()
    {
        return paused;
    }

    public void setPaused(boolean paused)
    {
    	if(this.paused!=paused){
    		this.paused=paused;
    		notifyChanges("paused");
    	}
    }

    public void setSilent(boolean silent) {
    	if(this.silent!=silent){
    		this.silent=silent;
    		notifyChanges("silent");
    	}
	}
    
	@Override
	protected void onCall(String key, Object value) {
		if(key.equals("change")){
			 if (worldObj.isRemote)
             {
                 if (!isSilent())
                 {
                     PC_SoundRegistry.playSound(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "random.click", 1.0F, 1.0F);
                 }
             }
		}
	}

	public boolean getShould() {
		return should;
	}
}
