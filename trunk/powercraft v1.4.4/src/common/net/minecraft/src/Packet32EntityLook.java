package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet32EntityLook extends Packet30Entity
{
    public Packet32EntityLook()
    {
        this.rotating = true;
    }

    public Packet32EntityLook(int par1, byte par2, byte par3)
    {
        super(par1);
        this.yaw = par2;
        this.pitch = par3;
        this.rotating = true;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        super.readPacketData(par1DataInputStream);
        this.yaw = par1DataInputStream.readByte();
        this.pitch = par1DataInputStream.readByte();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        super.writePacketData(par1DataOutputStream);
        par1DataOutputStream.writeByte(this.yaw);
        par1DataOutputStream.writeByte(this.pitch);
    }

    public int getPacketSize()
    {
        return 6;
    }
}
