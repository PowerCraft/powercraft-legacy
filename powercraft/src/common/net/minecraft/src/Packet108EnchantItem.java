package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet108EnchantItem extends Packet
{
    public int windowId;

    public int enchantment;

    public Packet108EnchantItem() {}

    @SideOnly(Side.CLIENT)
    public Packet108EnchantItem(int par1, int par2)
    {
        this.windowId = par1;
        this.enchantment = par2;
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleEnchantItem(this);
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.windowId = par1DataInputStream.readByte();
        this.enchantment = par1DataInputStream.readByte();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeByte(this.windowId);
        par1DataOutputStream.writeByte(this.enchantment);
    }

    public int getPacketSize()
    {
        return 2;
    }
}
