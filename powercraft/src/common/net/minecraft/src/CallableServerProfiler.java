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

    /**
     * Gets if Server Profiler (aka Snooper) is enabled.
     */
    public String getServerProfilerEnabled()
    {
        return this.minecraftServerProfiler.theProfiler.profilingEnabled ? this.minecraftServerProfiler.theProfiler.getNameOfLastSection() : "N/A (disabled)";
    }

    public Object call()
    {
        return this.getServerProfilerEnabled();
    }
}
