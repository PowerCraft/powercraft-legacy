package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class CFBBlockCipher implements BlockCipher {

   private byte[] field_71814_a;
   private byte[] field_71812_b;
   private byte[] field_71813_c;
   private int field_71810_d;
   private BlockCipher field_71811_e = null;
   private boolean field_71809_f;


   public CFBBlockCipher(BlockCipher p_i4044_1_, int p_i4044_2_) {
      this.field_71811_e = p_i4044_1_;
      this.field_71810_d = p_i4044_2_ / 8;
      this.field_71814_a = new byte[p_i4044_1_.func_71804_b()];
      this.field_71812_b = new byte[p_i4044_1_.func_71804_b()];
      this.field_71813_c = new byte[p_i4044_1_.func_71804_b()];
   }

   public void func_71805_a(boolean p_71805_1_, CipherParameters p_71805_2_) throws IllegalArgumentException {
      this.field_71809_f = p_71805_1_;
      if(p_71805_2_ instanceof ParametersWithIV) {
         ParametersWithIV var3 = (ParametersWithIV)p_71805_2_;
         byte[] var4 = var3.func_71779_a();
         if(var4.length < this.field_71814_a.length) {
            System.arraycopy(var4, 0, this.field_71814_a, this.field_71814_a.length - var4.length, var4.length);

            for(int var5 = 0; var5 < this.field_71814_a.length - var4.length; ++var5) {
               this.field_71814_a[var5] = 0;
            }
         } else {
            System.arraycopy(var4, 0, this.field_71814_a, 0, this.field_71814_a.length);
         }

         this.func_71803_c();
         if(var3.func_71780_b() != null) {
            this.field_71811_e.func_71805_a(true, var3.func_71780_b());
         }
      } else {
         this.func_71803_c();
         this.field_71811_e.func_71805_a(true, p_71805_2_);
      }

   }

   public String func_71802_a() {
      return this.field_71811_e.func_71802_a() + "/CFB" + this.field_71810_d * 8;
   }

   public int func_71804_b() {
      return this.field_71810_d;
   }

   public int func_71806_a(byte[] p_71806_1_, int p_71806_2_, byte[] p_71806_3_, int p_71806_4_) throws DataLengthException, IllegalStateException {
      return this.field_71809_f?this.func_71807_b(p_71806_1_, p_71806_2_, p_71806_3_, p_71806_4_):this.func_71808_c(p_71806_1_, p_71806_2_, p_71806_3_, p_71806_4_);
   }

   public int func_71807_b(byte[] p_71807_1_, int p_71807_2_, byte[] p_71807_3_, int p_71807_4_) throws DataLengthException, IllegalStateException {
      if(p_71807_2_ + this.field_71810_d > p_71807_1_.length) {
         throw new DataLengthException("input buffer too short");
      } else if(p_71807_4_ + this.field_71810_d > p_71807_3_.length) {
         throw new DataLengthException("output buffer too short");
      } else {
         this.field_71811_e.func_71806_a(this.field_71812_b, 0, this.field_71813_c, 0);

         for(int var5 = 0; var5 < this.field_71810_d; ++var5) {
            p_71807_3_[p_71807_4_ + var5] = (byte)(this.field_71813_c[var5] ^ p_71807_1_[p_71807_2_ + var5]);
         }

         System.arraycopy(this.field_71812_b, this.field_71810_d, this.field_71812_b, 0, this.field_71812_b.length - this.field_71810_d);
         System.arraycopy(p_71807_3_, p_71807_4_, this.field_71812_b, this.field_71812_b.length - this.field_71810_d, this.field_71810_d);
         return this.field_71810_d;
      }
   }

   public int func_71808_c(byte[] p_71808_1_, int p_71808_2_, byte[] p_71808_3_, int p_71808_4_) throws DataLengthException, IllegalStateException {
      if(p_71808_2_ + this.field_71810_d > p_71808_1_.length) {
         throw new DataLengthException("input buffer too short");
      } else if(p_71808_4_ + this.field_71810_d > p_71808_3_.length) {
         throw new DataLengthException("output buffer too short");
      } else {
         this.field_71811_e.func_71806_a(this.field_71812_b, 0, this.field_71813_c, 0);
         System.arraycopy(this.field_71812_b, this.field_71810_d, this.field_71812_b, 0, this.field_71812_b.length - this.field_71810_d);
         System.arraycopy(p_71808_1_, p_71808_2_, this.field_71812_b, this.field_71812_b.length - this.field_71810_d, this.field_71810_d);

         for(int var5 = 0; var5 < this.field_71810_d; ++var5) {
            p_71808_3_[p_71808_4_ + var5] = (byte)(this.field_71813_c[var5] ^ p_71808_1_[p_71808_2_ + var5]);
         }

         return this.field_71810_d;
      }
   }

   public void func_71803_c() {
      System.arraycopy(this.field_71814_a, 0, this.field_71812_b, 0, this.field_71814_a.length);
      this.field_71811_e.func_71803_c();
   }
}
