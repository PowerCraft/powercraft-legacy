package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.net.SocketAddress;

public interface INetworkManager
{
    void setNetHandler(NetHandler var1);

    void addToSendQueue(Packet var1);

    void wakeThreads();

    void processReadPackets();

    SocketAddress getSocketAddress();

    void serverShutdown();

    int packetSize();

    void networkShutdown(String var1, Object ... var2);

    @SideOnly(Side.CLIENT)
    void closeConnections();
}
