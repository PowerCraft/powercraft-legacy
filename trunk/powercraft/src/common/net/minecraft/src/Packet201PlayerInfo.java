package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet201PlayerInfo extends Packet
{
    public String playerName;

    public boolean isConnected;
    public int ping;

    public Packet201PlayerInfo() {}

    public Packet201PlayerInfo(String par1Str, boolean par2, int par3)
    {
        this.playerName = par1Str;
        this.isConnected = par2;
        this.ping = par3;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.playerName = readString(par1DataInputStream, 16);
        this.isConnected = par1DataInputStream.readByte() != 0;
        this.ping = par1DataInputStream.readShort();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        writeString(this.playerName, par1DataOutputStream);
        par1DataOutputStream.writeByte(this.isConnected ? 1 : 0);
        par1DataOutputStream.writeShort(this.ping);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handlePlayerInfo(this);
    }

    public int getPacketSize()
    {
        return this.playerName.length() + 2 + 1 + 2;
    }
}
