package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableJavaInfo implements Callable
{
    /** Gets Java Info to the Crash Report. */
    final CrashReport crashReportJavaInfo;

    CallableJavaInfo(CrashReport par1CrashReport)
    {
        this.crashReportJavaInfo = par1CrashReport;
    }

    public String func_71489_a()
    {
        return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
    }

    public Object call()
    {
        return this.func_71489_a();
    }
}
