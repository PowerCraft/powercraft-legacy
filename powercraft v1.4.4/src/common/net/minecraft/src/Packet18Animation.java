package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet18Animation extends Packet
{
    public int entityId;
    public int animate;

    public Packet18Animation() {}

    public Packet18Animation(Entity par1Entity, int par2)
    {
        this.entityId = par1Entity.entityId;
        this.animate = par2;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.entityId = par1DataInputStream.readInt();
        this.animate = par1DataInputStream.readByte();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.entityId);
        par1DataOutputStream.writeByte(this.animate);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleAnimation(this);
    }

    public int getPacketSize()
    {
        return 5;
    }
}
