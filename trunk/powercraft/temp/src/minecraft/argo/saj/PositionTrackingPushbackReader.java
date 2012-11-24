package argo.saj;

import argo.saj.ThingWithPosition;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

@SideOnly(Side.CLIENT)
final class PositionTrackingPushbackReader implements ThingWithPosition {

   private final PushbackReader field_74571_a;
   private int field_74569_b = 0;
   private int field_74570_c = 1;
   private boolean field_74568_d = false;


   public PositionTrackingPushbackReader(Reader p_i3243_1_) {
      this.field_74571_a = new PushbackReader(p_i3243_1_);
   }

   public void func_74567_a(char p_74567_1_) throws IOException {
      --this.field_74569_b;
      if(this.field_74569_b < 0) {
         this.field_74569_b = 0;
      }

      this.field_74571_a.unread(p_74567_1_);
   }

   public void func_74566_a(char[] p_74566_1_) throws IOException {
      this.field_74569_b -= p_74566_1_.length;
      if(this.field_74569_b < 0) {
         this.field_74569_b = 0;
      }

   }

   public int func_74564_a() throws IOException {
      int var1 = this.field_74571_a.read();
      this.func_74563_a(var1);
      return var1;
   }

   public int func_74565_b(char[] p_74565_1_) throws IOException {
      int var2 = this.field_74571_a.read(p_74565_1_);
      char[] var3 = p_74565_1_;
      int var4 = p_74565_1_.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         char var6 = var3[var5];
         this.func_74563_a(var6);
      }

      return var2;
   }

   private void func_74563_a(int p_74563_1_) {
      if(13 == p_74563_1_) {
         this.field_74569_b = 0;
         ++this.field_74570_c;
         this.field_74568_d = true;
      } else {
         if(10 == p_74563_1_ && !this.field_74568_d) {
            this.field_74569_b = 0;
            ++this.field_74570_c;
         } else {
            ++this.field_74569_b;
         }

         this.field_74568_d = false;
      }

   }

   public int func_74562_b() {
      return this.field_74569_b;
   }

   public int func_74561_c() {
      return this.field_74570_c;
   }
}
