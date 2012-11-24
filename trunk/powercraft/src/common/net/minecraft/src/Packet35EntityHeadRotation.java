package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet35EntityHeadRotation extends Packet
{
    public int entityId;
    public byte headRotationYaw;

    public Packet35EntityHeadRotation() {}

    public Packet35EntityHeadRotation(int par1, byte par2)
    {
        this.entityId = par1;
        this.headRotationYaw = par2;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.entityId = par1DataInputStream.readInt();
        this.headRotationYaw = par1DataInputStream.readByte();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.entityId);
        par1DataOutputStream.writeByte(this.headRotationYaw);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleEntityHeadRotation(this);
    }

    public int getPacketSize()
    {
        return 5;
    }

    public boolean isRealPacket()
    {
        return true;
    }

    public boolean containsSameEntityIDAs(Packet par1Packet)
    {
        Packet35EntityHeadRotation var2 = (Packet35EntityHeadRotation)par1Packet;
        return var2.entityId == this.entityId;
    }

    public boolean isWritePacket()
    {
        return true;
    }
}
