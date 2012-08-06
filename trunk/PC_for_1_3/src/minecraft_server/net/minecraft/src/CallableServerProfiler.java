package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.server.MinecraftServer;

public class CallableServerProfiler implements Callable
{
    final MinecraftServer field_74272_a;

    public CallableServerProfiler(MinecraftServer par1MinecraftServer)
    {
        this.field_74272_a = par1MinecraftServer;
    }

    public String func_74271_a()
    {
        return this.field_74272_a.field_71304_b.profilingEnabled ? this.field_74272_a.field_71304_b.func_76322_c() : "N/A (disabled)";
    }

    public Object call()
    {
        return this.func_74271_a();
    }
}
