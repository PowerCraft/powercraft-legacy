package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.server.MinecraftServer;

public class CallableServerProfiler implements Callable
{
    final MinecraftServer field_82555_a;

    public CallableServerProfiler(MinecraftServer par1MinecraftServer)
    {
        this.field_82555_a = par1MinecraftServer;
    }

    public String func_82554_a()
    {
        return this.field_82555_a.theProfiler.profilingEnabled ? this.field_82555_a.theProfiler.getNameOfLastSection() : "N/A (disabled)";
    }

    public Object call()
    {
        return this.func_82554_a();
    }
}
