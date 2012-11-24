package powercraft.core;

import java.io.Serializable;

import net.minecraft.src.IBlockAccess;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class PC_CoordI implements PC_ICoord, PC_INBT, Serializable
{
    @Override
    public String toString()
    {
        return "[" + x + ";" + y + ";" + z + "]";
    }

    public int x;

    public int y;

    public int z;

    public PC_CoordI()
    {
        x = 0;
        y = 0;
        z = 0;
    }

    public PC_CoordI(int a, int b, int c)
    {
        x = a;
        y = b;
        z = c;
    }

    public PC_CoordI(int a, int b)
    {
        x = a;
        y = b;
        z = 0;
    }

    public PC_CoordI(long a, long b, long c)
    {
        x = (int) a;
        y = (int) b;
        z = (int) c;
    }

    public PC_CoordI(long a, long b)
    {
        x = (int) a;
        y = (int) b;
        z = 0;
    }

    public PC_CoordI(PC_CoordI c)
    {
        x = c.x;
        y = c.y;
        z = c.z;
    }

    public PC_CoordI copy()
    {
        return new PC_CoordI(x, y, z);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (!this.getClass().equals(obj.getClass()))
        {
            return false;
        }

        return ((PC_CoordI) obj).x == x && ((PC_CoordI) obj).y == y && ((PC_CoordI) obj).z == z;
    }

    @Override
    public int hashCode()
    {
        return (x + 17) ^ (y - 156) ^ z;
    }

    public PC_CoordI setTo(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public PC_CoordI setTo(int x, int y)
    {
        this.x = x;
        this.y = y;
        return this;
    }

    public PC_CoordI setTo(PC_CoordI src)
    {
        this.x = src.x;
        this.y = src.y;
        this.z = src.z;
        return this;
    }

    public PC_CoordI setTo(long x, long y, long z)
    {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
        return this;
    }

    public PC_CoordI setTo(long x, long y)
    {
        this.x = (int) x;
        this.y = (int) y;
        return this;
    }

    @Override
    public PC_CoordI getInverted()
    {
        return new PC_CoordI(-x, -y, -z);
    }

    public boolean equalsXZ(PC_CoordI compared)
    {
        return x == compared.x && z == compared.z;
    }

    public PC_CoordI add(PC_CoordI added)
    {
        x += added.x;
        y += added.y;
        z += added.z;
        return this;
    }

    public PC_CoordI add(int x, int y, int z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public PC_CoordI add(int x, int y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    public PC_CoordI add(long x, long y, long z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public PC_CoordI add(long x, long y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    @Override
    public PC_CoordI offset(PC_CoordI added)
    {
        return new PC_CoordI(x + added.x, y + added.y, z + added.z);
    }

    @Override
    public PC_CoordF offset(PC_CoordF added)
    {
        return new PC_CoordF(x + added.x, y + added.y, z + added.z);
    }

    @Override
    public PC_CoordD offset(PC_CoordD added)
    {
        return new PC_CoordD(x + added.x, y + added.y, z + added.z);
    }

    @Override
    public PC_CoordI offset(int xm, int ym, int zm)
    {
        return new PC_CoordI(x + xm, y + ym, z + zm);
    }

    @Override
    public PC_CoordF offset(float xm, float ym, float zm)
    {
        return new PC_CoordF(x + xm, y + ym, z + zm);
    }

    @Override
    public PC_CoordD offset(double xm, double ym, double zm)
    {
        return new PC_CoordD(x + xm, y + ym, z + zm);
    }

    @Override
    public PC_CoordI offset(long xm, long ym, long zm)
    {
        return new PC_CoordI(x + xm, y + ym, z + zm);
    }

    public PC_CoordI offset(int xm, int ym)
    {
        return new PC_CoordI(x + xm, y + ym, z);
    }

    public PC_CoordF offset(float xm, float ym)
    {
        return new PC_CoordF(x + xm, y + ym, z);
    }

    public PC_CoordD offset(double xm, double ym)
    {
        return new PC_CoordD(x + xm, y + ym, z);
    }

    public PC_CoordI offset(long xm, long ym)
    {
        return new PC_CoordI(x + xm, y + ym, z);
    }

    public PC_CoordI setX(int xx)
    {
        return new PC_CoordI(xx, y, z);
    }

    public PC_CoordI setY(int yy)
    {
        return new PC_CoordI(x, yy, z);
    }

    public PC_CoordI setZ(int zz)
    {
        return new PC_CoordI(x, y, zz);
    }

    public int getId(IBlockAccess iba)
    {
        return iba.getBlockId(x, y, z);
    }

    public int getMeta(IBlockAccess iba)
    {
        return iba.getBlockMetadata(x, y, z);
    }

    public boolean setBlock(World w, int id, int meta)
    {
        return w.setBlockAndMetadataWithNotify(x, y, z, id, meta);
    }

    public boolean setBlockNoNotify(World w, int id, int meta)
    {
        return w.setBlockAndMetadata(x, y, z, id, meta);
    }

    public void notifyNigbours(World w)
    {
        w.notifyBlocksOfNeighborChange(x, y, z, w.getBlockId(x, y, z));
    }

    public void setMeta(World w, int meta)
    {
        w.setBlockMetadataWithNotify(x, y, z, meta);
    }

    public void setMetaNoNotify(World w, int meta)
    {
        w.setBlockMetadata(x, y, z, meta);
    }

    public void setId(World w, int id)
    {
        w.setBlockWithNotify(x, y, z, id);
    }

    public TileEntity getTileEntity(IBlockAccess world)
    {
        return world.getBlockTileEntity(x, y, z);
    }

    public boolean isPoweredDirectly(World world)
    {
        return world.isBlockGettingPowered(x, y, z);
    }

    public boolean isPoweredIndirectly(World world)
    {
        return world.isBlockIndirectlyGettingPowered(x, y, z);
    }

    @Override
    public PC_CoordI round()
    {
        return copy();
    }

    @Override
    public PC_CoordI floor()
    {
        return copy();
    }

    @Override
    public PC_CoordI ceil()
    {
        return copy();
    }

    public double distanceTo(PC_CoordI pos)
    {
        return Math.sqrt((x - pos.x) * (x - pos.x) + (y - pos.y) * (y - pos.y) + (z - pos.z) * (z - pos.z));
    }

    public PC_CoordD getVectorTo(PC_CoordI pos)
    {
        return new PC_CoordD(pos.x - x, pos.y - y, pos.z - z);
    }

    public static PC_CoordD getVector(PC_CoordI pos1, PC_CoordI pos2)
    {
        return new PC_CoordD(pos2.x - pos1.x, pos2.y - pos1.y, pos2.z - pos1.z);
    }

    public double distanceHorizontalTo(PC_CoordI pos)
    {
        return Math.sqrt((x - pos.x) * (x - pos.x) + (z - pos.z) * (z - pos.z));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger("ix", x);
        tag.setInteger("iy", y);
        tag.setInteger("iz", z);
        return tag;
    }

    @Override
    public PC_CoordI readFromNBT(NBTTagCompound tag)
    {
        x = tag.getInteger("ix");
        y = tag.getInteger("iy");
        z = tag.getInteger("iz");
        return this;
    }

    public PC_CoordI multiply(int scale)
    {
        return new PC_CoordI(x * scale, y * scale, z * scale);
    }
}
