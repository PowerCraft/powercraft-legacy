package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet12PlayerLook extends Packet10Flying
{
    public Packet12PlayerLook()
    {
        this.rotating = true;
    }

    @SideOnly(Side.CLIENT)
    public Packet12PlayerLook(float par1, float par2, boolean par3)
    {
        this.yaw = par1;
        this.pitch = par2;
        this.onGround = par3;
        this.rotating = true;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.yaw = par1DataInputStream.readFloat();
        this.pitch = par1DataInputStream.readFloat();
        super.readPacketData(par1DataInputStream);
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeFloat(this.yaw);
        par1DataOutputStream.writeFloat(this.pitch);
        super.writePacketData(par1DataOutputStream);
    }

    public int getPacketSize()
    {
        return 9;
    }
}
