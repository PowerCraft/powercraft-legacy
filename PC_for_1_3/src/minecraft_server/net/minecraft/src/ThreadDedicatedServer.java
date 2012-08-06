package net.minecraft.src;

public final class ThreadDedicatedServer extends Thread
{
    final DedicatedServer field_79030_a;

    public ThreadDedicatedServer(DedicatedServer par1DedicatedServer)
    {
        this.field_79030_a = par1DedicatedServer;
    }

    public void run()
    {
        this.field_79030_a.stopServer();
    }
}
