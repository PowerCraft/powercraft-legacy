package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLvl1 implements Callable
{
    final World field_77485_a;

    CallableLvl1(World par1World)
    {
        this.field_77485_a = par1World;
    }

    public String func_77484_a()
    {
        return this.field_77485_a.loadedEntityList.size() + " total; " + this.field_77485_a.loadedEntityList.toString();
    }

    public Object call()
    {
        return this.func_77484_a();
    }
}
