package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class ParametersWithIV implements CipherParameters
{
    private byte[] field_71782_a;
    private CipherParameters field_71781_b;

    public ParametersWithIV(CipherParameters par1CipherParameters, byte[] par2ArrayOfByte, int par3, int par4)
    {
        this.field_71782_a = new byte[par4];
        this.field_71781_b = par1CipherParameters;
        System.arraycopy(par2ArrayOfByte, par3, this.field_71782_a, 0, par4);
    }

    public byte[] func_71779_a()
    {
        return this.field_71782_a;
    }

    public CipherParameters func_71780_b()
    {
        return this.field_71781_b;
    }
}
