package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet62LevelSound extends Packet
{
    private String soundName;

    private int effectX;

    private int effectY = Integer.MAX_VALUE;

    private int effectZ;

    private float volume;

    private int pitch;

    public Packet62LevelSound() {}

    public Packet62LevelSound(String par1Str, double par2, double par4, double par6, float par8, float par9)
    {
        this.soundName = par1Str;
        this.effectX = (int)(par2 * 8.0D);
        this.effectY = (int)(par4 * 8.0D);
        this.effectZ = (int)(par6 * 8.0D);
        this.volume = par8;
        this.pitch = (int)(par9 * 63.0F);

        if (this.pitch < 0)
        {
            this.pitch = 0;
        }

        if (this.pitch > 255)
        {
            this.pitch = 255;
        }
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.soundName = readString(par1DataInputStream, 32);
        this.effectX = par1DataInputStream.readInt();
        this.effectY = par1DataInputStream.readInt();
        this.effectZ = par1DataInputStream.readInt();
        this.volume = par1DataInputStream.readFloat();
        this.pitch = par1DataInputStream.readUnsignedByte();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        writeString(this.soundName, par1DataOutputStream);
        par1DataOutputStream.writeInt(this.effectX);
        par1DataOutputStream.writeInt(this.effectY);
        par1DataOutputStream.writeInt(this.effectZ);
        par1DataOutputStream.writeFloat(this.volume);
        par1DataOutputStream.writeByte(this.pitch);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleLevelSound(this);
    }

    public int getPacketSize()
    {
        return 24;
    }

    @SideOnly(Side.CLIENT)
    public String getSoundName()
    {
        return this.soundName;
    }

    @SideOnly(Side.CLIENT)
    public double getEffectX()
    {
        return (double)((float)this.effectX / 8.0F);
    }

    @SideOnly(Side.CLIENT)
    public double getEffectY()
    {
        return (double)((float)this.effectY / 8.0F);
    }

    @SideOnly(Side.CLIENT)
    public double getEffectZ()
    {
        return (double)((float)this.effectZ / 8.0F);
    }

    @SideOnly(Side.CLIENT)
    public float getVolume()
    {
        return this.volume;
    }

    @SideOnly(Side.CLIENT)

    public float getPitch()
    {
        return (float)this.pitch / 63.0F;
    }
}
