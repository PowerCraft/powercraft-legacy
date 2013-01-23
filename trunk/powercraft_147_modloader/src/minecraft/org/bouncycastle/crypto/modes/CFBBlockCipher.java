package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class CFBBlockCipher implements BlockCipher
{
    private byte[] field_71814_a;
    private byte[] field_71812_b;
    private byte[] field_71813_c;
    private int field_71810_d;
    private BlockCipher field_71811_e = null;
    private boolean field_71809_f;

    public CFBBlockCipher(BlockCipher par1BlockCipher, int par2)
    {
        this.field_71811_e = par1BlockCipher;
        this.field_71810_d = par2 / 8;
        this.field_71814_a = new byte[par1BlockCipher.getBlockSize()];
        this.field_71812_b = new byte[par1BlockCipher.getBlockSize()];
        this.field_71813_c = new byte[par1BlockCipher.getBlockSize()];
    }

    public void func_71805_a(boolean par1, CipherParameters par2CipherParameters) throws IllegalArgumentException
    {
        this.field_71809_f = par1;

        if (par2CipherParameters instanceof ParametersWithIV)
        {
            ParametersWithIV var3 = (ParametersWithIV)par2CipherParameters;
            byte[] var4 = var3.getIV();

            if (var4.length < this.field_71814_a.length)
            {
                System.arraycopy(var4, 0, this.field_71814_a, this.field_71814_a.length - var4.length, var4.length);

                for (int var5 = 0; var5 < this.field_71814_a.length - var4.length; ++var5)
                {
                    this.field_71814_a[var5] = 0;
                }
            }
            else
            {
                System.arraycopy(var4, 0, this.field_71814_a, 0, this.field_71814_a.length);
            }

            this.func_71803_c();

            if (var3.getParameters() != null)
            {
                this.field_71811_e.func_71805_a(true, var3.getParameters());
            }
        }
        else
        {
            this.func_71803_c();
            this.field_71811_e.func_71805_a(true, par2CipherParameters);
        }
    }

    /**
     * Return the name of the algorithm the cipher implements.
     */
    public String getAlgorithmName()
    {
        return this.field_71811_e.getAlgorithmName() + "/CFB" + this.field_71810_d * 8;
    }

    /**
     * Return the block size for this cipher (in bytes).
     */
    public int getBlockSize()
    {
        return this.field_71810_d;
    }

    public int func_71806_a(byte[] par1ArrayOfByte, int par2, byte[] par3ArrayOfByte, int par4) throws DataLengthException, IllegalStateException
    {
        return this.field_71809_f ? this.func_71807_b(par1ArrayOfByte, par2, par3ArrayOfByte, par4) : this.func_71808_c(par1ArrayOfByte, par2, par3ArrayOfByte, par4);
    }

    public int func_71807_b(byte[] par1ArrayOfByte, int par2, byte[] par3ArrayOfByte, int par4) throws DataLengthException, IllegalStateException
    {
        if (par2 + this.field_71810_d > par1ArrayOfByte.length)
        {
            throw new DataLengthException("input buffer too short");
        }
        else if (par4 + this.field_71810_d > par3ArrayOfByte.length)
        {
            throw new DataLengthException("output buffer too short");
        }
        else
        {
            this.field_71811_e.func_71806_a(this.field_71812_b, 0, this.field_71813_c, 0);

            for (int var5 = 0; var5 < this.field_71810_d; ++var5)
            {
                par3ArrayOfByte[par4 + var5] = (byte)(this.field_71813_c[var5] ^ par1ArrayOfByte[par2 + var5]);
            }

            System.arraycopy(this.field_71812_b, this.field_71810_d, this.field_71812_b, 0, this.field_71812_b.length - this.field_71810_d);
            System.arraycopy(par3ArrayOfByte, par4, this.field_71812_b, this.field_71812_b.length - this.field_71810_d, this.field_71810_d);
            return this.field_71810_d;
        }
    }

    public int func_71808_c(byte[] par1ArrayOfByte, int par2, byte[] par3ArrayOfByte, int par4) throws DataLengthException, IllegalStateException
    {
        if (par2 + this.field_71810_d > par1ArrayOfByte.length)
        {
            throw new DataLengthException("input buffer too short");
        }
        else if (par4 + this.field_71810_d > par3ArrayOfByte.length)
        {
            throw new DataLengthException("output buffer too short");
        }
        else
        {
            this.field_71811_e.func_71806_a(this.field_71812_b, 0, this.field_71813_c, 0);
            System.arraycopy(this.field_71812_b, this.field_71810_d, this.field_71812_b, 0, this.field_71812_b.length - this.field_71810_d);
            System.arraycopy(par1ArrayOfByte, par2, this.field_71812_b, this.field_71812_b.length - this.field_71810_d, this.field_71810_d);

            for (int var5 = 0; var5 < this.field_71810_d; ++var5)
            {
                par3ArrayOfByte[par4 + var5] = (byte)(this.field_71813_c[var5] ^ par1ArrayOfByte[par2 + var5]);
            }

            return this.field_71810_d;
        }
    }

    public void func_71803_c()
    {
        System.arraycopy(this.field_71814_a, 0, this.field_71812_b, 0, this.field_71814_a.length);
        this.field_71811_e.func_71803_c();
    }
}
