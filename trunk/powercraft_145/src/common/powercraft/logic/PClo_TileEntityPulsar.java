package powercraft.logic;

import net.minecraft.src.NBTTagCompound;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils;
import powercraft.management.PC_Utils.Converter;
import powercraft.management.PC_Utils.GameInfo;
import powercraft.management.PC_Utils.Lang;

public class PClo_TileEntityPulsar extends PC_TileEntity
{
    private int delayTimer = 0;

    private int delay = 10;

    private int holdtime = 4;

    private boolean paused = false;

    private boolean silent = false;

    public PClo_TileEntityPulsar()
    {
    }

    public void setTimes(int delay, int holdTime)
    {
        this.delay = delay;

        if (this.delay < 2)
        {
            this.delay = 2;
        }

        this.holdtime = holdTime;

        if (this.holdtime >= this.delay)
        {
            this.holdtime = this.delay - 1;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("delayTimer", delayTimer);
        nbttagcompound.setInteger("delay", delay);
        nbttagcompound.setInteger("holdtime", holdtime);
        nbttagcompound.setBoolean("paused", paused);
        nbttagcompound.setBoolean("silent", silent);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        delayTimer = nbttagcompound.getInteger("delayTimer");
        delay = nbttagcompound.getInteger("delay");
        holdtime = nbttagcompound.getInteger("holdtime");
        paused = nbttagcompound.getBoolean("paused");
        silent = nbttagcompound.getBoolean("silent");

        if (delay < 2)
        {
            delay = 2;
        }

        if (holdtime < 1 || holdtime >= delay - 1)
        {
            holdtime = (delay > 6 ? 3 : 1);
        }
    }

    public void printDelay()
    {
        PC_Utils.chatMsg(Lang.tr("pc.pulsar.clickMsg", new String[] { delay + "", Converter.ticksToSecs(delay) + "" }), true);
    }

    public void printDelayTime()
    {
        PC_Utils.chatMsg(Lang.tr("pc.pulsar.clickMsgTime", new String[] { delay + "", Converter.ticksToSecs(delay) + "", (delay - delayTimer) + "" }), true);
    }

    public boolean isActive()
    {
        return GameInfo.getBID(worldObj, xCoord, yCoord, zCoord) == PClo_BlockPulsar.on.blockID;
    }

    @Override
    public void updateEntity()
    {
        if (paused || worldObj.isRemote)
        {
            return;
        }

        boolean change = false;

        if (delayTimer < 0 && !isActive())
        {
            PC_Utils.setBlockState(worldObj, xCoord, yCoord, zCoord, true);
            updateBlock();
            change = true;
        }

        delayTimer++;

        if (delayTimer >= holdtime && isActive())
        {
            PC_Utils.setBlockState(worldObj, xCoord, yCoord, zCoord, false);
            updateBlock();
            change = true;
        }

        if (delayTimer >= delay)
        {
            delayTimer = -1;
        }
    }

    public void updateBlock()
    {
        if (worldObj != null)
        {
            worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, PClo_App.pulsar.blockID, 1);
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
        PC_PacketHandler.setTileEntity(this, "paused", paused);
        this.paused = paused;
    }

    @Override
    public void setData(Object[] o)
    {
        int p = 0;

        while (p < o.length)
        {
            String var = (String)o[p++];

            if (var.equals("hold"))
            {
                holdtime = (Integer)o[p++];
            }
            else if (var.equals("delay"))
            {
                delay = (Integer)o[p++];
            }
            else if (var.equals("silent"))
            {
                silent = (Boolean)o[p++];
            }
            else if (var.equals("paused"))
            {
                paused = (Boolean)o[p++];
            }
            else if (var.equals("change"))
            {
                if (worldObj.isRemote)
                {
                    if (!silent)
                    {
                        PC_Utils.playSound(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "random.click", 1.0F, 1.0F);
                    }
                }
            }
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public Object[] getData()
    {
        return new Object[]
                {
                    "hold", holdtime,
                    "delay", delay,
                    "silent", silent,
                    "paused", paused
                };
    }
}
