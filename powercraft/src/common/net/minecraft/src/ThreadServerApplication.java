package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class ThreadServerApplication extends Thread
{
    /** Instance of MinecraftServer. */
    final MinecraftServer theServer;

    public ThreadServerApplication(MinecraftServer par1MinecraftServer, String par2Str)
    {
        super(par2Str);
        this.theServer = par1MinecraftServer;
    }

    public void run()
    {
        this.theServer.run();
    }
}
