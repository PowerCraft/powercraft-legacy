package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet207SetScore extends Packet
{
    public String field_96488_a = "";
    public String field_96486_b = "";
    public int field_96487_c = 0;
    public int field_96485_d = 0;

    public Packet207SetScore() {}

    public Packet207SetScore(Score par1, int par2)
    {
        this.field_96488_a = par1.func_96653_e();
        this.field_96486_b = par1.func_96645_d().func_96679_b();
        this.field_96487_c = par1.func_96652_c();
        this.field_96485_d = par2;
    }

    public Packet207SetScore(String par1)
    {
        this.field_96488_a = par1;
        this.field_96486_b = "";
        this.field_96487_c = 0;
        this.field_96485_d = 1;
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.field_96488_a = readString(par1DataInputStream, 16);
        this.field_96485_d = par1DataInputStream.readByte();

        if (this.field_96485_d != 1)
        {
            this.field_96486_b = readString(par1DataInputStream, 16);
            this.field_96487_c = par1DataInputStream.readInt();
        }
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        writeString(this.field_96488_a, par1DataOutputStream);
        par1DataOutputStream.writeByte(this.field_96485_d);

        if (this.field_96485_d != 1)
        {
            writeString(this.field_96486_b, par1DataOutputStream);
            par1DataOutputStream.writeInt(this.field_96487_c);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.func_96437_a(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 2 + this.field_96488_a.length() + 2 + this.field_96486_b.length() + 4 + 1;
    }
}
