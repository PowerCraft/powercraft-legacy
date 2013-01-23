package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelDimension implements Callable
{
    final WorldInfo field_85115_a;

    CallableLevelDimension(WorldInfo par1WorldInfo)
    {
        this.field_85115_a = par1WorldInfo;
    }

    public String func_85114_a()
    {
        return String.valueOf(WorldInfo.func_85122_i(this.field_85115_a));
    }

    public Object call()
    {
        return this.func_85114_a();
    }
}
