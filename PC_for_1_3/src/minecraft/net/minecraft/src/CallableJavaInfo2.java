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

    public String func_71491_a()
    {
        return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
    }

    public Object call()
    {
        return this.func_71491_a();
    }
}
