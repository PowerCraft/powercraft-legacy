package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cpw.mods.fml.common.network.FMLNetworkHandler;

public class MemoryConnection implements INetworkManager
{
    private static final SocketAddress mySocketAddress = new InetSocketAddress("127.0.0.1", 0);
    private final List readPacketCache = Collections.synchronizedList(new ArrayList());
    private MemoryConnection pairedConnection;
    private NetHandler myNetHandler;

    private boolean shuttingDown = false;
    private String shutdownReason = "";
    private Object[] field_74439_g;
    private boolean gamePaused = false;

    public MemoryConnection(NetHandler par1NetHandler) throws IOException
    {
        this.myNetHandler = par1NetHandler;
    }

    public void setNetHandler(NetHandler par1NetHandler)
    {
        this.myNetHandler = par1NetHandler;
    }

    public void addToSendQueue(Packet par1Packet)
    {
        if (!this.shuttingDown)
        {
            this.pairedConnection.processOrCachePacket(par1Packet);
        }
    }

    public void wakeThreads() {}

    @SideOnly(Side.CLIENT)
    public void closeConnections()
    {
        this.pairedConnection = null;
        this.myNetHandler = null;
    }

    @SideOnly(Side.CLIENT)
    public boolean isConnectionActive()
    {
        return !this.shuttingDown && this.pairedConnection != null;
    }

    public void processReadPackets()
    {
        int var1 = 2500;

        while (var1-- >= 0 && !this.readPacketCache.isEmpty())
        {
            Packet var2 = (Packet)this.readPacketCache.remove(0);
            var2.processPacket(this.myNetHandler);
        }

        if (this.readPacketCache.size() > var1)
        {
            System.out.println("Memory connection overburdened; after processing 2500 packets, we still have " + this.readPacketCache.size() + " to go!");
        }

        if (this.shuttingDown && this.readPacketCache.isEmpty())
        {
            this.myNetHandler.handleErrorMessage(this.shutdownReason, this.field_74439_g);
            FMLNetworkHandler.onConnectionClosed(this, this.myNetHandler.getPlayer());
        }
    }

    public SocketAddress getSocketAddress()
    {
        return mySocketAddress;
    }

    public void serverShutdown()
    {
        this.shuttingDown = true;
    }

    public void networkShutdown(String par1Str, Object ... par2ArrayOfObj)
    {
        this.shuttingDown = true;
        this.shutdownReason = par1Str;
        this.field_74439_g = par2ArrayOfObj;
    }

    public int packetSize()
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public void pairWith(MemoryConnection par1MemoryConnection)
    {
        this.pairedConnection = par1MemoryConnection;
        par1MemoryConnection.pairedConnection = this;
    }

    @SideOnly(Side.CLIENT)
    public boolean isGamePaused()
    {
        return this.gamePaused;
    }

    @SideOnly(Side.CLIENT)
    public void setGamePaused(boolean par1)
    {
        this.gamePaused = par1;
    }

    @SideOnly(Side.CLIENT)
    public MemoryConnection getPairedConnection()
    {
        return this.pairedConnection;
    }

    public void processOrCachePacket(Packet par1Packet)
    {
        String var2 = this.myNetHandler.isServerHandler() ? ">" : "<";

        if (par1Packet.isWritePacket() && this.myNetHandler.canProcessPackets())
        {
            par1Packet.processPacket(this.myNetHandler);
        }
        else
        {
            this.readPacketCache.add(par1Packet);
        }
    }
}
