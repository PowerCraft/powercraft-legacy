package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Container;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;
import net.minecraft.src.Slot;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public abstract class GuiContainer extends GuiScreen {

   protected static RenderItem field_74196_a = new RenderItem();
   protected int field_74194_b = 176;
   protected int field_74195_c = 166;
   public Container field_74193_d;
   protected int field_74198_m;
   protected int field_74197_n;
   private Slot field_82320_o;
   private Slot field_85051_p = null;
   private boolean field_90018_r = false;
   private ItemStack field_85050_q = null;
   private int field_85049_r = 0;
   private int field_85048_s = 0;
   private Slot field_85047_t = null;
   private long field_85046_u = 0L;
   private ItemStack field_85045_v = null;


   public GuiContainer(Container p_i3079_1_) {
      this.field_74193_d = p_i3079_1_;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.field_73882_e.field_71439_g.field_71070_bA = this.field_74193_d;
      this.field_74198_m = (this.field_73880_f - this.field_74194_b) / 2;
      this.field_74197_n = (this.field_73881_g - this.field_74195_c) / 2;
   }

   public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
      this.func_73873_v_();
      int var4 = this.field_74198_m;
      int var5 = this.field_74197_n;
      this.func_74185_a(p_73863_3_, p_73863_1_, p_73863_2_);
      GL11.glDisable('\u803a');
      RenderHelper.func_74518_a();
      GL11.glDisable(2896);
      GL11.glDisable(2929);
      super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
      RenderHelper.func_74520_c();
      GL11.glPushMatrix();
      GL11.glTranslatef((float)var4, (float)var5, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable('\u803a');
      this.field_82320_o = null;
      short var6 = 240;
      short var7 = 240;
      OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)var6 / 1.0F, (float)var7 / 1.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

      int var8;
      int var9;
      for(int var13 = 0; var13 < this.field_74193_d.field_75151_b.size(); ++var13) {
         Slot var14 = (Slot)this.field_74193_d.field_75151_b.get(var13);
         this.func_74192_a(var14);
         if(this.func_74186_a(var14, p_73863_1_, p_73863_2_)) {
            this.field_82320_o = var14;
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            var8 = var14.field_75223_e;
            var9 = var14.field_75221_f;
            this.func_73733_a(var8, var9, var8 + 16, var9 + 16, -2130706433, -2130706433);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
         }
      }

      this.func_74189_g(p_73863_1_, p_73863_2_);
      InventoryPlayer var15 = this.field_73882_e.field_71439_g.field_71071_by;
      ItemStack var16 = this.field_85050_q == null?var15.func_70445_o():this.field_85050_q;
      if(var16 != null) {
         var8 = this.field_85050_q == null?8:0;
         if(this.field_85050_q != null && this.field_90018_r) {
            var16 = var16.func_77946_l();
            var16.field_77994_a = MathHelper.func_76123_f((float)var16.field_77994_a / 2.0F);
         }

         this.func_85044_b(var16, p_73863_1_ - var4 - var8, p_73863_2_ - var5 - var8);
      }

      if(this.field_85045_v != null) {
         float var17 = (float)(Minecraft.func_71386_F() - this.field_85046_u) / 100.0F;
         if(var17 >= 1.0F) {
            var17 = 1.0F;
            this.field_85045_v = null;
         }

         var9 = this.field_85047_t.field_75223_e - this.field_85049_r;
         int var10 = this.field_85047_t.field_75221_f - this.field_85048_s;
         int var11 = this.field_85049_r + (int)((float)var9 * var17);
         int var12 = this.field_85048_s + (int)((float)var10 * var17);
         this.func_85044_b(this.field_85045_v, var11, var12);
      }

      if(var15.func_70445_o() == null && this.field_82320_o != null && this.field_82320_o.func_75216_d()) {
         ItemStack var18 = this.field_82320_o.func_75211_c();
         this.func_74184_a(var18, p_73863_1_ - var4 + 8, p_73863_2_ - var5 + 8);
      }

      GL11.glPopMatrix();
      GL11.glEnable(2896);
      GL11.glEnable(2929);
      RenderHelper.func_74519_b();
   }

   private void func_85044_b(ItemStack p_85044_1_, int p_85044_2_, int p_85044_3_) {
      GL11.glTranslatef(0.0F, 0.0F, 32.0F);
      this.field_73735_i = 200.0F;
      field_74196_a.field_77023_b = 200.0F;
      field_74196_a.func_82406_b(this.field_73886_k, this.field_73882_e.field_71446_o, p_85044_1_, p_85044_2_, p_85044_3_);
      field_74196_a.func_77021_b(this.field_73886_k, this.field_73882_e.field_71446_o, p_85044_1_, p_85044_2_, p_85044_3_);
      this.field_73735_i = 0.0F;
      field_74196_a.field_77023_b = 0.0F;
   }

   protected void func_74184_a(ItemStack p_74184_1_, int p_74184_2_, int p_74184_3_) {
      GL11.glDisable('\u803a');
      RenderHelper.func_74518_a();
      GL11.glDisable(2896);
      GL11.glDisable(2929);
      List var4 = p_74184_1_.func_82840_a(this.field_73882_e.field_71439_g, this.field_73882_e.field_71474_y.field_82882_x);
      if(!var4.isEmpty()) {
         int var5 = 0;

         int var6;
         int var7;
         for(var6 = 0; var6 < var4.size(); ++var6) {
            var7 = this.field_73886_k.func_78256_a((String)var4.get(var6));
            if(var7 > var5) {
               var5 = var7;
            }
         }

         var6 = p_74184_2_ + 12;
         var7 = p_74184_3_ - 12;
         int var9 = 8;
         if(var4.size() > 1) {
            var9 += 2 + (var4.size() - 1) * 10;
         }

         this.field_73735_i = 300.0F;
         field_74196_a.field_77023_b = 300.0F;
         int var10 = -267386864;
         this.func_73733_a(var6 - 3, var7 - 4, var6 + var5 + 3, var7 - 3, var10, var10);
         this.func_73733_a(var6 - 3, var7 + var9 + 3, var6 + var5 + 3, var7 + var9 + 4, var10, var10);
         this.func_73733_a(var6 - 3, var7 - 3, var6 + var5 + 3, var7 + var9 + 3, var10, var10);
         this.func_73733_a(var6 - 4, var7 - 3, var6 - 3, var7 + var9 + 3, var10, var10);
         this.func_73733_a(var6 + var5 + 3, var7 - 3, var6 + var5 + 4, var7 + var9 + 3, var10, var10);
         int var11 = 1347420415;
         int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
         this.func_73733_a(var6 - 3, var7 - 3 + 1, var6 - 3 + 1, var7 + var9 + 3 - 1, var11, var12);
         this.func_73733_a(var6 + var5 + 2, var7 - 3 + 1, var6 + var5 + 3, var7 + var9 + 3 - 1, var11, var12);
         this.func_73733_a(var6 - 3, var7 - 3, var6 + var5 + 3, var7 - 3 + 1, var11, var11);
         this.func_73733_a(var6 - 3, var7 + var9 + 2, var6 + var5 + 3, var7 + var9 + 3, var12, var12);

         for(int var13 = 0; var13 < var4.size(); ++var13) {
            String var14 = (String)var4.get(var13);
            if(var13 == 0) {
               var14 = "\u00a7" + Integer.toHexString(p_74184_1_.func_77953_t().field_77937_e) + var14;
            } else {
               var14 = "\u00a77" + var14;
            }

            this.field_73886_k.func_78261_a(var14, var6, var7, -1);
            if(var13 == 0) {
               var7 += 2;
            }

            var7 += 10;
         }

         this.field_73735_i = 0.0F;
         field_74196_a.field_77023_b = 0.0F;
      }

   }

   protected void func_74190_a(String p_74190_1_, int p_74190_2_, int p_74190_3_) {
      GL11.glDisable('\u803a');
      RenderHelper.func_74518_a();
      GL11.glDisable(2896);
      GL11.glDisable(2929);
      int var4 = this.field_73886_k.func_78256_a(p_74190_1_);
      int var5 = p_74190_2_ + 12;
      int var6 = p_74190_3_ - 12;
      byte var8 = 8;
      this.field_73735_i = 300.0F;
      field_74196_a.field_77023_b = 300.0F;
      int var9 = -267386864;
      this.func_73733_a(var5 - 3, var6 - 4, var5 + var4 + 3, var6 - 3, var9, var9);
      this.func_73733_a(var5 - 3, var6 + var8 + 3, var5 + var4 + 3, var6 + var8 + 4, var9, var9);
      this.func_73733_a(var5 - 3, var6 - 3, var5 + var4 + 3, var6 + var8 + 3, var9, var9);
      this.func_73733_a(var5 - 4, var6 - 3, var5 - 3, var6 + var8 + 3, var9, var9);
      this.func_73733_a(var5 + var4 + 3, var6 - 3, var5 + var4 + 4, var6 + var8 + 3, var9, var9);
      int var10 = 1347420415;
      int var11 = (var10 & 16711422) >> 1 | var10 & -16777216;
      this.func_73733_a(var5 - 3, var6 - 3 + 1, var5 - 3 + 1, var6 + var8 + 3 - 1, var10, var11);
      this.func_73733_a(var5 + var4 + 2, var6 - 3 + 1, var5 + var4 + 3, var6 + var8 + 3 - 1, var10, var11);
      this.func_73733_a(var5 - 3, var6 - 3, var5 + var4 + 3, var6 - 3 + 1, var10, var10);
      this.func_73733_a(var5 - 3, var6 + var8 + 2, var5 + var4 + 3, var6 + var8 + 3, var11, var11);
      this.field_73886_k.func_78261_a(p_74190_1_, var5, var6, -1);
      this.field_73735_i = 0.0F;
      field_74196_a.field_77023_b = 0.0F;
      GL11.glEnable(2896);
      GL11.glEnable(2929);
      RenderHelper.func_74519_b();
      GL11.glEnable('\u803a');
   }

   protected void func_74189_g(int p_74189_1_, int p_74189_2_) {}

   protected abstract void func_74185_a(float var1, int var2, int var3);

   private void func_74192_a(Slot p_74192_1_) {
      int var2 = p_74192_1_.field_75223_e;
      int var3 = p_74192_1_.field_75221_f;
      ItemStack var4 = p_74192_1_.func_75211_c();
      boolean var5 = p_74192_1_ == this.field_85051_p && this.field_85050_q != null && !this.field_90018_r;
      if(p_74192_1_ == this.field_85051_p && this.field_85050_q != null && this.field_90018_r && var4 != null) {
         var4 = var4.func_77946_l();
         var4.field_77994_a /= 2;
      }

      this.field_73735_i = 100.0F;
      field_74196_a.field_77023_b = 100.0F;
      if(var4 == null) {
         int var6 = p_74192_1_.func_75212_b();
         if(var6 >= 0) {
            GL11.glDisable(2896);
            this.field_73882_e.field_71446_o.func_78342_b(this.field_73882_e.field_71446_o.func_78341_b("/gui/items.png"));
            this.func_73729_b(var2, var3, var6 % 16 * 16, var6 / 16 * 16, 16, 16);
            GL11.glEnable(2896);
            var5 = true;
         }
      }

      if(!var5) {
         GL11.glEnable(2929);
         field_74196_a.func_82406_b(this.field_73886_k, this.field_73882_e.field_71446_o, var4, var2, var3);
         field_74196_a.func_77021_b(this.field_73886_k, this.field_73882_e.field_71446_o, var4, var2, var3);
      }

      field_74196_a.field_77023_b = 0.0F;
      this.field_73735_i = 0.0F;
   }

   private Slot func_74187_b(int p_74187_1_, int p_74187_2_) {
      for(int var3 = 0; var3 < this.field_74193_d.field_75151_b.size(); ++var3) {
         Slot var4 = (Slot)this.field_74193_d.field_75151_b.get(var3);
         if(this.func_74186_a(var4, p_74187_1_, p_74187_2_)) {
            return var4;
         }
      }

      return null;
   }

   protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
      super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
      boolean var4 = p_73864_3_ == this.field_73882_e.field_71474_y.field_74322_I.field_74512_d + 100;
      if(p_73864_3_ == 0 || p_73864_3_ == 1 || var4) {
         Slot var5 = this.func_74187_b(p_73864_1_, p_73864_2_);
         int var6 = this.field_74198_m;
         int var7 = this.field_74197_n;
         boolean var8 = p_73864_1_ < var6 || p_73864_2_ < var7 || p_73864_1_ >= var6 + this.field_74194_b || p_73864_2_ >= var7 + this.field_74195_c;
         int var9 = -1;
         if(var5 != null) {
            var9 = var5.field_75222_d;
         }

         if(var8) {
            var9 = -999;
         }

         if(this.field_73882_e.field_71474_y.field_85185_A && var8 && this.field_73882_e.field_71439_g.field_71071_by.func_70445_o() == null) {
            this.field_73882_e.func_71373_a((GuiScreen)null);
            return;
         }

         if(var9 != -1) {
            if(this.field_73882_e.field_71474_y.field_85185_A) {
               if(var5 != null && var5.func_75216_d()) {
                  this.field_85051_p = var5;
                  this.field_85050_q = null;
                  this.field_90018_r = p_73864_3_ == 1;
               } else {
                  this.field_85051_p = null;
               }
            } else if(var4) {
               this.func_74191_a(var5, var9, p_73864_3_, 3);
            } else {
               boolean var10 = var9 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
               this.func_74191_a(var5, var9, p_73864_3_, var10?1:0);
            }
         }
      }

   }

   protected void func_85041_a(int p_85041_1_, int p_85041_2_, int p_85041_3_, long p_85041_4_) {
      if(this.field_85051_p != null && this.field_73882_e.field_71474_y.field_85185_A && this.field_85050_q == null) {
         if(p_85041_3_ == 0 || p_85041_3_ == 1) {
            Slot var6 = this.func_74187_b(p_85041_1_, p_85041_2_);
            if(var6 != this.field_85051_p) {
               this.field_85050_q = this.field_85051_p.func_75211_c();
            }
         }

      }
   }

   protected void func_73879_b(int p_73879_1_, int p_73879_2_, int p_73879_3_) {
      if(this.field_85051_p != null && this.field_73882_e.field_71474_y.field_85185_A) {
         if(p_73879_3_ == 0 || p_73879_3_ == 1) {
            Slot var4 = this.func_74187_b(p_73879_1_, p_73879_2_);
            int var5 = this.field_74198_m;
            int var6 = this.field_74197_n;
            boolean var7 = p_73879_1_ < var5 || p_73879_2_ < var6 || p_73879_1_ >= var5 + this.field_74194_b || p_73879_2_ >= var6 + this.field_74195_c;
            int var8 = -1;
            if(var4 != null) {
               var8 = var4.field_75222_d;
            }

            if(var7) {
               var8 = -999;
            }

            if(this.field_85050_q == null && var4 != this.field_85051_p) {
               this.field_85050_q = this.field_85051_p.func_75211_c();
            }

            boolean var9 = var4 == null || !var4.func_75216_d();
            if(var4 != null && var4.func_75216_d() && this.field_85050_q != null && ItemStack.func_77970_a(var4.func_75211_c(), this.field_85050_q)) {
               var9 |= var4.func_75211_c().field_77994_a + this.field_85050_q.field_77994_a <= this.field_85050_q.func_77976_d();
            }

            if(var8 != -1 && this.field_85050_q != null && var9) {
               this.func_74191_a(this.field_85051_p, this.field_85051_p.field_75222_d, p_73879_3_, 0);
               this.func_74191_a(var4, var8, 0, 0);
               if(this.field_73882_e.field_71439_g.field_71071_by.func_70445_o() != null) {
                  this.func_74191_a(this.field_85051_p, this.field_85051_p.field_75222_d, p_73879_3_, 0);
                  this.field_85049_r = p_73879_1_ - var5;
                  this.field_85048_s = p_73879_2_ - var6;
                  this.field_85047_t = this.field_85051_p;
                  this.field_85045_v = this.field_85050_q;
                  this.field_85046_u = Minecraft.func_71386_F();
               } else {
                  this.field_85045_v = null;
               }
            } else if(this.field_85050_q != null) {
               this.field_85049_r = p_73879_1_ - var5;
               this.field_85048_s = p_73879_2_ - var6;
               this.field_85047_t = this.field_85051_p;
               this.field_85045_v = this.field_85050_q;
               this.field_85046_u = Minecraft.func_71386_F();
            }

            this.field_85050_q = null;
            this.field_85051_p = null;
         }

      }
   }

   private boolean func_74186_a(Slot p_74186_1_, int p_74186_2_, int p_74186_3_) {
      return this.func_74188_c(p_74186_1_.field_75223_e, p_74186_1_.field_75221_f, 16, 16, p_74186_2_, p_74186_3_);
   }

   protected boolean func_74188_c(int p_74188_1_, int p_74188_2_, int p_74188_3_, int p_74188_4_, int p_74188_5_, int p_74188_6_) {
      int var7 = this.field_74198_m;
      int var8 = this.field_74197_n;
      p_74188_5_ -= var7;
      p_74188_6_ -= var8;
      return p_74188_5_ >= p_74188_1_ - 1 && p_74188_5_ < p_74188_1_ + p_74188_3_ + 1 && p_74188_6_ >= p_74188_2_ - 1 && p_74188_6_ < p_74188_2_ + p_74188_4_ + 1;
   }

   protected void func_74191_a(Slot p_74191_1_, int p_74191_2_, int p_74191_3_, int p_74191_4_) {
      if(p_74191_1_ != null) {
         p_74191_2_ = p_74191_1_.field_75222_d;
      }

      this.field_73882_e.field_71442_b.func_78753_a(this.field_74193_d.field_75152_c, p_74191_2_, p_74191_3_, p_74191_4_, this.field_73882_e.field_71439_g);
   }

   protected void func_73869_a(char p_73869_1_, int p_73869_2_) {
      if(p_73869_2_ == 1 || p_73869_2_ == this.field_73882_e.field_71474_y.field_74315_B.field_74512_d) {
         this.field_73882_e.field_71439_g.func_71053_j();
      }

      this.func_82319_a(p_73869_2_);
      if(p_73869_2_ == this.field_73882_e.field_71474_y.field_74322_I.field_74512_d && this.field_82320_o != null && this.field_82320_o.func_75216_d()) {
         this.func_74191_a(this.field_82320_o, this.field_82320_o.field_75222_d, this.field_74195_c, 3);
      }

   }

   protected boolean func_82319_a(int p_82319_1_) {
      if(this.field_73882_e.field_71439_g.field_71071_by.func_70445_o() == null && this.field_82320_o != null) {
         for(int var2 = 0; var2 < 9; ++var2) {
            if(p_82319_1_ == 2 + var2) {
               this.func_74191_a(this.field_82320_o, this.field_82320_o.field_75222_d, var2, 2);
               return true;
            }
         }
      }

      return false;
   }

   public void func_73874_b() {
      if(this.field_73882_e.field_71439_g != null) {
         this.field_74193_d.func_75134_a(this.field_73882_e.field_71439_g);
      }
   }

   public boolean func_73868_f() {
      return false;
   }

   public void func_73876_c() {
      super.func_73876_c();
      if(!this.field_73882_e.field_71439_g.func_70089_S() || this.field_73882_e.field_71439_g.field_70128_L) {
         this.field_73882_e.field_71439_g.func_71053_j();
      }

   }

}
