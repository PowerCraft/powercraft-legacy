package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;

public class CallableClientProfiler implements Callable
{
    final Minecraft field_90046_a;

    public CallableClientProfiler(Minecraft par1Minecraft)
    {
        this.field_90046_a = par1Minecraft;
    }

    public String func_90045_a()
    {
        return this.field_90046_a.mcProfiler.profilingEnabled ? this.field_90046_a.mcProfiler.getNameOfLastSection() : "N/A (disabled)";
    }

    public Object call()
    {
        return this.func_90045_a();
    }
}
