package net.minecraft.src;

public class NextTickListEntry implements Comparable
{
    private static long nextTickEntryID = 0L;

    public int xCoord;

    public int yCoord;

    public int zCoord;

    public int blockID;

    public long scheduledTime;
    public int field_82754_f;

    private long tickEntryID;

    public NextTickListEntry(int par1, int par2, int par3, int par4)
    {
        this.tickEntryID = (long)(nextTickEntryID++);
        this.xCoord = par1;
        this.yCoord = par2;
        this.zCoord = par3;
        this.blockID = par4;
    }

    public boolean equals(Object par1Obj)
    {
        if (!(par1Obj instanceof NextTickListEntry))
        {
            return false;
        }
        else
        {
            NextTickListEntry var2 = (NextTickListEntry)par1Obj;
            return this.xCoord == var2.xCoord && this.yCoord == var2.yCoord && this.zCoord == var2.zCoord && this.blockID == var2.blockID;
        }
    }

    public int hashCode()
    {
        return (this.xCoord * 1024 * 1024 + this.zCoord * 1024 + this.yCoord) * 256 + this.blockID;
    }

    public NextTickListEntry setScheduledTime(long par1)
    {
        this.scheduledTime = par1;
        return this;
    }

    public void func_82753_a(int par1)
    {
        this.field_82754_f = par1;
    }

    public int comparer(NextTickListEntry par1NextTickListEntry)
    {
        return this.scheduledTime < par1NextTickListEntry.scheduledTime ? -1 : (this.scheduledTime > par1NextTickListEntry.scheduledTime ? 1 : (this.field_82754_f != par1NextTickListEntry.field_82754_f ? this.field_82754_f - par1NextTickListEntry.field_82754_f : (this.tickEntryID < par1NextTickListEntry.tickEntryID ? -1 : (this.tickEntryID > par1NextTickListEntry.tickEntryID ? 1 : 0))));
    }

    public int compareTo(Object par1Obj)
    {
        return this.comparer((NextTickListEntry)par1Obj);
    }
}
