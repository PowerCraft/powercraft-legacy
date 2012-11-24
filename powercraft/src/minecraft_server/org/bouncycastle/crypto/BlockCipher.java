package org.bouncycastle.crypto;

public interface BlockCipher
{
    void func_71805_a(boolean var1, CipherParameters var2) throws IllegalArgumentException;

    /**
     * Return the name of the algorithm the cipher implements.
     */
    String getAlgorithmName();

    /**
     * Return the block size for this cipher (in bytes).
     */
    int getBlockSize();

    int func_71806_a(byte[] var1, int var2, byte[] var3, int var4) throws DataLengthException, IllegalStateException;

    void func_71803_c();
}
