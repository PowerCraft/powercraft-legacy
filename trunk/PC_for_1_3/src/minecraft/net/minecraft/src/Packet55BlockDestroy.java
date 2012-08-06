package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet55BlockDestroy extends Packet
{
    private int field_73329_a;
    private int field_73327_b;
    private int field_73328_c;
    private int field_73325_d;
    private int field_73326_e;

    public Packet55BlockDestroy() {}

    public Packet55BlockDestroy(int par1, int par2, int par3, int par4, int par5)
    {
        this.field_73329_a = par1;
        this.field_73327_b = par2;
        this.field_73328_c = par3;
        this.field_73325_d = par4;
        this.field_73326_e = par5;
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.field_73329_a = par1DataInputStream.readInt();
        this.field_73327_b = par1DataInputStream.readInt();
        this.field_73328_c = par1DataInputStream.readInt();
        this.field_73325_d = par1DataInputStream.readInt();
        this.field_73326_e = par1DataInputStream.read();
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(this.field_73329_a);
        par1DataOutputStream.writeInt(this.field_73327_b);
        par1DataOutputStream.writeInt(this.field_73328_c);
        par1DataOutputStream.writeInt(this.field_73325_d);
        par1DataOutputStream.write(this.field_73326_e);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.handleBlockDestroy(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 13;
    }

    public int func_73322_d()
    {
        return this.field_73329_a;
    }

    public int func_73321_f()
    {
        return this.field_73327_b;
    }

    public int func_73324_g()
    {
        return this.field_73328_c;
    }

    public int func_73320_h()
    {
        return this.field_73325_d;
    }

    public int func_73323_i()
    {
        return this.field_73326_e;
    }

    /**
     * only false for the abstract Packet class, all real packets return true
     */
    public boolean isRealPacket()
    {
        return true;
    }

    /**
     * eg return packet30entity.entityId == entityId; WARNING : will throw if you compare a packet to a different packet
     * class
     */
    public boolean containsSameEntityIDAs(Packet par1Packet)
    {
        Packet55BlockDestroy var2 = (Packet55BlockDestroy)par1Packet;
        return var2.field_73329_a == this.field_73329_a;
    }
}
