package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet255KickDisconnect extends Packet
{
    public String reason;

    public Packet255KickDisconnect() {}

    public Packet255KickDisconnect(String par1Str)
    {
        this.reason = par1Str;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.reason = readString(par1DataInputStream, 256);
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        writeString(this.reason, par1DataOutputStream);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleKickDisconnect(this);
    }

    public int getPacketSize()
    {
        return this.reason.length();
    }

    public boolean isRealPacket()
    {
        return true;
    }

    public boolean containsSameEntityIDAs(Packet par1Packet)
    {
        return true;
    }
}
