package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class Packet40EntityMetadata extends Packet
{
    public int entityId;
    private List metadata;

    public Packet40EntityMetadata() {}

    public Packet40EntityMetadata(int par1, DataWatcher par2DataWatcher, boolean par3)
    {
        this.entityId = par1;

        if (par3)
        {
            this.metadata = par2DataWatcher.func_75685_c();
        }
        else
        {
            this.metadata = par2DataWatcher.unwatchAndReturnAllWatched();
        }
    }

    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.entityId = par1DataInputStream.readInt();
        this.metadata = DataWatcher.readWatchableObjects(par1DataInputStream);
    }

    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.entityId);
        DataWatcher.writeObjectsInListToStream(this.metadata, par1DataOutputStream);
    }

    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleEntityMetadata(this);
    }

    public int getPacketSize()
    {
        return 5;
    }

    @SideOnly(Side.CLIENT)
    public List getMetadata()
    {
        return this.metadata;
    }
}
