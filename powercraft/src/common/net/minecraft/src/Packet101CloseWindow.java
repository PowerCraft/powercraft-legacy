package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet101CloseWindow extends Packet
{
    public int windowId;

    public Packet101CloseWindow() {}

    public Packet101CloseWindow(int par1)
    {
        this.windowId = par1;
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleCloseWindow(this);
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.windowId = par1DataInputStream.readByte();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeByte(this.windowId);
    }

    public int getPacketSize()
    {
        return 1;
    }
}
