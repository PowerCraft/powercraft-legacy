package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet38EntityStatus extends Packet
{
    public int entityId;

    public byte entityStatus;

    public Packet38EntityStatus() {}

    public Packet38EntityStatus(int par1, byte par2)
    {
        this.entityId = par1;
        this.entityStatus = par2;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.entityId = par1DataInputStream.readInt();
        this.entityStatus = par1DataInputStream.readByte();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.entityId);
        par1DataOutputStream.writeByte(this.entityStatus);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleEntityStatus(this);
    }

    public int getPacketSize()
    {
        return 5;
    }
}
