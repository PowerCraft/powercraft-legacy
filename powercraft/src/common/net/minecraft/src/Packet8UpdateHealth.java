package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet8UpdateHealth extends Packet
{
    public int healthMP;
    public int food;

    public float foodSaturation;

    public Packet8UpdateHealth() {}

    public Packet8UpdateHealth(int par1, int par2, float par3)
    {
        this.healthMP = par1;
        this.food = par2;
        this.foodSaturation = par3;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.healthMP = par1DataInputStream.readShort();
        this.food = par1DataInputStream.readShort();
        this.foodSaturation = par1DataInputStream.readFloat();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeShort(this.healthMP);
        par1DataOutputStream.writeShort(this.food);
        par1DataOutputStream.writeFloat(this.foodSaturation);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleUpdateHealth(this);
    }

    public int getPacketSize()
    {
        return 8;
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
