package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet43Experience extends Packet
{
    public float experience;

    public int experienceTotal;

    public int experienceLevel;

    public Packet43Experience() {}

    public Packet43Experience(float par1, int par2, int par3)
    {
        this.experience = par1;
        this.experienceTotal = par2;
        this.experienceLevel = par3;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.experience = par1DataInputStream.readFloat();
        this.experienceLevel = par1DataInputStream.readShort();
        this.experienceTotal = par1DataInputStream.readShort();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeFloat(this.experience);
        par1DataOutputStream.writeShort(this.experienceLevel);
        par1DataOutputStream.writeShort(this.experienceTotal);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleExperience(this);
    }

    public int getPacketSize()
    {
        return 4;
    }

    public boolean isRealPacket()
    {
        return true;
    }

    public boolean containsSameEntityIDAs(Packet par1Packet)
    {
        return true;
    }
}
