package net.minecraft.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.server.dedicated.DedicatedServer;

@SideOnly(Side.SERVER)
public final class ThreadDedicatedServer extends Thread
{
    final DedicatedServer field_92021_a;

    public ThreadDedicatedServer(DedicatedServer par1DedicatedServer)
    {
        this.field_92021_a = par1DedicatedServer;
    }

    public void run()
    {
        this.field_92021_a.stopServer();
    }
}
