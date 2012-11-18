package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet105UpdateProgressbar extends Packet
{
    public int windowId;

    public int progressBar;

    public int progressBarValue;

    public Packet105UpdateProgressbar() {}

    public Packet105UpdateProgressbar(int par1, int par2, int par3)
    {
        this.windowId = par1;
        this.progressBar = par2;
        this.progressBarValue = par3;
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleUpdateProgressbar(this);
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.windowId = par1DataInputStream.readByte();
        this.progressBar = par1DataInputStream.readShort();
        this.progressBarValue = par1DataInputStream.readShort();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeByte(this.windowId);
        par1DataOutputStream.writeShort(this.progressBar);
        par1DataOutputStream.writeShort(this.progressBarValue);
    }

    public int getPacketSize()
    {
        return 5;
    }
}
