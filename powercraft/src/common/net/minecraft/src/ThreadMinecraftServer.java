package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class ThreadMinecraftServer extends Thread
{
    final MinecraftServer field_82553_a;

    public ThreadMinecraftServer(MinecraftServer par1MinecraftServer, String par2Str)
    {
        super(par2Str);
        this.field_82553_a = par1MinecraftServer;
    }

    public void run()
    {
        this.field_82553_a.run();
    }
}
