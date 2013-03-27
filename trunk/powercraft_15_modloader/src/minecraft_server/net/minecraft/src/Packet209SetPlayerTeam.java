package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Packet209SetPlayerTeam extends Packet
{
    public String field_96495_a = "";
    public String field_96493_b = "";
    public String field_96494_c = "";
    public String field_96491_d = "";
    public Collection field_96492_e = new ArrayList();
    public int field_96489_f = 0;
    public int field_98212_g;

    public Packet209SetPlayerTeam() {}

    public Packet209SetPlayerTeam(ScorePlayerTeam par1, int par2)
    {
        this.field_96495_a = par1.func_96661_b();
        this.field_96489_f = par2;

        if (par2 == 0 || par2 == 2)
        {
            this.field_96493_b = par1.func_96669_c();
            this.field_96494_c = par1.func_96668_e();
            this.field_96491_d = par1.func_96663_f();
            this.field_98212_g = par1.func_98299_i();
        }

        if (par2 == 0)
        {
            this.field_96492_e.addAll(par1.func_96670_d());
        }
    }

    public Packet209SetPlayerTeam(ScorePlayerTeam par1, Collection par2, int par3)
    {
        if (par3 != 3 && par3 != 4)
        {
            throw new IllegalArgumentException("Method must be join or leave for player constructor");
        }
        else if (par2 != null && !par2.isEmpty())
        {
            this.field_96489_f = par3;
            this.field_96495_a = par1.func_96661_b();
            this.field_96492_e.addAll(par2);
        }
        else
        {
            throw new IllegalArgumentException("Players cannot be null/empty");
        }
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        this.field_96495_a = readString(par1DataInputStream, 16);
        this.field_96489_f = par1DataInputStream.readByte();

        if (this.field_96489_f == 0 || this.field_96489_f == 2)
        {
            this.field_96493_b = readString(par1DataInputStream, 32);
            this.field_96494_c = readString(par1DataInputStream, 16);
            this.field_96491_d = readString(par1DataInputStream, 16);
            this.field_98212_g = par1DataInputStream.readByte();
        }

        if (this.field_96489_f == 0 || this.field_96489_f == 3 || this.field_96489_f == 4)
        {
            short var2 = par1DataInputStream.readShort();

            for (int var3 = 0; var3 < var2; ++var3)
            {
                this.field_96492_e.add(readString(par1DataInputStream, 16));
            }
        }
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        writeString(this.field_96495_a, par1DataOutputStream);
        par1DataOutputStream.writeByte(this.field_96489_f);

        if (this.field_96489_f == 0 || this.field_96489_f == 2)
        {
            writeString(this.field_96493_b, par1DataOutputStream);
            writeString(this.field_96494_c, par1DataOutputStream);
            writeString(this.field_96491_d, par1DataOutputStream);
            par1DataOutputStream.writeByte(this.field_98212_g);
        }

        if (this.field_96489_f == 0 || this.field_96489_f == 3 || this.field_96489_f == 4)
        {
            par1DataOutputStream.writeShort(this.field_96492_e.size());
            Iterator var2 = this.field_96492_e.iterator();

            while (var2.hasNext())
            {
                String var3 = (String)var2.next();
                writeString(var3, par1DataOutputStream);
            }
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.func_96435_a(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 3 + this.field_96495_a.length();
    }
}
