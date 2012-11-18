package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet107CreativeSetSlot extends Packet
{
    public int slot;
    public ItemStack itemStack;

    public Packet107CreativeSetSlot() {}

    @SideOnly(Side.CLIENT)
    public Packet107CreativeSetSlot(int par1, ItemStack par2ItemStack)
    {
        this.slot = par1;
        this.itemStack = par2ItemStack;
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleCreativeSetSlot(this);
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.slot = par1DataInputStream.readShort();
        this.itemStack = readItemStack(par1DataInputStream);
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeShort(this.slot);
        writeItemStack(this.itemStack, par1DataOutputStream);
    }

    public int getPacketSize()
    {
        return 8;
    }
}
