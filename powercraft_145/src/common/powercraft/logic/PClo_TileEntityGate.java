package powercraft.logic;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import powercraft.core.PC_PacketHandler;
import powercraft.core.PC_TileEntity;
import powercraft.core.PC_Utils;

public class PClo_TileEntityGate extends PC_TileEntity
{
    private int type = -1;
    private int inp = -1;

    public void create(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        type = stack.getItemDamage();
        inp = PClo_GateType.ROT_L_D_R;
    }

    public int getType()
    {
        return type;
    }

    public int getInp()
    {
        return inp;
    }

    public void rotInp()
    {
        inp = PClo_GateType.rotateCornerSides(type, inp);
        PC_PacketHandler.setTileEntity(this, "inp", inp);
        worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
        PC_Utils.notifyBlockOfNeighborChange(worldObj, xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        type = nbtTagCompound.getInteger("type");
        inp = nbtTagCompound.getInteger("inp");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("type", type);
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
            else if (var.equals("inp"))
            {
                inp = (Integer)o[p++];
            }
        }

        PC_Utils.hugeUpdate(worldObj, xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
        worldObj.markBlockNeedsUpdate(xCoord, yCoord, zCoord);
        PC_Utils.notifyBlockOfNeighborChange(worldObj, xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord));
    }

    @Override
    public Object[] getData()
    {
        return new Object[]
                {
                    "type", type,
                    "inp", inp
                };
    }
}
