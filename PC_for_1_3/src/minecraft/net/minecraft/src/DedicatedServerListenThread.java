package net.minecraft.src;

import java.io.IOException;
import java.net.InetAddress;
import net.minecraft.server.MinecraftServer;

public class DedicatedServerListenThread extends NetworkListenThread
{
    private final ServerListenThread field_71763_c;

    public DedicatedServerListenThread(MinecraftServer par1MinecraftServer, InetAddress par2InetAddress, int par3) throws IOException
    {
        super(par1MinecraftServer);
        this.field_71763_c = new ServerListenThread(this, par2InetAddress, par3);
        this.field_71763_c.start();
    }

    public void stopListening()
    {
        super.stopListening();
        this.field_71763_c.func_71768_b();
        this.field_71763_c.interrupt();
    }

    /**
     * processes packets and pending connections
     */
    public void networkTick()
    {
        this.field_71763_c.processPendingConnections();
        super.networkTick();
    }

    public DedicatedServer func_71762_c()
    {
        return (DedicatedServer)super.getServer();
    }

    public void func_71761_a(InetAddress par1InetAddress)
    {
        this.field_71763_c.func_71769_a(par1InetAddress);
    }

    public MinecraftServer getServer()
    {
        return this.func_71762_c();
    }
}
