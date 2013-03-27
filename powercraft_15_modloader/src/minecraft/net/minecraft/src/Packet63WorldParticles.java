package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet63WorldParticles extends Packet
{
    private String field_98209_a;
    private float field_98207_b;
    private float field_98208_c;
    private float field_98205_d;
    private float field_98206_e;
    private float field_98203_f;
    private float field_98204_g;
    private float field_98210_h;
    private int field_98211_i;

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.field_98209_a = readString(par1DataInputStream, 64);
        this.field_98207_b = par1DataInputStream.readFloat();
        this.field_98208_c = par1DataInputStream.readFloat();
        this.field_98205_d = par1DataInputStream.readFloat();
        this.field_98206_e = par1DataInputStream.readFloat();
        this.field_98203_f = par1DataInputStream.readFloat();
        this.field_98204_g = par1DataInputStream.readFloat();
        this.field_98210_h = par1DataInputStream.readFloat();
        this.field_98211_i = par1DataInputStream.readInt();
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        writeString(this.field_98209_a, par1DataOutputStream);
        par1DataOutputStream.writeFloat(this.field_98207_b);
        par1DataOutputStream.writeFloat(this.field_98208_c);
        par1DataOutputStream.writeFloat(this.field_98205_d);
        par1DataOutputStream.writeFloat(this.field_98206_e);
        par1DataOutputStream.writeFloat(this.field_98203_f);
        par1DataOutputStream.writeFloat(this.field_98204_g);
        par1DataOutputStream.writeFloat(this.field_98210_h);
        par1DataOutputStream.writeInt(this.field_98211_i);
    }

    public String func_98195_d()
    {
        return this.field_98209_a;
    }

    public double func_98200_f()
    {
        return (double)this.field_98207_b;
    }

    public double func_98194_g()
    {
        return (double)this.field_98208_c;
    }

    public double func_98198_h()
    {
        return (double)this.field_98205_d;
    }

    public float func_98196_i()
    {
        return this.field_98206_e;
    }

    public float func_98201_j()
    {
        return this.field_98203_f;
    }

    public float func_98199_k()
    {
        return this.field_98204_g;
    }

    public float func_98197_l()
    {
        return this.field_98210_h;
    }

    public int func_98202_m()
    {
        return this.field_98211_i;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.func_98182_a(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 64;
    }
}
