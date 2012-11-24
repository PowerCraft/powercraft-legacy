package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet22Collect extends Packet
{
    public int collectedEntityId;

    public int collectorEntityId;

    public Packet22Collect() {}

    public Packet22Collect(int par1, int par2)
    {
        this.collectedEntityId = par1;
        this.collectorEntityId = par2;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.collectedEntityId = par1DataInputStream.readInt();
        this.collectorEntityId = par1DataInputStream.readInt();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.collectedEntityId);
        par1DataOutputStream.writeInt(this.collectorEntityId);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleCollect(this);
    }

    public int getPacketSize()
    {
        return 8;
    }
}
