package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet204ClientInfo extends Packet
{
    private String field_73468_a;
    private int field_73466_b;
    private int field_73467_c;
    private boolean field_73464_d;
    private int field_73465_e;

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.field_73468_a = readString(par1DataInputStream, 7);
        this.field_73466_b = par1DataInputStream.readByte();
        byte var2 = par1DataInputStream.readByte();
        this.field_73467_c = var2 & 7;
        this.field_73464_d = (var2 & 8) == 8;
        this.field_73465_e = par1DataInputStream.readByte();
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        writeString(this.field_73468_a, par1DataOutputStream);
        par1DataOutputStream.writeByte(this.field_73466_b);
        par1DataOutputStream.writeByte(this.field_73467_c | (this.field_73464_d ? 1 : 0) << 3);
        par1DataOutputStream.writeByte(this.field_73465_e);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.func_72504_a(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 0;
    }

    public String func_73459_d()
    {
        return this.field_73468_a;
    }

    public int func_73461_f()
    {
        return this.field_73466_b;
    }

    public int func_73463_g()
    {
        return this.field_73467_c;
    }

    public boolean func_73460_h()
    {
        return this.field_73464_d;
    }

    public int func_73462_i()
    {
        return this.field_73465_e;
    }

    public boolean func_73278_e()
    {
        return true;
    }

    public boolean func_73268_a(Packet par1Packet)
    {
        return true;
    }
}
