package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet131MapData extends Packet
{
    public short itemID;

    public short uniqueID;

    public byte[] itemData;

    public Packet131MapData()
    {
        this.isChunkDataPacket = true;
    }

    public Packet131MapData(short par1, short par2, byte[] par3ArrayOfByte)
    {
        this.isChunkDataPacket = true;
        this.itemID = par1;
        this.uniqueID = par2;
        this.itemData = par3ArrayOfByte;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.itemID = par1DataInputStream.readShort();
        this.uniqueID = par1DataInputStream.readShort();
        this.itemData = new byte[par1DataInputStream.readUnsignedShort()];
        par1DataInputStream.readFully(this.itemData);
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeShort(this.itemID);
        par1DataOutputStream.writeShort(this.uniqueID);
        par1DataOutputStream.writeShort(this.itemData.length);
        par1DataOutputStream.write(this.itemData);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleMapData(this);
    }

    public int getPacketSize()
    {
        return 4 + this.itemData.length;
    }
}
