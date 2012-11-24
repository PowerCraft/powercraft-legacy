package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class KeyParameter implements CipherParameters {

   private byte[] field_71784_a;


   public KeyParameter(byte[] p_i4045_1_) {
      this(p_i4045_1_, 0, p_i4045_1_.length);
   }

   public KeyParameter(byte[] p_i4046_1_, int p_i4046_2_, int p_i4046_3_) {
      this.field_71784_a = new byte[p_i4046_3_];
      System.arraycopy(p_i4046_1_, p_i4046_2_, this.field_71784_a, 0, p_i4046_3_);
   }

   public byte[] func_71783_a() {
      return this.field_71784_a;
   }
}
