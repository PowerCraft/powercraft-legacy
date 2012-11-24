package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableJavaInfo implements Callable
{
    final CrashReport theCrashReport;

    CallableJavaInfo(CrashReport par1CrashReport)
    {
        this.theCrashReport = par1CrashReport;
    }

    public String getJavaInfoAsString()
    {
        return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
    }

    public Object call()
    {
        return this.getJavaInfoAsString();
    }
}
