package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet21PickupSpawn extends Packet
{
    public int entityId;

    public int xPosition;

    public int yPosition;

    public int zPosition;

    public byte rotation;

    public byte pitch;

    public byte roll;
    public ItemStack itemID;

    public Packet21PickupSpawn() {}

    public Packet21PickupSpawn(EntityItem par1EntityItem)
    {
        this.entityId = par1EntityItem.entityId;
        this.itemID = par1EntityItem.item.copy();
        this.xPosition = MathHelper.floor_double(par1EntityItem.posX * 32.0D);
        this.yPosition = MathHelper.floor_double(par1EntityItem.posY * 32.0D);
        this.zPosition = MathHelper.floor_double(par1EntityItem.posZ * 32.0D);
        this.rotation = (byte)((int)(par1EntityItem.motionX * 128.0D));
        this.pitch = (byte)((int)(par1EntityItem.motionY * 128.0D));
        this.roll = (byte)((int)(par1EntityItem.motionZ * 128.0D));
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.entityId = par1DataInputStream.readInt();
        this.itemID = readItemStack(par1DataInputStream);
        this.xPosition = par1DataInputStream.readInt();
        this.yPosition = par1DataInputStream.readInt();
        this.zPosition = par1DataInputStream.readInt();
        this.rotation = par1DataInputStream.readByte();
        this.pitch = par1DataInputStream.readByte();
        this.roll = par1DataInputStream.readByte();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.entityId);
        writeItemStack(this.itemID, par1DataOutputStream);
        par1DataOutputStream.writeInt(this.xPosition);
        par1DataOutputStream.writeInt(this.yPosition);
        par1DataOutputStream.writeInt(this.zPosition);
        par1DataOutputStream.writeByte(this.rotation);
        par1DataOutputStream.writeByte(this.pitch);
        par1DataOutputStream.writeByte(this.roll);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handlePickupSpawn(this);
    }

    public int getPacketSize()
    {
        return 24;
    }
}
