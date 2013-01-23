package org.bouncycastle.crypto;

public class BufferedBlockCipher
{
    protected byte[] field_71801_a;
    protected int field_71799_b;
    protected boolean field_71800_c;
    protected BlockCipher field_71797_d;
    protected boolean field_71798_e;
    protected boolean field_71796_f;

    protected BufferedBlockCipher() {}

    public BufferedBlockCipher(BlockCipher par1BlockCipher)
    {
        this.field_71797_d = par1BlockCipher;
        this.field_71801_a = new byte[par1BlockCipher.getBlockSize()];
        this.field_71799_b = 0;
        String var2 = par1BlockCipher.getAlgorithmName();
        int var3 = var2.indexOf(47) + 1;
        this.field_71796_f = var3 > 0 && var2.startsWith("PGP", var3);

        if (this.field_71796_f)
        {
            this.field_71798_e = true;
        }
        else
        {
            this.field_71798_e = var3 > 0 && (var2.startsWith("CFB", var3) || var2.startsWith("OFB", var3) || var2.startsWith("OpenPGP", var3) || var2.startsWith("SIC", var3) || var2.startsWith("GCTR", var3));
        }
    }

    public void init(boolean par1, CipherParameters par2CipherParameters) throws IllegalArgumentException
    {
        this.field_71800_c = par1;
        this.func_71794_b();
        this.field_71797_d.func_71805_a(par1, par2CipherParameters);
    }

    public int func_71792_a()
    {
        return this.field_71797_d.getBlockSize();
    }

    public int func_71793_a(int par1)
    {
        int var2 = par1 + this.field_71799_b;
        int var3;

        if (this.field_71796_f)
        {
            var3 = var2 % this.field_71801_a.length - (this.field_71797_d.getBlockSize() + 2);
        }
        else
        {
            var3 = var2 % this.field_71801_a.length;
        }

        return var2 - var3;
    }

    public int func_71789_b(int par1)
    {
        return par1 + this.field_71799_b;
    }

    public int func_71791_a(byte[] par1ArrayOfByte, int par2, int par3, byte[] par4ArrayOfByte, int par5) throws DataLengthException, IllegalStateException
    {
        if (par3 < 0)
        {
            throw new IllegalArgumentException("Can\'t have a negative input length!");
        }
        else
        {
            int var6 = this.func_71792_a();
            int var7 = this.func_71793_a(par3);

            if (var7 > 0 && par5 + var7 > par4ArrayOfByte.length)
            {
                throw new DataLengthException("output buffer too short");
            }
            else
            {
                int var8 = 0;
                int var9 = this.field_71801_a.length - this.field_71799_b;

                if (par3 > var9)
                {
                    System.arraycopy(par1ArrayOfByte, par2, this.field_71801_a, this.field_71799_b, var9);
                    var8 += this.field_71797_d.func_71806_a(this.field_71801_a, 0, par4ArrayOfByte, par5);
                    this.field_71799_b = 0;
                    par3 -= var9;

                    for (par2 += var9; par3 > this.field_71801_a.length; par2 += var6)
                    {
                        var8 += this.field_71797_d.func_71806_a(par1ArrayOfByte, par2, par4ArrayOfByte, par5 + var8);
                        par3 -= var6;
                    }
                }

                System.arraycopy(par1ArrayOfByte, par2, this.field_71801_a, this.field_71799_b, par3);
                this.field_71799_b += par3;

                if (this.field_71799_b == this.field_71801_a.length)
                {
                    var8 += this.field_71797_d.func_71806_a(this.field_71801_a, 0, par4ArrayOfByte, par5 + var8);
                    this.field_71799_b = 0;
                }

                return var8;
            }
        }
    }

    public int func_71790_a(byte[] par1ArrayOfByte, int par2) throws DataLengthException, IllegalStateException
    {
        int var4;

        try
        {
            int var3 = 0;

            if (par2 + this.field_71799_b > par1ArrayOfByte.length)
            {
                throw new DataLengthException("output buffer too short for doFinal()");
            }

            if (this.field_71799_b != 0)
            {
                if (!this.field_71798_e)
                {
                    throw new DataLengthException("data not block size aligned");
                }

                this.field_71797_d.func_71806_a(this.field_71801_a, 0, this.field_71801_a, 0);
                var3 = this.field_71799_b;
                this.field_71799_b = 0;
                System.arraycopy(this.field_71801_a, 0, par1ArrayOfByte, par2, var3);
            }

            var4 = var3;
        }
        finally
        {
            this.func_71794_b();
        }

        return var4;
    }

    public void func_71794_b()
    {
        for (int var1 = 0; var1 < this.field_71801_a.length; ++var1)
        {
            this.field_71801_a[var1] = 0;
        }

        this.field_71799_b = 0;
        this.field_71797_d.func_71803_c();
    }
}
