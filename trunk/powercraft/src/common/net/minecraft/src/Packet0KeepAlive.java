package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet0KeepAlive extends Packet
{
    public int randomId;

    public Packet0KeepAlive() {}

    public Packet0KeepAlive(int par1)
    {
        this.randomId = par1;
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleKeepAlive(this);
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.randomId = par1DataInputStream.readInt();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.randomId);
    }

    public int getPacketSize()
    {
        return 4;
    }

    public boolean isRealPacket()
    {
        return true;
    }

    public boolean containsSameEntityIDAs(Packet par1Packet)
    {
        return true;
    }

    public boolean isWritePacket()
    {
        return true;
    }
}
