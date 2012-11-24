package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet132TileEntityData extends Packet
{
    public int xPosition;

    public int yPosition;

    public int zPosition;

    public int actionType;

    public NBTTagCompound customParam1;

    public Packet132TileEntityData()
    {
        this.isChunkDataPacket = true;
    }

    public Packet132TileEntityData(int par1, int par2, int par3, int par4, NBTTagCompound par5NBTTagCompound)
    {
        this.isChunkDataPacket = true;
        this.xPosition = par1;
        this.yPosition = par2;
        this.zPosition = par3;
        this.actionType = par4;
        this.customParam1 = par5NBTTagCompound;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.xPosition = par1DataInputStream.readInt();
        this.yPosition = par1DataInputStream.readShort();
        this.zPosition = par1DataInputStream.readInt();
        this.actionType = par1DataInputStream.readByte();
        this.customParam1 = readNBTTagCompound(par1DataInputStream);
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.xPosition);
        par1DataOutputStream.writeShort(this.yPosition);
        par1DataOutputStream.writeInt(this.zPosition);
        par1DataOutputStream.writeByte((byte)this.actionType);
        writeNBTTagCompound(this.customParam1, par1DataOutputStream);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleTileEntityData(this);
    }

    public int getPacketSize()
    {
        return 25;
    }
}
