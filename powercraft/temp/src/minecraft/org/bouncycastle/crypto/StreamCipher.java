package org.bouncycastle.crypto;

import org.bouncycastle.crypto.DataLengthException;

public interface StreamCipher {

   byte func_74851_a(byte var1);

   void func_74850_a(byte[] var1, int var2, int var3, byte[] var4, int var5) throws DataLengthException;
}
