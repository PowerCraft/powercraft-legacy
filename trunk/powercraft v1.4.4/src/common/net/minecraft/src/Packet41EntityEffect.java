package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet41EntityEffect extends Packet
{
    public int entityId;
    public byte effectId;

    public byte effectAmplifier;
    public short duration;

    public Packet41EntityEffect() {}

    public Packet41EntityEffect(int par1, PotionEffect par2PotionEffect)
    {
        this.entityId = par1;
        this.effectId = (byte)(par2PotionEffect.getPotionID() & 255);
        this.effectAmplifier = (byte)(par2PotionEffect.getAmplifier() & 255);
        this.duration = (short)par2PotionEffect.getDuration();
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.entityId = par1DataInputStream.readInt();
        this.effectId = par1DataInputStream.readByte();
        this.effectAmplifier = par1DataInputStream.readByte();
        this.duration = par1DataInputStream.readShort();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.entityId);
        par1DataOutputStream.writeByte(this.effectId);
        par1DataOutputStream.writeByte(this.effectAmplifier);
        par1DataOutputStream.writeShort(this.duration);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleEntityEffect(this);
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
        Packet41EntityEffect var2 = (Packet41EntityEffect)par1Packet;
        return var2.entityId == this.entityId && var2.effectId == this.effectId;
    }
}
