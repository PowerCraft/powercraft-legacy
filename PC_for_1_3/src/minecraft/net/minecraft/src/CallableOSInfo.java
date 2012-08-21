package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableOSInfo implements Callable
{
    /** Gets OS Info for Crash Report. */
    final CrashReport crashReportOSInfo;

    CallableOSInfo(CrashReport par1CrashReport)
    {
        this.crashReportOSInfo = par1CrashReport;
    }

    public String getOsAsString()
    {
        return System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
    }

    public Object call()
    {
        return this.getOsAsString();
    }
}
