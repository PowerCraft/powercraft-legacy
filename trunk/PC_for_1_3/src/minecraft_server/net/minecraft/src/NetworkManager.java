package net.minecraft.src;

import java.net.SocketAddress;

public interface NetworkManager
{
    /**
     * Sets the NetHandler for this NetworkManager. Server-only.
     */
    void setNetHandler(NetHandler var1);

    /**
     * Adds the packet to the correct send queue (chunk data packets go to a separate queue).
     */
    void addToSendQueue(Packet var1);

    void func_74427_a();

    /**
     * Checks timeouts and processes all pending read packets.
     */
    void processReadPackets();

    /**
     * Returns the socket address of the remote side. Server-only.
     */
    SocketAddress getRemoteAddress();

    /**
     * Shuts down the server. (Only actually used on the server)
     */
    void serverShutdown();

    /**
     * Returns the number of chunk data packets waiting to be sent.
     */
    int getNumChunkDataPackets();

    /**
     * Shuts down the network with the specified reason. Closes all streams and sockets, spawns NetworkMasterThread to
     * stop reading and writing threads.
     */
    void networkShutdown(String var1, Object ... var2);
}
