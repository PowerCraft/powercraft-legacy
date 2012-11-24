package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet55BlockDestroy extends Packet
{
    private int entityId;

    private int posX;

    private int posY;

    private int posZ;

    private int destroyedStage;

    public Packet55BlockDestroy() {}

    public Packet55BlockDestroy(int par1, int par2, int par3, int par4, int par5)
    {
        this.entityId = par1;
        this.posX = par2;
        this.posY = par3;
        this.posZ = par4;
        this.destroyedStage = par5;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.entityId = par1DataInputStream.readInt();
        this.posX = par1DataInputStream.readInt();
        this.posY = par1DataInputStream.readInt();
        this.posZ = par1DataInputStream.readInt();
        this.destroyedStage = par1DataInputStream.read();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.entityId);
        par1DataOutputStream.writeInt(this.posX);
        par1DataOutputStream.writeInt(this.posY);
        par1DataOutputStream.writeInt(this.posZ);
        par1DataOutputStream.write(this.destroyedStage);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleBlockDestroy(this);
    }

    public int getPacketSize()
    {
        return 13;
    }

    @SideOnly(Side.CLIENT)

    public int getEntityId()
    {
        return this.entityId;
    }

    @SideOnly(Side.CLIENT)

    public int getPosX()
    {
        return this.posX;
    }

    @SideOnly(Side.CLIENT)

    public int getPosY()
    {
        return this.posY;
    }

    @SideOnly(Side.CLIENT)

    public int getPosZ()
    {
        return this.posZ;
    }

    @SideOnly(Side.CLIENT)

    public int getDestroyedStage()
    {
        return this.destroyedStage;
    }

    public boolean isRealPacket()
    {
        return true;
    }

    public boolean containsSameEntityIDAs(Packet par1Packet)
    {
        Packet55BlockDestroy var2 = (Packet55BlockDestroy)par1Packet;
        return var2.entityId == this.entityId;
    }
}
