package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet42RemoveEntityEffect extends Packet
{
    public int entityId;

    public byte effectId;

    public Packet42RemoveEntityEffect() {}

    public Packet42RemoveEntityEffect(int par1, PotionEffect par2PotionEffect)
    {
        this.entityId = par1;
        this.effectId = (byte)(par2PotionEffect.getPotionID() & 255);
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.entityId = par1DataInputStream.readInt();
        this.effectId = par1DataInputStream.readByte();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.entityId);
        par1DataOutputStream.writeByte(this.effectId);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleRemoveEntityEffect(this);
    }

    public int getPacketSize()
    {
        return 5;
    }
}
