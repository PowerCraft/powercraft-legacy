package org.bouncycastle.crypto;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;

public interface BlockCipher {

   void func_71805_a(boolean var1, CipherParameters var2) throws IllegalArgumentException;

   String func_71802_a();

   int func_71804_b();

   int func_71806_a(byte[] var1, int var2, byte[] var3, int var4) throws DataLengthException, IllegalStateException;

   void func_71803_c();
}
