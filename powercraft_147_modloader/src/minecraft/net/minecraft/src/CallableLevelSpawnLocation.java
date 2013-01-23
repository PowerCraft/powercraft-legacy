package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelSpawnLocation implements Callable
{
    final WorldInfo field_85135_a;

    CallableLevelSpawnLocation(WorldInfo par1WorldInfo)
    {
        this.field_85135_a = par1WorldInfo;
    }

    public String func_85134_a()
    {
        return CrashReportCategory.func_85071_a(WorldInfo.func_85125_d(this.field_85135_a), WorldInfo.func_85124_e(this.field_85135_a), WorldInfo.func_85123_f(this.field_85135_a));
    }

    public Object call()
    {
        return this.func_85134_a();
    }
}
