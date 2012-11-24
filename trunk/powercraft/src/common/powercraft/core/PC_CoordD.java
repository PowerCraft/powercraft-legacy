package powercraft.core;

import net.minecraft.src.NBTTagCompound;

public class PC_CoordD implements PC_ICoord, PC_INBT
{
    @Override
    public String toString()
    {
        return "[" + x + ";" + y + ";" + z + "]";
    }

    public double x;

    public double y;

    public double z;

    public PC_CoordD()
    {
        x = 0;
        y = 0;
        z = 0;
    }

    public PC_CoordD(double a, double b, double c)
    {
        x = a;
        y = b;
        z = c;
    }

    public PC_CoordD(float a, float b, float c)
    {
        x = a;
        y = b;
        z = c;
    }

    public PC_CoordD(int a, int b, int c)
    {
        x = a;
        y = b;
        z = c;
    }

    public PC_CoordD(long a, long b, long c)
    {
        x = a;
        y = b;
        z = c;
    }

    public PC_CoordD(PC_CoordD c)
    {
        x = c.x;
        y = c.y;
        z = c.z;
    }

    public PC_CoordD(PC_CoordF c)
    {
        x = c.x;
        y = c.y;
        z = c.z;
    }

    public PC_CoordD(PC_CoordI c)
    {
        x = c.x;
        y = c.y;
        z = c.z;
    }

    public PC_CoordD copy()
    {
        return new PC_CoordD(x, y, z);
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

        return ((PC_CoordD) obj).x == x && ((PC_CoordD) obj).y == y && ((PC_CoordD) obj).z == z;
    }

    @Override
    public int hashCode()
    {
        return new Double(x + 17).hashCode() ^ new Double((y - 156)).hashCode() ^ new Double(z).hashCode();
    }

    public PC_CoordD setTo(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public PC_CoordD setTo(PC_CoordD src)
    {
        this.x = src.x;
        this.y = src.y;
        this.z = src.z;
        return this;
    }

    public PC_CoordD setTo(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public PC_CoordD setTo(PC_CoordF src)
    {
        this.x = src.x;
        this.y = src.y;
        this.z = src.z;
        return this;
    }

    public PC_CoordD setTo(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public PC_CoordD setTo(PC_CoordI src)
    {
        this.x = src.x;
        this.y = src.y;
        this.z = src.z;
        return this;
    }

    public PC_CoordD setTo(long x, long y, long z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    @Override
    public PC_CoordD getInverted()
    {
        return new PC_CoordD(-x, -y, -z);
    }

    public boolean equalsXZ(PC_CoordD matched)
    {
        return x == matched.x && z == matched.z;
    }

    public PC_CoordD add(PC_CoordD added)
    {
        x += added.x;
        y += added.y;
        z += added.z;
        return this;
    }

    public PC_CoordD add(int x, int y, int z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public PC_CoordD add(int x, int y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    public PC_CoordD add(double x, double y, double z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public PC_CoordD add(double x, double y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    public PC_CoordD add(float x, float y, float z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public PC_CoordD add(float x, float y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    public PC_CoordD add(long x, long y, long z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public PC_CoordD add(long x, long y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    @Override
    public PC_CoordD offset(PC_CoordI added)
    {
        return new PC_CoordD(x + added.x, y + added.y, z + added.z);
    }

    @Override
    public PC_CoordD offset(PC_CoordF added)
    {
        return new PC_CoordD(x + added.x, y + added.y, z + added.z);
    }

    @Override
    public PC_CoordD offset(PC_CoordD added)
    {
        return new PC_CoordD(x + added.x, y + added.y, z + added.z);
    }

    @Override
    public PC_CoordD offset(int xm, int ym, int zm)
    {
        return new PC_CoordD(x + xm, y + ym, z + zm);
    }

    @Override
    public PC_CoordD offset(float xm, float ym, float zm)
    {
        return new PC_CoordD(x + xm, y + ym, z + zm);
    }

    @Override
    public PC_CoordD offset(double xm, double ym, double zm)
    {
        return new PC_CoordD(x + xm, y + ym, z + zm);
    }

    @Override
    public PC_CoordD offset(long xm, long ym, long zm)
    {
        return new PC_CoordD(x + xm, y + ym, z + zm);
    }

    public PC_CoordD setX(double xx)
    {
        return new PC_CoordD(xx, y, z);
    }

    public PC_CoordD setY(double yy)
    {
        return new PC_CoordD(x, yy, z);
    }

    public PC_CoordD setZ(double zz)
    {
        return new PC_CoordD(x, y, zz);
    }

    public double distanceTo(PC_CoordD pos)
    {
        return Math.sqrt((x - pos.x) * (x - pos.x) + (y - pos.y) * (y - pos.y) + (z - pos.z) * (z - pos.z));
    }

    public PC_CoordD getVectorTo(PC_CoordD pos)
    {
        return new PC_CoordD(pos.x - x, pos.y - y, pos.z - z);
    }

    public static PC_CoordD getVector(PC_CoordD pos1, PC_CoordD pos2)
    {
        return new PC_CoordD(pos2.x - pos1.x, pos2.y - pos1.y, pos2.z - pos1.z);
    }

    @Override
    public PC_CoordI round()
    {
        return new PC_CoordI(Math.round(x), Math.round(y), Math.round(z));
    }

    @Override
    public PC_CoordI floor()
    {
        return new PC_CoordI(Math.round(Math.floor(x)), Math.round(Math.floor(y)), Math.round(Math.floor(z)));
    }

    @Override
    public PC_CoordI ceil()
    {
        return new PC_CoordI(Math.round(Math.ceil(x)), Math.round(Math.ceil(y)), Math.round(Math.ceil(z)));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setDouble("dx", x);
        tag.setDouble("dy", y);
        tag.setDouble("dz", z);
        return tag;
    }

    @Override
    public PC_INBT readFromNBT(NBTTagCompound tag)
    {
        x = tag.getDouble("dx");
        y = tag.getDouble("dy");
        z = tag.getDouble("dz");
        return this;
    }
}
