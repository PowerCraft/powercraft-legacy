package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet39AttachEntity extends Packet
{
    public int entityId;
    public int vehicleEntityId;

    public Packet39AttachEntity() {}

    public Packet39AttachEntity(Entity par1Entity, Entity par2Entity)
    {
        this.entityId = par1Entity.entityId;
        this.vehicleEntityId = par2Entity != null ? par2Entity.entityId : -1;
    }

    public int getPacketSize()
    {
        return 8;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.entityId = par1DataInputStream.readInt();
        this.vehicleEntityId = par1DataInputStream.readInt();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.entityId);
        par1DataOutputStream.writeInt(this.vehicleEntityId);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleAttachEntity(this);
    }

    public boolean isRealPacket()
    {
        return true;
    }

    public boolean containsSameEntityIDAs(Packet par1Packet)
    {
        Packet39AttachEntity var2 = (Packet39AttachEntity)par1Packet;
        return var2.entityId == this.entityId;
    }
}
