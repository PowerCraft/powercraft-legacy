package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet71Weather extends Packet
{
    public int entityID;
    public int posX;
    public int posY;
    public int posZ;
    public int isLightningBolt;

    public Packet71Weather() {}

    public Packet71Weather(Entity par1Entity)
    {
        this.entityID = par1Entity.entityId;
        this.posX = MathHelper.floor_double(par1Entity.posX * 32.0D);
        this.posY = MathHelper.floor_double(par1Entity.posY * 32.0D);
        this.posZ = MathHelper.floor_double(par1Entity.posZ * 32.0D);

        if (par1Entity instanceof EntityLightningBolt)
        {
            this.isLightningBolt = 1;
        }
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.entityID = par1DataInputStream.readInt();
        this.isLightningBolt = par1DataInputStream.readByte();
        this.posX = par1DataInputStream.readInt();
        this.posY = par1DataInputStream.readInt();
        this.posZ = par1DataInputStream.readInt();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.entityID);
        par1DataOutputStream.writeByte(this.isLightningBolt);
        par1DataOutputStream.writeInt(this.posX);
        par1DataOutputStream.writeInt(this.posY);
        par1DataOutputStream.writeInt(this.posZ);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleWeather(this);
    }

    public int getPacketSize()
    {
        return 17;
    }
}
