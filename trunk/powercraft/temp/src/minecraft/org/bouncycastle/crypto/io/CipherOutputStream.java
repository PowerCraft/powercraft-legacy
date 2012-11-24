package org.bouncycastle.crypto.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.StreamCipher;

public class CipherOutputStream extends FilterOutputStream {

   private BufferedBlockCipher field_74849_a;
   private StreamCipher field_74847_b;
   private byte[] field_74848_c = new byte[1];
   private byte[] field_74846_d;


   public CipherOutputStream(OutputStream p_i4043_1_, BufferedBlockCipher p_i4043_2_) {
      super(p_i4043_1_);
      this.field_74849_a = p_i4043_2_;
      this.field_74846_d = new byte[p_i4043_2_.func_71792_a()];
   }

   public void write(int p_write_1_) throws IOException {
      this.field_74848_c[0] = (byte)p_write_1_;
      if(this.field_74849_a != null) {
         int var2 = this.field_74849_a.func_71791_a(this.field_74848_c, 0, 1, this.field_74846_d, 0);
         if(var2 != 0) {
            this.out.write(this.field_74846_d, 0, var2);
         }
      } else {
         this.out.write(this.field_74847_b.func_74851_a((byte)p_write_1_));
      }

   }

   public void write(byte[] p_write_1_) throws IOException {
      this.write(p_write_1_, 0, p_write_1_.length);
   }

   public void write(byte[] p_write_1_, int p_write_2_, int p_write_3_) throws IOException {
      byte[] var4;
      if(this.field_74849_a != null) {
         var4 = new byte[this.field_74849_a.func_71789_b(p_write_3_)];
         int var5 = this.field_74849_a.func_71791_a(p_write_1_, p_write_2_, p_write_3_, var4, 0);
         if(var5 != 0) {
            this.out.write(var4, 0, var5);
         }
      } else {
         var4 = new byte[p_write_3_];
         this.field_74847_b.func_74850_a(p_write_1_, p_write_2_, p_write_3_, var4, 0);
         this.out.write(var4, 0, p_write_3_);
      }

   }

   public void flush() throws IOException {
      super.flush();
   }

   public void close() throws IOException {
      try {
         if(this.field_74849_a != null) {
            byte[] var1 = new byte[this.field_74849_a.func_71789_b(0)];
            int var2 = this.field_74849_a.func_71790_a(var1, 0);
            if(var2 != 0) {
               this.out.write(var1, 0, var2);
            }
         }
      } catch (Exception var3) {
         throw new IOException("Error closing stream: " + var3.toString());
      }

      this.flush();
      super.close();
   }
}
