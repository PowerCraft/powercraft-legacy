package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class ParametersWithIV implements CipherParameters {

   private byte[] field_71782_a;
   private CipherParameters field_71781_b;


   public ParametersWithIV(CipherParameters p_i4047_1_, byte[] p_i4047_2_, int p_i4047_3_, int p_i4047_4_) {
      this.field_71782_a = new byte[p_i4047_4_];
      this.field_71781_b = p_i4047_1_;
      System.arraycopy(p_i4047_2_, p_i4047_3_, this.field_71782_a, 0, p_i4047_4_);
   }

   public byte[] func_71779_a() {
      return this.field_71782_a;
   }

   public CipherParameters func_71780_b() {
      return this.field_71781_b;
   }
}
