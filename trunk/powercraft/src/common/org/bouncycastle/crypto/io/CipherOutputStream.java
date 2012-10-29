package org.bouncycastle.crypto.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.StreamCipher;

public class CipherOutputStream extends FilterOutputStream
{
    private BufferedBlockCipher field_74849_a;
    private StreamCipher field_74847_b;
    private byte[] field_74848_c = new byte[1];
    private byte[] field_74846_d;

    public CipherOutputStream(OutputStream par1OutputStream, BufferedBlockCipher par2BufferedBlockCipher)
    {
        super(par1OutputStream);
        this.field_74849_a = par2BufferedBlockCipher;
        this.field_74846_d = new byte[par2BufferedBlockCipher.func_71792_a()];
    }

    public void write(int par1) throws IOException
    {
        this.field_74848_c[0] = (byte)par1;

        if (this.field_74849_a != null)
        {
            int var2 = this.field_74849_a.func_71791_a(this.field_74848_c, 0, 1, this.field_74846_d, 0);

            if (var2 != 0)
            {
                this.out.write(this.field_74846_d, 0, var2);
            }
        }
        else
        {
            this.out.write(this.field_74847_b.returnByte((byte)par1));
        }
    }

    public void write(byte[] par1) throws IOException
    {
        this.write(par1, 0, par1.length);
    }

    public void write(byte[] par1, int par2, int par3) throws IOException
    {
        byte[] var4;

        if (this.field_74849_a != null)
        {
            var4 = new byte[this.field_74849_a.func_71789_b(par3)];
            int var5 = this.field_74849_a.func_71791_a(par1, par2, par3, var4, 0);

            if (var5 != 0)
            {
                this.out.write(var4, 0, var5);
            }
        }
        else
        {
            var4 = new byte[par3];
            this.field_74847_b.func_74850_a(par1, par2, par3, var4, 0);
            this.out.write(var4, 0, par3);
        }
    }

    public void flush() throws IOException
    {
        super.flush();
    }

    public void close() throws IOException
    {
        try
        {
            if (this.field_74849_a != null)
            {
                byte[] var1 = new byte[this.field_74849_a.func_71789_b(0)];
                int var2 = this.field_74849_a.func_71790_a(var1, 0);

                if (var2 != 0)
                {
                    this.out.write(var1, 0, var2);
                }
            }
        }
        catch (Exception var3)
        {
            throw new IOException("Error closing stream: " + var3.toString());
        }

        this.flush();
        super.close();
    }
}
