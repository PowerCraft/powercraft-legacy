package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableType implements Callable
{
    final DedicatedServer field_71743_a;

    CallableType(DedicatedServer par1DedicatedServer)
    {
        this.field_71743_a = par1DedicatedServer;
    }

    public String func_71742_a()
    {
        return "Dedicated Server";
    }

    public Object call()
    {
        return this.func_71742_a();
    }
}
