package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableMemoryInfo implements Callable
{
    final CrashReport theCrashReport;

    CallableMemoryInfo(CrashReport par1CrashReport)
    {
        this.theCrashReport = par1CrashReport;
    }

    public String getMemoryInfoAsString()
    {
        Runtime var1 = Runtime.getRuntime();
        long var2 = var1.maxMemory();
        long var4 = var1.totalMemory();
        long var6 = var1.freeMemory();
        long var8 = var2 / 1024L / 1024L;
        long var10 = var4 / 1024L / 1024L;
        long var12 = var6 / 1024L / 1024L;
        return var6 + " bytes (" + var12 + " MB) / " + var4 + " bytes (" + var10 + " MB) up to " + var2 + " bytes (" + var8 + " MB)";
    }

    public Object call()
    {
        return this.getMemoryInfoAsString();
    }
}
