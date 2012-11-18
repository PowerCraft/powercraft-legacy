package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet10Flying extends Packet
{
    public double xPosition;

    public double yPosition;

    public double zPosition;

    public double stance;

    public float yaw;

    public float pitch;

    public boolean onGround;

    public boolean moving;

    public boolean rotating;

    public Packet10Flying() {}

    @SideOnly(Side.CLIENT)
    public Packet10Flying(boolean par1)
    {
        this.onGround = par1;
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleFlying(this);
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.onGround = par1DataInputStream.read() != 0;
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.write(this.onGround ? 1 : 0);
    }

    public int getPacketSize()
    {
        return 1;
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
