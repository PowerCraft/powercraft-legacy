package net.minecraft.src;

public class Tuple
{
    private Object first;

    private Object second;

    public Tuple(Object par1Obj, Object par2Obj)
    {
        this.first = par1Obj;
        this.second = par2Obj;
    }

    public Object getFirst()
    {
        return this.first;
    }

    public Object getSecond()
    {
        return this.second;
    }
}
