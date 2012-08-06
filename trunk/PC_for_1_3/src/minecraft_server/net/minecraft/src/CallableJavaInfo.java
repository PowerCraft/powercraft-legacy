package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableJavaInfo implements Callable
{
    final CrashReport field_71490_a;

    CallableJavaInfo(CrashReport par1CrashReport)
    {
        this.field_71490_a = par1CrashReport;
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
