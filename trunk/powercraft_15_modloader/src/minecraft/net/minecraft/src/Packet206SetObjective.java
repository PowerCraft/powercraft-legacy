package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet206SetObjective extends Packet
{
    public String field_96484_a;
    public String field_96482_b;
    public int field_96483_c;

    public Packet206SetObjective() {}

    public Packet206SetObjective(ScoreObjective par1, int par2)
    {
        this.field_96484_a = par1.func_96679_b();
        this.field_96482_b = par1.func_96678_d();
        this.field_96483_c = par2;
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.field_96484_a = readString(par1DataInputStream, 16);
        this.field_96482_b = readString(par1DataInputStream, 32);
        this.field_96483_c = par1DataInputStream.readByte();
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        writeString(this.field_96484_a, par1DataOutputStream);
        writeString(this.field_96482_b, par1DataOutputStream);
        par1DataOutputStream.writeByte(this.field_96483_c);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.func_96436_a(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 2 + this.field_96484_a.length() + 2 + this.field_96482_b.length() + 1;
    }
}
