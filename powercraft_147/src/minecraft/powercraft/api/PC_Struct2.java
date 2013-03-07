package powercraft.api;

import java.io.Serializable;

import powercraft.api.PC_Utils.Coding;

public class PC_Struct2<T1, T2> implements Serializable
{
    public T1 a;

    public T2 b;

    public PC_Struct2(T1 objA, T2 objB)
    {
        a = objA;
        b = objB;
    }

    public T1 getA()
    {
        return a;
    }

    public T2 getB()
    {
        return b;
    }

    public T1 get1()
    {
        return a;
    }

    public T2 get2()
    {
        return b;
    }

    public void setA(T1 obj)
    {
        a = obj;
    }

    public void setB(T2 obj)
    {
        b = obj;
    }

    public void set1(T1 obj)
    {
        a = obj;
    }

    public void set2(T2 obj)
    {
        b = obj;
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

        PC_Struct2 <? , ? > t = (PC_Struct2 <? , ? >) obj;
        return Coding.areObjectsEqual(a, t.a) && Coding.areObjectsEqual(b, t.b);
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (a == null ? 0 : a.hashCode());
        hash += (b == null ? 0 : b.hashCode());
        return hash;
    }

    @Override
    public String toString()
    {
        return "STRUCT {" + a + "," + b + "}";
    }
}
