package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet5PlayerInventory extends Packet
{
    public int entityID;

    public int slot;

    private ItemStack itemSlot;

    public Packet5PlayerInventory() {}

    public Packet5PlayerInventory(int par1, int par2, ItemStack par3ItemStack)
    {
        this.entityID = par1;
        this.slot = par2;
        this.itemSlot = par3ItemStack == null ? null : par3ItemStack.copy();
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.entityID = par1DataInputStream.readInt();
        this.slot = par1DataInputStream.readShort();
        this.itemSlot = readItemStack(par1DataInputStream);
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.entityID);
        par1DataOutputStream.writeShort(this.slot);
        writeItemStack(this.itemSlot, par1DataOutputStream);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handlePlayerInventory(this);
    }

    public int getPacketSize()
    {
        return 8;
    }

    @SideOnly(Side.CLIENT)

    public ItemStack getItemSlot()
    {
        return this.itemSlot;
    }

    public boolean isRealPacket()
    {
        return true;
    }

    public boolean containsSameEntityIDAs(Packet par1Packet)
    {
        Packet5PlayerInventory var2 = (Packet5PlayerInventory)par1Packet;
        return var2.entityID == this.entityID && var2.slot == this.slot;
    }
}
