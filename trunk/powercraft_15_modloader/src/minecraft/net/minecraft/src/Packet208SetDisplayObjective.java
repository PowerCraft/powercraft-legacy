package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet208SetDisplayObjective extends Packet
{
    public int field_96481_a;
    public String field_96480_b;

    public Packet208SetDisplayObjective() {}

    public Packet208SetDisplayObjective(int par1, ScoreObjective par2ScoreObjective)
    {
        this.field_96481_a = par1;

        if (par2ScoreObjective == null)
        {
            this.field_96480_b = "";
        }
        else
        {
            this.field_96480_b = par2ScoreObjective.func_96679_b();
        }
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.field_96481_a = par1DataInputStream.readByte();
        this.field_96480_b = readString(par1DataInputStream, 16);
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeByte(this.field_96481_a);
        writeString(this.field_96480_b, par1DataOutputStream);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.func_96438_a(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 3 + this.field_96480_b.length();
    }
}
