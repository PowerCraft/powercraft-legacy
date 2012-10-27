package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableJavaInfo2 implements Callable
{
    /** Gets Java Enviroment Info to the Crash Report. */
    final CrashReport crashReportJavaInfo2;

    CallableJavaInfo2(CrashReport par1CrashReport)
    {
        this.crashReportJavaInfo2 = par1CrashReport;
    }

    /**
     * Retuns the Java VM Information as a String.  Includes the VM Name, VM Info and VM Vendor.
     */
    public String getJavaVMInfoAsString()
    {
        return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
    }

    public Object call()
    {
        return this.getJavaVMInfoAsString();
    }
}
