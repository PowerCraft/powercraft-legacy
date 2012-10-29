package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.SERVER)
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
