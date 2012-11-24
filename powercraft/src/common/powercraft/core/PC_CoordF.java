package powercraft.core;

import net.minecraft.src.NBTTagCompound;

public class PC_CoordF implements PC_ICoord, PC_INBT
{
    @Override
    public String toString()
    {
        return "[" + x + ";" + y + ";" + z + "]";
    }

    public float x;

    public float y;

    public float z;

    public PC_CoordF()
    {
        x = 0;
        y = 0;
        z = 0;
    }

    public PC_CoordF(float a, float b, float c)
    {
        x = a;
        y = b;
        z = c;
    }

    public PC_CoordF(int a, int b, int c)
    {
        x = a;
        y = b;
        z = c;
    }

    public PC_CoordF(long a, long b, long c)
    {
        x = a;
        y = b;
        z = c;
    }

    public PC_CoordF(double a, double b, double c)
    {
        x = (float) a;
        y = (float) b;
        z = (float) c;
    }

    public PC_CoordF(PC_CoordF c)
    {
        x = c.x;
        y = c.y;
        z = c.z;
    }

    public PC_CoordF(PC_CoordI c)
    {
        x = c.x;
        y = c.y;
        z = c.z;
    }

    public PC_CoordF(PC_CoordD c)
    {
        x = (float) c.x;
        y = (float) c.y;
        z = (float) c.z;
    }

    public PC_CoordF copy()
    {
        return new PC_CoordF(x, y, z);
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

        return ((PC_CoordF) obj).x == x && ((PC_CoordF) obj).y == y && ((PC_CoordF) obj).z == z;
    }

    @Override
    public int hashCode()
    {
        return new Float(x + 17).hashCode() ^ new Float((y - 156)).hashCode() ^ new Float(z).hashCode();
    }

    public PC_CoordF setTo(double x, double y, double z)
    {
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
        return this;
    }

    public PC_CoordF setTo(PC_CoordD src)
    {
        this.x = (float) src.x;
        this.y = (float) src.y;
        this.z = (float) src.z;
        return this;
    }

    public PC_CoordF setTo(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public PC_CoordF setTo(PC_CoordF src)
    {
        this.x = src.x;
        this.y = src.y;
        this.z = src.z;
        return this;
    }

    public PC_CoordF setTo(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public PC_CoordF setTo(PC_CoordI src)
    {
        this.x = src.x;
        this.y = src.y;
        this.z = src.z;
        return this;
    }

    public PC_CoordF setTo(long x, long y, long z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    @Override
    public PC_CoordF getInverted()
    {
        return new PC_CoordF(-x, -y, -z);
    }

    public boolean equalsXZ(PC_CoordF matched)
    {
        return x == matched.x && z == matched.z;
    }

    public PC_CoordF add(PC_CoordF added)
    {
        x += added.x;
        y += added.y;
        z += added.z;
        return this;
    }

    public PC_CoordF add(int x, int y, int z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public PC_CoordF add(int x, int y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    public PC_CoordF add(float x, float y, float z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public PC_CoordF add(float x, float y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    public PC_CoordF add(double x, double y, double z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public PC_CoordF add(double x, double y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    public PC_CoordF add(long x, long y, long z)
    {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public PC_CoordF add(long x, long y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    @Override
    public PC_CoordF offset(PC_CoordI added)
    {
        return new PC_CoordF(x + added.x, y + added.y, z + added.z);
    }

    @Override
    public PC_CoordF offset(PC_CoordF added)
    {
        return new PC_CoordF(x + added.x, y + added.y, z + added.z);
    }

    @Override
    public PC_CoordF offset(PC_CoordD added)
    {
        return new PC_CoordF(x + added.x, y + added.y, z + added.z);
    }

    @Override
    public PC_CoordF offset(int xm, int ym, int zm)
    {
        return new PC_CoordF(x + xm, y + ym, z + zm);
    }

    @Override
    public PC_CoordF offset(float xm, float ym, float zm)
    {
        return new PC_CoordF(x + xm, y + ym, z + zm);
    }

    @Override
    public PC_CoordF offset(double xm, double ym, double zm)
    {
        return new PC_CoordF(x + xm, y + ym, z + zm);
    }

    @Override
    public PC_CoordF offset(long xm, long ym, long zm)
    {
        return new PC_CoordF(x + xm, y + ym, z + zm);
    }

    public PC_CoordF setX(float xx)
    {
        return new PC_CoordF(xx, y, z);
    }

    public PC_CoordF setY(float yy)
    {
        return new PC_CoordF(x, yy, z);
    }

    public PC_CoordF setZ(float zz)
    {
        return new PC_CoordF(x, y, zz);
    }

    public double distanceTo(PC_CoordF pos)
    {
        return Math.sqrt((x - pos.x) * (x - pos.x) + (y - pos.y) * (y - pos.y) + (z - pos.z) * (z - pos.z));
    }

    public PC_CoordD getVectorTo(PC_CoordF pos)
    {
        return new PC_CoordD(pos.x - x, pos.y - y, pos.z - z);
    }

    public static PC_CoordD getVector(PC_CoordF pos1, PC_CoordF pos2)
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
        tag.setFloat("fx", x);
        tag.setFloat("fy", y);
        tag.setFloat("fz", z);
        return tag;
    }

    @Override
    public PC_INBT readFromNBT(NBTTagCompound tag)
    {
        x = tag.getFloat("fx");
        y = tag.getFloat("fy");
        z = tag.getFloat("fz");
        return this;
    }
}
