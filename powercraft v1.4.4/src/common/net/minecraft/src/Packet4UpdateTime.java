package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet4UpdateTime extends Packet
{
    public long field_82562_a;

    public long time;

    public Packet4UpdateTime() {}

    public Packet4UpdateTime(long par1, long par3)
    {
        this.field_82562_a = par1;
        this.time = par3;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.field_82562_a = par1DataInputStream.readLong();
        this.time = par1DataInputStream.readLong();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeLong(this.field_82562_a);
        par1DataOutputStream.writeLong(this.time);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleUpdateTime(this);
    }

    public int getPacketSize()
    {
        return 16;
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
