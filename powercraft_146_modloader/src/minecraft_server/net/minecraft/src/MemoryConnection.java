package net.minecraft.src;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryConnection implements INetworkManager
{
    private static final SocketAddress field_74444_a = new InetSocketAddress("127.0.0.1", 0);
    private final List field_74442_b = Collections.synchronizedList(new ArrayList());
    private MemoryConnection field_74443_c;
    private NetHandler field_74440_d;
    private boolean field_74441_e = false;
    private String field_74438_f = "";
    private Object[] field_74439_g;
    private boolean field_74445_h = false;

    public MemoryConnection(NetHandler par1NetHandler) throws IOException
    {
        this.field_74440_d = par1NetHandler;
    }

    /**
     * Sets the NetHandler for this NetworkManager. Server-only.
     */
    public void setNetHandler(NetHandler par1NetHandler)
    {
        this.field_74440_d = par1NetHandler;
    }

    /**
     * Adds the packet to the correct send queue (chunk data packets go to a separate queue).
     */
    public void addToSendQueue(Packet par1Packet)
    {
        if (!this.field_74441_e)
        {
            this.field_74443_c.func_74436_b(par1Packet);
        }
    }

    /**
     * Wakes reader and writer threads
     */
    public void wakeThreads() {}

    /**
     * Checks timeouts and processes all pending read packets.
     */
    public void processReadPackets()
    {
        int var1 = 2500;

        while (var1-- >= 0 && !this.field_74442_b.isEmpty())
        {
            Packet var2 = (Packet)this.field_74442_b.remove(0);
            var2.processPacket(this.field_74440_d);
        }

        if (this.field_74442_b.size() > var1)
        {
            System.out.println("Memory connection overburdened; after processing 2500 packets, we still have " + this.field_74442_b.size() + " to go!");
        }

        if (this.field_74441_e && this.field_74442_b.isEmpty())
        {
            this.field_74440_d.handleErrorMessage(this.field_74438_f, this.field_74439_g);
        }
    }

    /**
     * Returns the socket address of the remote side. Server-only.
     */
    public SocketAddress getRemoteAddress()
    {
        return field_74444_a;
    }

    /**
     * Shuts down the server. (Only actually used on the server)
     */
    public void serverShutdown()
    {
        this.field_74441_e = true;
    }

    /**
     * Shuts down the network with the specified reason. Closes all streams and sockets, spawns NetworkMasterThread to
     * stop reading and writing threads.
     */
    public void networkShutdown(String par1Str, Object ... par2ArrayOfObj)
    {
        this.field_74441_e = true;
        this.field_74438_f = par1Str;
        this.field_74439_g = par2ArrayOfObj;
    }

    /**
     * Returns the number of chunk data packets waiting to be sent.
     */
    public int getNumChunkDataPackets()
    {
        return 0;
    }

    public void func_74436_b(Packet par1Packet)
    {
        String var2 = this.field_74440_d.isServerHandler() ? ">" : "<";

        if (par1Packet.isWritePacket() && this.field_74440_d.canProcessPackets())
        {
            par1Packet.processPacket(this.field_74440_d);
        }
        else
        {
            this.field_74442_b.add(par1Packet);
        }
    }
}
