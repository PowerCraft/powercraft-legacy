package powercraft.logic;

import net.minecraft.nbt.NBTTagCompound;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_Struct2;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.Communication;
import powercraft.management.PC_Utils.Converter;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Lang;
import powercraft.management.PC_Utils.ValueWriting;

public class PClo_TileEntityPulsar extends PC_TileEntity{
	
	public static final String DELAY = "delay", HOLDTIME = "holdtime", PAUSED = "paused", SILENT = "silent";
	
    private int delayTimer = 0;

    //private int delay = 10;

    //private int holdtime = 4;

    //private boolean paused = false;

    //private boolean silent = false;

    private boolean should = true;
    
    public PClo_TileEntityPulsar(){
		setData(DELAY, 10);
    	setData(HOLDTIME, 4);
    	setData(PAUSED, false);
    	setData(SILENT, false);
    }

    public void setTimes(int delay, int holdTime){
        if (delay < 2){
        	delay = 2;
        }

        setData(DELAY, delay);

        if (holdTime >= delay)
        {
        	holdTime = delay - 1;
        }
        
        setData(HOLDTIME, holdTime);
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
        Communication.chatMsg(Lang.tr("pc.pulsar.clickMsg", new String[] {getDelay() + "", Converter.ticksToSecs(getDelay()) + "" }), true);
    }

    public void printDelayTime()
    {
        Communication.chatMsg(Lang.tr("pc.pulsar.clickMsgTime", new String[] {getDelay() + "", Converter.ticksToSecs(getDelay()) + "", (getDelay() - delayTimer) + "" }), true);
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
        return (Boolean)getData(SILENT);
    }

    public int getDelay()
    {
        return (Integer)getData(DELAY);
    }

    public int getHold()
    {
        return (Integer)getData(HOLDTIME);
    }

    public boolean isPaused()
    {
        return (Boolean)getData(PAUSED);
    }

    public void setPaused(boolean paused)
    {
    	setData(PAUSED, paused);
    }

    public void setSilent(boolean silent) {
		setData(SILENT, silent);
	}
    
	@Override
	protected void onCall(String key, Object value) {
		if(key.equals("change")){
			 if (worldObj.isRemote)
             {
                 if (!isSilent())
                 {
                     ValueWriting.playSound(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "random.click", 1.0F, 1.0F);
                 }
             }
		}
	}

	public boolean getShould() {
		return should;
	}
}
