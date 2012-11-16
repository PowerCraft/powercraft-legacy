package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelSeed implements Callable
{
    final WorldInfo field_85143_a;

    CallableLevelSeed(WorldInfo par1WorldInfo)
    {
        this.field_85143_a = par1WorldInfo;
    }

    public String func_85142_a()
    {
        return String.valueOf(this.field_85143_a.getSeed());
    }

    public Object call()
    {
        return this.func_85142_a();
    }
}
