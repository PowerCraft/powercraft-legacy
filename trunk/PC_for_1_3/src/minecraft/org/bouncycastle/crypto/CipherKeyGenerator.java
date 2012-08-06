package org.bouncycastle.crypto;

import java.security.SecureRandom;

public class CipherKeyGenerator
{
    protected SecureRandom field_71788_a;
    protected int field_71787_b;

    public void init(KeyGenerationParameters par1)
    {
        this.field_71788_a = par1.func_71843_a();
        this.field_71787_b = (par1.func_71842_b() + 7) / 8;
    }

    public byte[] generateKey()
    {
        byte[] var1 = new byte[this.field_71787_b];
        this.field_71788_a.nextBytes(var1);
        return var1;
    }
}
