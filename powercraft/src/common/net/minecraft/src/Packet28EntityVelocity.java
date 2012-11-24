package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet28EntityVelocity extends Packet
{
    public int entityId;
    public int motionX;
    public int motionY;
    public int motionZ;

    public Packet28EntityVelocity() {}

    public Packet28EntityVelocity(Entity par1Entity)
    {
        this(par1Entity.entityId, par1Entity.motionX, par1Entity.motionY, par1Entity.motionZ);
    }

    public Packet28EntityVelocity(int par1, double par2, double par4, double par6)
    {
        this.entityId = par1;
        double var8 = 3.9D;

        if (par2 < -var8)
        {
            par2 = -var8;
        }

        if (par4 < -var8)
        {
            par4 = -var8;
        }

        if (par6 < -var8)
        {
            par6 = -var8;
        }

        if (par2 > var8)
        {
            par2 = var8;
        }

        if (par4 > var8)
        {
            par4 = var8;
        }

        if (par6 > var8)
        {
            par6 = var8;
        }

        this.motionX = (int)(par2 * 8000.0D);
        this.motionY = (int)(par4 * 8000.0D);
        this.motionZ = (int)(par6 * 8000.0D);
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.entityId = par1DataInputStream.readInt();
        this.motionX = par1DataInputStream.readShort();
        this.motionY = par1DataInputStream.readShort();
        this.motionZ = par1DataInputStream.readShort();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.entityId);
        par1DataOutputStream.writeShort(this.motionX);
        par1DataOutputStream.writeShort(this.motionY);
        par1DataOutputStream.writeShort(this.motionZ);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleEntityVelocity(this);
    }

    public int getPacketSize()
    {
        return 10;
    }

    public boolean isRealPacket()
    {
        return true;
    }

    public boolean containsSameEntityIDAs(Packet par1Packet)
    {
        Packet28EntityVelocity var2 = (Packet28EntityVelocity)par1Packet;
        return var2.entityId == this.entityId;
    }
}
