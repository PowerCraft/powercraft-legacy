package powercraft.logic;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import powercraft.core.PC_PacketHandler;
import powercraft.core.PC_TileEntity;
import powercraft.core.PC_Utils;

public class PClo_TileEntityDelayer extends PC_TileEntity
{
    private int type = -1;
    private boolean stateBuffer[] = new boolean[20];

    public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        type = stack.getItemDamage();
    }

    public int getType()
    {
        return type;
    }

    public boolean[] getStateBuffer()
    {
        return stateBuffer;
    }

    public void updateStateBuffer()
    {
        //PC_PacketHandler.setTileEntity(this, "stateBuffer", stateBuffer);
    }

    public int getDelay()
    {
        return stateBuffer.length;
    }

    public void setDelay(int delay)
    {
        stateBuffer = new boolean[delay];
        //PC_PacketHandler.setTileEntity(this, "stateBuffer", stateBuffer);
    }

    @Override
    public void updateEntity()
    {
        int rot = PClo_BlockDelayer.getRotation_static(PC_Utils.getMD(worldObj, xCoord, yCoord, zCoord));
        boolean stop = false;
        boolean reset = false;

        if (type == PClo_DelayerType.FIFO)
        {
            stop = PC_Utils.poweredFromInput(worldObj, xCoord, yCoord, zCoord, PC_Utils.RIGHT, rot);
            reset = PC_Utils.poweredFromInput(worldObj, xCoord, yCoord, zCoord, PC_Utils.LEFT, rot);
        }

        if (!stop || reset)
        {
            worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, PC_Utils.getBID(worldObj, xCoord, yCoord, zCoord), mod_PowerCraftLogic.delayer.tickRate());
        }
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        type = nbtTagCompound.getInteger("type");
        int length = nbtTagCompound.getInteger("delay");
        stateBuffer = new boolean[length];

        for (int i = 0; i < length; i++)
        {
            stateBuffer[i] = nbtTagCompound.getBoolean("stateBuffer[" + i + "]");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("type", type);
        nbtTagCompound.setInteger("delay", stateBuffer.length);

        for (int i = 0; i < stateBuffer.length; i++)
        {
            nbtTagCompound.setBoolean("stateBuffer[" + i + "]", stateBuffer[i]);
        }
    }

    @Override
    public void setData(Object[] o)
    {
        int p = 0;

        while (p < o.length)
        {
            String var = (String)o[p++];

            if (var.equals("type"))
            {
                if (type == -1)
                {
                    type = (Integer)o[p++];
                }
                else
                {
                    p++;
                }
            }
            else if (var.equals("stateBuffer"))
            {
                stateBuffer = (boolean[])o[p++];
            }
        }

        PC_Utils.hugeUpdate(worldObj, xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
        worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public Object[] getData()
    {
        return new Object[]
                {
                    "type", type,
                    "stateBuffer", stateBuffer
                };
    }
}
