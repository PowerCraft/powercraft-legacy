package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class Packet24MobSpawn extends Packet
{
    public int entityId;

    public int type;

    public int xPosition;

    public int yPosition;

    public int zPosition;
    public int velocityX;
    public int velocityY;
    public int velocityZ;

    public byte yaw;

    public byte pitch;

    public byte headYaw;

    private DataWatcher metaData;
    private List metadata;

    public Packet24MobSpawn() {}

    public Packet24MobSpawn(EntityLiving par1EntityLiving)
    {
        this.entityId = par1EntityLiving.entityId;
        this.type = (byte)EntityList.getEntityID(par1EntityLiving);
        this.xPosition = par1EntityLiving.myEntitySize.multiplyBy32AndRound(par1EntityLiving.posX);
        this.yPosition = MathHelper.floor_double(par1EntityLiving.posY * 32.0D);
        this.zPosition = par1EntityLiving.myEntitySize.multiplyBy32AndRound(par1EntityLiving.posZ);
        this.yaw = (byte)((int)(par1EntityLiving.rotationYaw * 256.0F / 360.0F));
        this.pitch = (byte)((int)(par1EntityLiving.rotationPitch * 256.0F / 360.0F));
        this.headYaw = (byte)((int)(par1EntityLiving.rotationYawHead * 256.0F / 360.0F));
        double var2 = 3.9D;
        double var4 = par1EntityLiving.motionX;
        double var6 = par1EntityLiving.motionY;
        double var8 = par1EntityLiving.motionZ;

        if (var4 < -var2)
        {
            var4 = -var2;
        }

        if (var6 < -var2)
        {
            var6 = -var2;
        }

        if (var8 < -var2)
        {
            var8 = -var2;
        }

        if (var4 > var2)
        {
            var4 = var2;
        }

        if (var6 > var2)
        {
            var6 = var2;
        }

        if (var8 > var2)
        {
            var8 = var2;
        }

        this.velocityX = (int)(var4 * 8000.0D);
        this.velocityY = (int)(var6 * 8000.0D);
        this.velocityZ = (int)(var8 * 8000.0D);
        this.metaData = par1EntityLiving.getDataWatcher();
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.entityId = par1DataInputStream.readInt();
        this.type = par1DataInputStream.readByte() & 255;
        this.xPosition = par1DataInputStream.readInt();
        this.yPosition = par1DataInputStream.readInt();
        this.zPosition = par1DataInputStream.readInt();
        this.yaw = par1DataInputStream.readByte();
        this.pitch = par1DataInputStream.readByte();
        this.headYaw = par1DataInputStream.readByte();
        this.velocityX = par1DataInputStream.readShort();
        this.velocityY = par1DataInputStream.readShort();
        this.velocityZ = par1DataInputStream.readShort();
        this.metadata = DataWatcher.readWatchableObjects(par1DataInputStream);
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.entityId);
        par1DataOutputStream.writeByte(this.type & 255);
        par1DataOutputStream.writeInt(this.xPosition);
        par1DataOutputStream.writeInt(this.yPosition);
        par1DataOutputStream.writeInt(this.zPosition);
        par1DataOutputStream.writeByte(this.yaw);
        par1DataOutputStream.writeByte(this.pitch);
        par1DataOutputStream.writeByte(this.headYaw);
        par1DataOutputStream.writeShort(this.velocityX);
        par1DataOutputStream.writeShort(this.velocityY);
        par1DataOutputStream.writeShort(this.velocityZ);
        this.metaData.writeWatchableObjects(par1DataOutputStream);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleMobSpawn(this);
    }

    public int getPacketSize()
    {
        return 26;
    }

    @SideOnly(Side.CLIENT)
    public List getMetadata()
    {
        if (this.metadata == null)
        {
            this.metadata = this.metaData.func_75685_c();
        }

        return this.metadata;
    }
}
