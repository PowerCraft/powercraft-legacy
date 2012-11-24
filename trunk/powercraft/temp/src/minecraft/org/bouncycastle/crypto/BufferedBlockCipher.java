package org.bouncycastle.crypto;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;

public class BufferedBlockCipher {

   protected byte[] field_71801_a;
   protected int field_71799_b;
   protected boolean field_71800_c;
   protected BlockCipher field_71797_d;
   protected boolean field_71798_e;
   protected boolean field_71796_f;


   protected BufferedBlockCipher() {}

   public BufferedBlockCipher(BlockCipher p_i4038_1_) {
      this.field_71797_d = p_i4038_1_;
      this.field_71801_a = new byte[p_i4038_1_.func_71804_b()];
      this.field_71799_b = 0;
      String var2 = p_i4038_1_.func_71802_a();
      int var3 = var2.indexOf(47) + 1;
      this.field_71796_f = var3 > 0 && var2.startsWith("PGP", var3);
      if(this.field_71796_f) {
         this.field_71798_e = true;
      } else {
         this.field_71798_e = var3 > 0 && (var2.startsWith("CFB", var3) || var2.startsWith("OFB", var3) || var2.startsWith("OpenPGP", var3) || var2.startsWith("SIC", var3) || var2.startsWith("GCTR", var3));
      }

   }

   public void init(boolean p_init_1_, CipherParameters p_init_2_) throws IllegalArgumentException {
      this.field_71800_c = p_init_1_;
      this.func_71794_b();
      this.field_71797_d.func_71805_a(p_init_1_, p_init_2_);
   }

   public int func_71792_a() {
      return this.field_71797_d.func_71804_b();
   }

   public int func_71793_a(int p_71793_1_) {
      int var2 = p_71793_1_ + this.field_71799_b;
      int var3;
      if(this.field_71796_f) {
         var3 = var2 % this.field_71801_a.length - (this.field_71797_d.func_71804_b() + 2);
      } else {
         var3 = var2 % this.field_71801_a.length;
      }

      return var2 - var3;
   }

   public int func_71789_b(int p_71789_1_) {
      return p_71789_1_ + this.field_71799_b;
   }

   public int func_71791_a(byte[] p_71791_1_, int p_71791_2_, int p_71791_3_, byte[] p_71791_4_, int p_71791_5_) throws DataLengthException, IllegalStateException {
      if(p_71791_3_ < 0) {
         throw new IllegalArgumentException("Can\'t have a negative input length!");
      } else {
         int var6 = this.func_71792_a();
         int var7 = this.func_71793_a(p_71791_3_);
         if(var7 > 0 && p_71791_5_ + var7 > p_71791_4_.length) {
            throw new DataLengthException("output buffer too short");
         } else {
            int var8 = 0;
            int var9 = this.field_71801_a.length - this.field_71799_b;
            if(p_71791_3_ > var9) {
               System.arraycopy(p_71791_1_, p_71791_2_, this.field_71801_a, this.field_71799_b, var9);
               var8 += this.field_71797_d.func_71806_a(this.field_71801_a, 0, p_71791_4_, p_71791_5_);
               this.field_71799_b = 0;
               p_71791_3_ -= var9;

               for(p_71791_2_ += var9; p_71791_3_ > this.field_71801_a.length; p_71791_2_ += var6) {
                  var8 += this.field_71797_d.func_71806_a(p_71791_1_, p_71791_2_, p_71791_4_, p_71791_5_ + var8);
                  p_71791_3_ -= var6;
               }
            }

            System.arraycopy(p_71791_1_, p_71791_2_, this.field_71801_a, this.field_71799_b, p_71791_3_);
            this.field_71799_b += p_71791_3_;
            if(this.field_71799_b == this.field_71801_a.length) {
               var8 += this.field_71797_d.func_71806_a(this.field_71801_a, 0, p_71791_4_, p_71791_5_ + var8);
               this.field_71799_b = 0;
            }

            return var8;
         }
      }
   }

   public int func_71790_a(byte[] p_71790_1_, int p_71790_2_) throws DataLengthException, IllegalStateException {
      int var4;
      try {
         int var3 = 0;
         if(p_71790_2_ + this.field_71799_b > p_71790_1_.length) {
            throw new DataLengthException("output buffer too short for doFinal()");
         }

         if(this.field_71799_b != 0) {
            if(!this.field_71798_e) {
               throw new DataLengthException("data not block size aligned");
            }

            this.field_71797_d.func_71806_a(this.field_71801_a, 0, this.field_71801_a, 0);
            var3 = this.field_71799_b;
            this.field_71799_b = 0;
            System.arraycopy(this.field_71801_a, 0, p_71790_1_, p_71790_2_, var3);
         }

         var4 = var3;
      } finally {
         this.func_71794_b();
      }

      return var4;
   }

   public void func_71794_b() {
      for(int var1 = 0; var1 < this.field_71801_a.length; ++var1) {
         this.field_71801_a[var1] = 0;
      }

      this.field_71799_b = 0;
      this.field_71797_d.func_71803_c();
   }
}
