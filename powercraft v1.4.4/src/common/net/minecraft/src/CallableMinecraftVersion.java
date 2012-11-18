package net.minecraft.src;

import java.util.concurrent.Callable;

public class CallableMinecraftVersion implements Callable
{
    final CrashReport theCrashReport;

    public CallableMinecraftVersion(CrashReport par1CrashReport)
    {
        this.theCrashReport = par1CrashReport;
    }

    public String minecraftVersion()
    {
        return "1.4.4";
    }

    public Object call()
    {
        return this.minecraftVersion();
    }
}
