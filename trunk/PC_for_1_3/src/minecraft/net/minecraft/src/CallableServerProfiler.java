package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.server.MinecraftServer;

public class CallableServerProfiler implements Callable
{
    /** Gets Minecraft Server profile. */
    final MinecraftServer minecraftServerProfiler;

    public CallableServerProfiler(MinecraftServer par1MinecraftServer)
    {
        this.minecraftServerProfiler = par1MinecraftServer;
    }

    public String func_74271_a()
    {
        return this.minecraftServerProfiler.theProfiler.profilingEnabled ? this.minecraftServerProfiler.theProfiler.func_76322_c() : "N/A (disabled)";
    }

    public Object call()
    {
        return this.func_74271_a();
    }
}
