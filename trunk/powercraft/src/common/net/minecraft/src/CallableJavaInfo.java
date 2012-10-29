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

    /**
     * Returns the Java VM Information as a String.  Includes the Version and Vender.
     */
    public String getJavaInfoAsString()
    {
        return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
    }

    public Object call()
    {
        return this.getJavaInfoAsString();
    }
}
