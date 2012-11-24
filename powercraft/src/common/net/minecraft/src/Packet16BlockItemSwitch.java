package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet16BlockItemSwitch extends Packet
{
    public int id;

    public Packet16BlockItemSwitch() {}

    @SideOnly(Side.CLIENT)
    public Packet16BlockItemSwitch(int par1)
    {
        this.id = par1;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.id = par1DataInputStream.readShort();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeShort(this.id);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleBlockItemSwitch(this);
    }

    public int getPacketSize()
    {
        return 2;
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
