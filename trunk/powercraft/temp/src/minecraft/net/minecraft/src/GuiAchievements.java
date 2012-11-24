package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Achievement;
import net.minecraft.src.AchievementList;
import net.minecraft.src.Block;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSmallButton;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderItem;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StatFileWriter;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiAchievements extends GuiScreen {

   private static final int field_74122_s = AchievementList.field_76010_a * 24 - 112;
   private static final int field_74121_t = AchievementList.field_76008_b * 24 - 112;
   private static final int field_74120_u = AchievementList.field_76009_c * 24 - 77;
   private static final int field_74119_v = AchievementList.field_76006_d * 24 - 77;
   protected int field_74114_a = 256;
   protected int field_74112_b = 202;
   protected int field_74113_c = 0;
   protected int field_74111_d = 0;
   protected double field_74117_m;
   protected double field_74115_n;
   protected double field_74116_o;
   protected double field_74125_p;
   protected double field_74124_q;
   protected double field_74123_r;
   private int field_74118_w = 0;
   private StatFileWriter field_74126_x;


   public GuiAchievements(StatFileWriter p_i3070_1_) {
      this.field_74126_x = p_i3070_1_;
      short var2 = 141;
      short var3 = 141;
      this.field_74117_m = this.field_74116_o = this.field_74124_q = (double)(AchievementList.field_76004_f.field_75993_a * 24 - var2 / 2 - 12);
      this.field_74115_n = this.field_74125_p = this.field_74123_r = (double)(AchievementList.field_76004_f.field_75991_b * 24 - var3 / 2);
   }

   public void func_73866_w_() {
      this.field_73887_h.clear();
      this.field_73887_h.add(new GuiSmallButton(1, this.field_73880_f / 2 + 24, this.field_73881_g / 2 + 74, 80, 20, StatCollector.func_74838_a("gui.done")));
   }

   protected void func_73875_a(GuiButton p_73875_1_) {
      if(p_73875_1_.field_73741_f == 1) {
         this.field_73882_e.func_71373_a((GuiScreen)null);
         this.field_73882_e.func_71381_h();
      }

      super.func_73875_a(p_73875_1_);
   }

   protected void func_73869_a(char p_73869_1_, int p_73869_2_) {
      if(p_73869_2_ == this.field_73882_e.field_71474_y.field_74315_B.field_74512_d) {
         this.field_73882_e.func_71373_a((GuiScreen)null);
         this.field_73882_e.func_71381_h();
      } else {
         super.func_73869_a(p_73869_1_, p_73869_2_);
      }

   }

   public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
      if(Mouse.isButtonDown(0)) {
         int var4 = (this.field_73880_f - this.field_74114_a) / 2;
         int var5 = (this.field_73881_g - this.field_74112_b) / 2;
         int var6 = var4 + 8;
         int var7 = var5 + 17;
         if((this.field_74118_w == 0 || this.field_74118_w == 1) && p_73863_1_ >= var6 && p_73863_1_ < var6 + 224 && p_73863_2_ >= var7 && p_73863_2_ < var7 + 155) {
            if(this.field_74118_w == 0) {
               this.field_74118_w = 1;
            } else {
               this.field_74116_o -= (double)(p_73863_1_ - this.field_74113_c);
               this.field_74125_p -= (double)(p_73863_2_ - this.field_74111_d);
               this.field_74124_q = this.field_74117_m = this.field_74116_o;
               this.field_74123_r = this.field_74115_n = this.field_74125_p;
            }

            this.field_74113_c = p_73863_1_;
            this.field_74111_d = p_73863_2_;
         }

         if(this.field_74124_q < (double)field_74122_s) {
            this.field_74124_q = (double)field_74122_s;
         }

         if(this.field_74123_r < (double)field_74121_t) {
            this.field_74123_r = (double)field_74121_t;
         }

         if(this.field_74124_q >= (double)field_74120_u) {
            this.field_74124_q = (double)(field_74120_u - 1);
         }

         if(this.field_74123_r >= (double)field_74119_v) {
            this.field_74123_r = (double)(field_74119_v - 1);
         }
      } else {
         this.field_74118_w = 0;
      }

      this.func_73873_v_();
      this.func_74110_b(p_73863_1_, p_73863_2_, p_73863_3_);
      GL11.glDisable(2896);
      GL11.glDisable(2929);
      this.func_74109_g();
      GL11.glEnable(2896);
      GL11.glEnable(2929);
   }

   public void func_73876_c() {
      this.field_74117_m = this.field_74116_o;
      this.field_74115_n = this.field_74125_p;
      double var1 = this.field_74124_q - this.field_74116_o;
      double var3 = this.field_74123_r - this.field_74125_p;
      if(var1 * var1 + var3 * var3 < 4.0D) {
         this.field_74116_o += var1;
         this.field_74125_p += var3;
      } else {
         this.field_74116_o += var1 * 0.85D;
         this.field_74125_p += var3 * 0.85D;
      }

   }

   protected void func_74109_g() {
      int var1 = (this.field_73880_f - this.field_74114_a) / 2;
      int var2 = (this.field_73881_g - this.field_74112_b) / 2;
      this.field_73886_k.func_78276_b("Achievements", var1 + 15, var2 + 5, 4210752);
   }

   protected void func_74110_b(int p_74110_1_, int p_74110_2_, float p_74110_3_) {
      int var4 = MathHelper.func_76128_c(this.field_74117_m + (this.field_74116_o - this.field_74117_m) * (double)p_74110_3_);
      int var5 = MathHelper.func_76128_c(this.field_74115_n + (this.field_74125_p - this.field_74115_n) * (double)p_74110_3_);
      if(var4 < field_74122_s) {
         var4 = field_74122_s;
      }

      if(var5 < field_74121_t) {
         var5 = field_74121_t;
      }

      if(var4 >= field_74120_u) {
         var4 = field_74120_u - 1;
      }

      if(var5 >= field_74119_v) {
         var5 = field_74119_v - 1;
      }

      int var6 = this.field_73882_e.field_71446_o.func_78341_b("/terrain.png");
      int var7 = this.field_73882_e.field_71446_o.func_78341_b("/achievement/bg.png");
      int var8 = (this.field_73880_f - this.field_74114_a) / 2;
      int var9 = (this.field_73881_g - this.field_74112_b) / 2;
      int var10 = var8 + 16;
      int var11 = var9 + 17;
      this.field_73735_i = 0.0F;
      GL11.glDepthFunc(518);
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, 0.0F, -200.0F);
      GL11.glEnable(3553);
      GL11.glDisable(2896);
      GL11.glEnable('\u803a');
      GL11.glEnable(2903);
      this.field_73882_e.field_71446_o.func_78342_b(var6);
      int var12 = var4 + 288 >> 4;
      int var13 = var5 + 288 >> 4;
      int var14 = (var4 + 288) % 16;
      int var15 = (var5 + 288) % 16;
      Random var21 = new Random();

      int var22;
      int var25;
      int var24;
      int var26;
      for(var22 = 0; var22 * 16 - var15 < 155; ++var22) {
         float var23 = 0.6F - (float)(var13 + var22) / 25.0F * 0.3F;
         GL11.glColor4f(var23, var23, var23, 1.0F);

         for(var24 = 0; var24 * 16 - var14 < 224; ++var24) {
            var21.setSeed((long)(1234 + var12 + var24));
            var21.nextInt();
            var25 = var21.nextInt(1 + var13 + var22) + (var13 + var22) / 2;
            var26 = Block.field_71939_E.field_72059_bZ;
            if(var25 <= 37 && var13 + var22 != 35) {
               if(var25 == 22) {
                  if(var21.nextInt(2) == 0) {
                     var26 = Block.field_72073_aw.field_72059_bZ;
                  } else {
                     var26 = Block.field_72047_aN.field_72059_bZ;
                  }
               } else if(var25 == 10) {
                  var26 = Block.field_71949_H.field_72059_bZ;
               } else if(var25 == 8) {
                  var26 = Block.field_71950_I.field_72059_bZ;
               } else if(var25 > 4) {
                  var26 = Block.field_71981_t.field_72059_bZ;
               } else if(var25 > 0) {
                  var26 = Block.field_71979_v.field_72059_bZ;
               }
            } else {
               var26 = Block.field_71986_z.field_72059_bZ;
            }

            this.func_73729_b(var10 + var24 * 16 - var14, var11 + var22 * 16 - var15, var26 % 16 << 4, var26 >> 4 << 4, 16, 16);
         }
      }

      GL11.glEnable(2929);
      GL11.glDepthFunc(515);
      GL11.glDisable(3553);

      int var27;
      int var30;
      for(var22 = 0; var22 < AchievementList.field_76007_e.size(); ++var22) {
         Achievement var33 = (Achievement)AchievementList.field_76007_e.get(var22);
         if(var33.field_75992_c != null) {
            var24 = var33.field_75993_a * 24 - var4 + 11 + var10;
            var25 = var33.field_75991_b * 24 - var5 + 11 + var11;
            var26 = var33.field_75992_c.field_75993_a * 24 - var4 + 11 + var10;
            var27 = var33.field_75992_c.field_75991_b * 24 - var5 + 11 + var11;
            boolean var28 = this.field_74126_x.func_77443_a(var33);
            boolean var29 = this.field_74126_x.func_77442_b(var33);
            var30 = Math.sin((double)(Minecraft.func_71386_F() % 600L) / 600.0D * 3.141592653589793D * 2.0D) > 0.6D?255:130;
            int var31 = -16777216;
            if(var28) {
               var31 = -9408400;
            } else if(var29) {
               var31 = '\uff00' + (var30 << 24);
            }

            this.func_73730_a(var24, var26, var25, var31);
            this.func_73728_b(var26, var25, var27, var31);
         }
      }

      Achievement var32 = null;
      RenderItem var37 = new RenderItem();
      RenderHelper.func_74520_c();
      GL11.glDisable(2896);
      GL11.glEnable('\u803a');
      GL11.glEnable(2903);

      int var42;
      int var41;
      for(var24 = 0; var24 < AchievementList.field_76007_e.size(); ++var24) {
         Achievement var35 = (Achievement)AchievementList.field_76007_e.get(var24);
         var26 = var35.field_75993_a * 24 - var4;
         var27 = var35.field_75991_b * 24 - var5;
         if(var26 >= -24 && var27 >= -24 && var26 <= 224 && var27 <= 155) {
            float var38;
            if(this.field_74126_x.func_77443_a(var35)) {
               var38 = 1.0F;
               GL11.glColor4f(var38, var38, var38, 1.0F);
            } else if(this.field_74126_x.func_77442_b(var35)) {
               var38 = Math.sin((double)(Minecraft.func_71386_F() % 600L) / 600.0D * 3.141592653589793D * 2.0D) < 0.6D?0.6F:0.8F;
               GL11.glColor4f(var38, var38, var38, 1.0F);
            } else {
               var38 = 0.3F;
               GL11.glColor4f(var38, var38, var38, 1.0F);
            }

            this.field_73882_e.field_71446_o.func_78342_b(var7);
            var42 = var10 + var26;
            var41 = var11 + var27;
            if(var35.func_75984_f()) {
               this.func_73729_b(var42 - 2, var41 - 2, 26, 202, 26, 26);
            } else {
               this.func_73729_b(var42 - 2, var41 - 2, 0, 202, 26, 26);
            }

            if(!this.field_74126_x.func_77442_b(var35)) {
               float var40 = 0.1F;
               GL11.glColor4f(var40, var40, var40, 1.0F);
               var37.field_77024_a = false;
            }

            GL11.glEnable(2896);
            GL11.glEnable(2884);
            var37.func_82406_b(this.field_73882_e.field_71466_p, this.field_73882_e.field_71446_o, var35.field_75990_d, var42 + 3, var41 + 3);
            GL11.glDisable(2896);
            if(!this.field_74126_x.func_77442_b(var35)) {
               var37.field_77024_a = true;
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if(p_74110_1_ >= var10 && p_74110_2_ >= var11 && p_74110_1_ < var10 + 224 && p_74110_2_ < var11 + 155 && p_74110_1_ >= var42 && p_74110_1_ <= var42 + 22 && p_74110_2_ >= var41 && p_74110_2_ <= var41 + 22) {
               var32 = var35;
            }
         }
      }

      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_73882_e.field_71446_o.func_78342_b(var7);
      this.func_73729_b(var8, var9, 0, 0, this.field_74114_a, this.field_74112_b);
      GL11.glPopMatrix();
      this.field_73735_i = 0.0F;
      GL11.glDepthFunc(515);
      GL11.glDisable(2929);
      GL11.glEnable(3553);
      super.func_73863_a(p_74110_1_, p_74110_2_, p_74110_3_);
      if(var32 != null) {
         String var34 = StatCollector.func_74838_a(var32.func_75970_i());
         String var36 = var32.func_75989_e();
         var26 = p_74110_1_ + 12;
         var27 = p_74110_2_ - 4;
         if(this.field_74126_x.func_77442_b(var32)) {
            var42 = Math.max(this.field_73886_k.func_78256_a(var34), 120);
            var41 = this.field_73886_k.func_78267_b(var36, var42);
            if(this.field_74126_x.func_77443_a(var32)) {
               var41 += 12;
            }

            this.func_73733_a(var26 - 3, var27 - 3, var26 + var42 + 3, var27 + var41 + 3 + 12, -1073741824, -1073741824);
            this.field_73886_k.func_78279_b(var36, var26, var27 + 12, var42, -6250336);
            if(this.field_74126_x.func_77443_a(var32)) {
               this.field_73886_k.func_78261_a(StatCollector.func_74838_a("achievement.taken"), var26, var27 + var41 + 4, -7302913);
            }
         } else {
            var42 = Math.max(this.field_73886_k.func_78256_a(var34), 120);
            String var39 = StatCollector.func_74837_a("achievement.requires", new Object[]{StatCollector.func_74838_a(var32.field_75992_c.func_75970_i())});
            var30 = this.field_73886_k.func_78267_b(var39, var42);
            this.func_73733_a(var26 - 3, var27 - 3, var26 + var42 + 3, var27 + var30 + 12 + 3, -1073741824, -1073741824);
            this.field_73886_k.func_78279_b(var39, var26, var27 + 12, var42, -9416624);
         }

         this.field_73886_k.func_78261_a(var34, var26, var27, this.field_74126_x.func_77442_b(var32)?(var32.func_75984_f()?-128:-1):(var32.func_75984_f()?-8355776:-8355712));
      }

      GL11.glEnable(2929);
      GL11.glEnable(2896);
      RenderHelper.func_74518_a();
   }

   public boolean func_73868_f() {
      return true;
   }

}
