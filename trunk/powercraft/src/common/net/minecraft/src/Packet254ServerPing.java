package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet254ServerPing extends Packet
{
    public int field_82559_a = 0;

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        try
        {
            this.field_82559_a = par1DataInputStream.readByte();
        }
        catch (Throwable var3)
        {
            this.field_82559_a = 0;
        }
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {}

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleServerPing(this);
    }

    public int getPacketSize()
    {
        return 0;
    }
}
