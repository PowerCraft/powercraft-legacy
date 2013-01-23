package net.minecraft.crash;

import java.util.concurrent.Callable;
import net.minecraft.world.gen.layer.IntCache;

class CallableIntCache implements Callable
{
    final CrashReport field_85084_a;

    CallableIntCache(CrashReport par1CrashReport)
    {
        this.field_85084_a = par1CrashReport;
    }

    public String func_85083_a()
    {
        return IntCache.func_85144_b();
    }

    public Object call()
    {
        return this.func_85083_a();
    }
}
