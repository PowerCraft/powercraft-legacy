package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet30Entity extends Packet
{
    public int entityId;

    public byte xPosition;

    public byte yPosition;

    public byte zPosition;

    public byte yaw;

    public byte pitch;

    public boolean rotating = false;

    public Packet30Entity() {}

    public Packet30Entity(int par1)
    {
        this.entityId = par1;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.entityId = par1DataInputStream.readInt();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.entityId);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleEntity(this);
    }

    public int getPacketSize()
    {
        return 4;
    }

    public String toString()
    {
        return "Entity_" + super.toString();
    }

    public boolean isRealPacket()
    {
        return true;
    }

    public boolean containsSameEntityIDAs(Packet par1Packet)
    {
        Packet30Entity var2 = (Packet30Entity)par1Packet;
        return var2.entityId == this.entityId;
    }
}
