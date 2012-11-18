package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet19EntityAction extends Packet
{
    public int entityId;

    public int state;

    public Packet19EntityAction() {}

    @SideOnly(Side.CLIENT)
    public Packet19EntityAction(Entity par1Entity, int par2)
    {
        this.entityId = par1Entity.entityId;
        this.state = par2;
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.entityId = par1DataInputStream.readInt();
        this.state = par1DataInputStream.readByte();
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.entityId);
        par1DataOutputStream.writeByte(this.state);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleEntityAction(this);
    }

    public int getPacketSize()
    {
        return 5;
    }
}
