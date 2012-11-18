package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet70GameEvent extends Packet
{
    public static final String[] bedChat = new String[] {"tile.bed.notValid", null, null, "gameMode.changed"};

    public int bedState;

    public int gameMode;

    public Packet70GameEvent() {}

    public Packet70GameEvent(int par1, int par2)
    {
        this.bedState = par1;
        this.gameMode = par2;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.bedState = par1DataInputStream.readByte();
        this.gameMode = par1DataInputStream.readByte();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeByte(this.bedState);
        par1DataOutputStream.writeByte(this.gameMode);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleBed(this);
    }

    public int getPacketSize()
    {
        return 2;
    }
}
