package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class Packet104WindowItems extends Packet
{
    public int windowId;

    public ItemStack[] itemStack;

    public Packet104WindowItems() {}

    public Packet104WindowItems(int par1, List par2List)
    {
        this.windowId = par1;
        this.itemStack = new ItemStack[par2List.size()];

        for (int var3 = 0; var3 < this.itemStack.length; ++var3)
        {
            ItemStack var4 = (ItemStack)par2List.get(var3);
            this.itemStack[var3] = var4 == null ? null : var4.copy();
        }
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.windowId = par1DataInputStream.readByte();
        short var2 = par1DataInputStream.readShort();
        this.itemStack = new ItemStack[var2];

        for (int var3 = 0; var3 < var2; ++var3)
        {
            this.itemStack[var3] = readItemStack(par1DataInputStream);
        }
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeByte(this.windowId);
        par1DataOutputStream.writeShort(this.itemStack.length);

        for (int var2 = 0; var2 < this.itemStack.length; ++var2)
        {
            writeItemStack(this.itemStack[var2], par1DataOutputStream);
        }
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleWindowItems(this);
    }

    public int getPacketSize()
    {
        return 3 + this.itemStack.length * 5;
    }
}
