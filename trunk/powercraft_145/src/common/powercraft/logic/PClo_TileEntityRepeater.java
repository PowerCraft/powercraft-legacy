package powercraft.logic;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import powercraft.management.PC_PacketHandler;
import powercraft.management.PC_TileEntity;
import powercraft.management.PC_Utils.ValueWriting;

public class PClo_TileEntityRepeater extends PC_TileEntity
{
    private int type = -1;
    private int state = 0;
    private int inp = -1;

    public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        type = stack.getItemDamage();
        inp = 0;
    }

    public int getType()
    {
        return type;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int b)
    {
        PC_PacketHandler.setTileEntity(this, "state", b);
        state = b;
        ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public int getInp()
    {
        return inp;
    }

    public void change()
    {
        inp = PClo_RepeaterType.change(type, inp);
        PC_PacketHandler.setTileEntity(this, "inp", inp);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        ValueWriting.notifyBlockOfNeighborChange(worldObj, xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
        ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        type = nbtTagCompound.getInteger("type");
        state = nbtTagCompound.getInteger("state");
        inp = nbtTagCompound.getInteger("inp");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("type", type);
        nbtTagCompound.setInteger("state", state);
        nbtTagCompound.setInteger("inp", inp);
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
            else if (var.equals("state"))
            {
                state = (Integer)o[p++];
            }
            else if (var.equals("inp"))
            {
                inp = (Integer)o[p++];
            }
        }

        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        ValueWriting.notifyBlockOfNeighborChange(worldObj, xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
        ValueWriting.hugeUpdate(worldObj, xCoord, yCoord, zCoord);
    }

    @Override
    public Object[] getData()
    {
        return new Object[]
                {
                    "type", type,
                    "state", state,
                    "inp", inp
                };
    }
}
