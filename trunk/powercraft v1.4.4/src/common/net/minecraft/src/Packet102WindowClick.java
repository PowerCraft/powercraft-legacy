package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet102WindowClick extends Packet
{
    public int window_Id;

    public int inventorySlot;

    public int mouseClick;

    public short action;

    public ItemStack itemStack;
    public int holdingShift;

    public Packet102WindowClick() {}

    @SideOnly(Side.CLIENT)
    public Packet102WindowClick(int par1, int par2, int par3, int par4, ItemStack par5ItemStack, short par6)
    {
        this.window_Id = par1;
        this.inventorySlot = par2;
        this.mouseClick = par3;
        this.itemStack = par5ItemStack;
        this.action = par6;
        this.holdingShift = par4;
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleWindowClick(this);
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.window_Id = par1DataInputStream.readByte();
        this.inventorySlot = par1DataInputStream.readShort();
        this.mouseClick = par1DataInputStream.readByte();
        this.action = par1DataInputStream.readShort();
        this.holdingShift = par1DataInputStream.readByte();
        this.itemStack = readItemStack(par1DataInputStream);
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeByte(this.window_Id);
        par1DataOutputStream.writeShort(this.inventorySlot);
        par1DataOutputStream.writeByte(this.mouseClick);
        par1DataOutputStream.writeShort(this.action);
        par1DataOutputStream.writeByte(this.holdingShift);
        writeItemStack(this.itemStack, par1DataOutputStream);
    }

    public int getPacketSize()
    {
        return 11;
    }
}
