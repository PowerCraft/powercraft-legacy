package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet200Statistic extends Packet
{
    public int statisticId;
    public int amount;

    public Packet200Statistic() {}

    public Packet200Statistic(int par1, int par2)
    {
        this.statisticId = par1;
        this.amount = par2;
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleStatistic(this);
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.statisticId = par1DataInputStream.readInt();
        this.amount = par1DataInputStream.readByte();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.statisticId);
        par1DataOutputStream.writeByte(this.amount);
    }

    public int getPacketSize()
    {
        return 6;
    }

    public boolean isWritePacket()
    {
        return true;
    }
}
