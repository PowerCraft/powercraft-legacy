package org.bouncycastle.crypto.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.StreamCipher;

public class CipherInputStream extends FilterInputStream {

   private BufferedBlockCipher field_74859_a;
   private StreamCipher field_74857_b;
   private byte[] field_74858_c;
   private byte[] field_74855_d;
   private int field_74856_e;
   private int field_74853_f;
   private boolean field_74854_g;


   public CipherInputStream(InputStream p_i4042_1_, BufferedBlockCipher p_i4042_2_) {
      super(p_i4042_1_);
      this.field_74859_a = p_i4042_2_;
      this.field_74858_c = new byte[p_i4042_2_.func_71789_b(2048)];
      this.field_74855_d = new byte[2048];
   }

   private int func_74852_a() throws IOException {
      int var1 = super.available();
      if(var1 <= 0) {
         var1 = 1;
      }

      if(var1 > this.field_74855_d.length) {
         var1 = super.read(this.field_74855_d, 0, this.field_74855_d.length);
      } else {
         var1 = super.read(this.field_74855_d, 0, var1);
      }

      if(var1 < 0) {
         if(this.field_74854_g) {
            return -1;
         }

         try {
            if(this.field_74859_a != null) {
               this.field_74853_f = this.field_74859_a.func_71790_a(this.field_74858_c, 0);
            } else {
               this.field_74853_f = 0;
            }
         } catch (Exception var4) {
            throw new IOException("error processing stream: " + var4.toString());
         }

         this.field_74856_e = 0;
         this.field_74854_g = true;
         if(this.field_74856_e == this.field_74853_f) {
            return -1;
         }
      } else {
         this.field_74856_e = 0;

         try {
            if(this.field_74859_a != null) {
               this.field_74853_f = this.field_74859_a.func_71791_a(this.field_74855_d, 0, var1, this.field_74858_c, 0);
            } else {
               this.field_74857_b.func_74850_a(this.field_74855_d, 0, var1, this.field_74858_c, 0);
               this.field_74853_f = var1;
            }
         } catch (Exception var3) {
            throw new IOException("error processing stream: " + var3.toString());
         }

         if(this.field_74853_f == 0) {
            return this.func_74852_a();
         }
      }

      return this.field_74853_f;
   }

   public int read() throws IOException {
      return this.field_74856_e == this.field_74853_f && this.func_74852_a() < 0?-1:this.field_74858_c[this.field_74856_e++] & 255;
   }

   public int read(byte[] p_read_1_) throws IOException {
      return this.read(p_read_1_, 0, p_read_1_.length);
   }

   public int read(byte[] p_read_1_, int p_read_2_, int p_read_3_) throws IOException {
      if(this.field_74856_e == this.field_74853_f && this.func_74852_a() < 0) {
         return -1;
      } else {
         int var4 = this.field_74853_f - this.field_74856_e;
         if(p_read_3_ > var4) {
            System.arraycopy(this.field_74858_c, this.field_74856_e, p_read_1_, p_read_2_, var4);
            this.field_74856_e = this.field_74853_f;
            return var4;
         } else {
            System.arraycopy(this.field_74858_c, this.field_74856_e, p_read_1_, p_read_2_, p_read_3_);
            this.field_74856_e += p_read_3_;
            return p_read_3_;
         }
      }
   }

   public long skip(long p_skip_1_) throws IOException {
      if(p_skip_1_ <= 0L) {
         return 0L;
      } else {
         int var3 = this.field_74853_f - this.field_74856_e;
         if(p_skip_1_ > (long)var3) {
            this.field_74856_e = this.field_74853_f;
            return (long)var3;
         } else {
            this.field_74856_e += (int)p_skip_1_;
            return (long)((int)p_skip_1_);
         }
      }
   }

   public int available() throws IOException {
      return this.field_74853_f - this.field_74856_e;
   }

   public void close() throws IOException {
      super.close();
   }

   public boolean markSupported() {
      return false;
   }
}
