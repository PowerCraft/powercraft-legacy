package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableJavaInfo2 implements Callable
{
    final CrashReport field_71492_a;

    CallableJavaInfo2(CrashReport par1CrashReport)
    {
        this.field_71492_a = par1CrashReport;
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
