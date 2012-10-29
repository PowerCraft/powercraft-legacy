package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class CallableClientProfiler implements Callable
{
    /** Gets skin of Minecraft player. */
    final Minecraft minecraftClientProfiler;

    public CallableClientProfiler(Minecraft par1Minecraft)
    {
        this.minecraftClientProfiler = par1Minecraft;
    }

    /**
     * Gets if Client Profiler (aka Snooper) is enabled.
     */
    public String getClientProfilerEnabled()
    {
        return this.minecraftClientProfiler.mcProfiler.profilingEnabled ? this.minecraftClientProfiler.mcProfiler.getNameOfLastSection() : "N/A (disabled)";
    }

    public Object call()
    {
        return this.getClientProfilerEnabled();
    }
}
