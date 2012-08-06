package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLvl2 implements Callable
{
    final World field_77405_a;

    CallableLvl2(World par1World)
    {
        this.field_77405_a = par1World;
    }

    public String func_77404_a()
    {
        return this.field_77405_a.playerEntities.size() + " total; " + this.field_77405_a.playerEntities.toString();
    }

    public Object call()
    {
        return this.func_77404_a();
    }
}
