package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class KeyParameter implements CipherParameters
{
    private byte[] field_71784_a;

    public KeyParameter(byte[] par1ArrayOfByte)
    {
        this(par1ArrayOfByte, 0, par1ArrayOfByte.length);
    }

    public KeyParameter(byte[] par1ArrayOfByte, int par2, int par3)
    {
        this.field_71784_a = new byte[par3];
        System.arraycopy(par1ArrayOfByte, par2, this.field_71784_a, 0, par3);
    }

    public byte[] func_71783_a()
    {
        return this.field_71784_a;
    }
}
