package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet17Sleep extends Packet
{
    public int entityID;
    public int bedX;
    public int bedY;
    public int bedZ;
    public int field_73622_e;

    public Packet17Sleep() {}

    public Packet17Sleep(Entity par1Entity, int par2, int par3, int par4, int par5)
    {
        this.field_73622_e = par2;
        this.bedX = par3;
        this.bedY = par4;
        this.bedZ = par5;
        this.entityID = par1Entity.entityId;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.entityID = par1DataInputStream.readInt();
        this.field_73622_e = par1DataInputStream.readByte();
        this.bedX = par1DataInputStream.readInt();
        this.bedY = par1DataInputStream.readByte();
        this.bedZ = par1DataInputStream.readInt();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.entityID);
        par1DataOutputStream.writeByte(this.field_73622_e);
        par1DataOutputStream.writeInt(this.bedX);
        par1DataOutputStream.writeByte(this.bedY);
        par1DataOutputStream.writeInt(this.bedZ);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleSleep(this);
    }

    public int getPacketSize()
    {
        return 14;
    }
}
