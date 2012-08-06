package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class ThreadServerApplication extends Thread
{
    final MinecraftServer field_73716_a;

    public ThreadServerApplication(MinecraftServer par1MinecraftServer, String par2Str)
    {
        super(par2Str);
        this.field_73716_a = par1MinecraftServer;
    }

    public void run()
    {
        this.field_73716_a.run();
    }
}
