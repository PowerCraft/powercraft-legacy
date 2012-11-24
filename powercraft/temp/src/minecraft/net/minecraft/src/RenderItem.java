package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Render;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderItem extends Render {

   private RenderBlocks field_77022_g = new RenderBlocks();
   private Random field_77025_h = new Random();
   public boolean field_77024_a = true;
   public float field_77023_b = 0.0F;
   public static boolean field_82407_g = false;


   public RenderItem() {
      this.field_76989_e = 0.15F;
      this.field_76987_f = 0.75F;
   }

   public void func_77014_a(EntityItem p_77014_1_, double p_77014_2_, double p_77014_4_, double p_77014_6_, float p_77014_8_, float p_77014_9_) {
      this.field_77025_h.setSeed(187L);
      ItemStack var10 = p_77014_1_.field_70294_a;
      if(var10.func_77973_b() != null) {
         GL11.glPushMatrix();
         float var11 = MathHelper.func_76126_a(((float)p_77014_1_.field_70292_b + p_77014_9_) / 10.0F + p_77014_1_.field_70290_d) * 0.1F + 0.1F;
         float var12 = (((float)p_77014_1_.field_70292_b + p_77014_9_) / 20.0F + p_77014_1_.field_70290_d) * 57.295776F;
         byte var13 = 1;
         if(p_77014_1_.field_70294_a.field_77994_a > 1) {
            var13 = 2;
         }

         if(p_77014_1_.field_70294_a.field_77994_a > 5) {
            var13 = 3;
         }

         if(p_77014_1_.field_70294_a.field_77994_a > 20) {
            var13 = 4;
         }

         GL11.glTranslatef((float)p_77014_2_, (float)p_77014_4_ + var11, (float)p_77014_6_);
         GL11.glEnable('\u803a');
         Block var14 = Block.field_71973_m[var10.field_77993_c];
         int var16;
         float var19;
         float var20;
         float var24;
         if(var14 != null && RenderBlocks.func_78597_b(var14.func_71857_b())) {
            GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);
            if(field_82407_g) {
               GL11.glScalef(1.25F, 1.25F, 1.25F);
               GL11.glTranslatef(0.0F, 0.05F, 0.0F);
               GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            }

            this.func_76985_a("/terrain.png");
            float var22 = 0.25F;
            var16 = var14.func_71857_b();
            if(var16 == 1 || var16 == 19 || var16 == 12 || var16 == 2) {
               var22 = 0.5F;
            }

            GL11.glScalef(var22, var22, var22);

            for(int var23 = 0; var23 < var13; ++var23) {
               GL11.glPushMatrix();
               if(var23 > 0) {
                  var24 = (this.field_77025_h.nextFloat() * 2.0F - 1.0F) * 0.2F / var22;
                  var19 = (this.field_77025_h.nextFloat() * 2.0F - 1.0F) * 0.2F / var22;
                  var20 = (this.field_77025_h.nextFloat() * 2.0F - 1.0F) * 0.2F / var22;
                  GL11.glTranslatef(var24, var19, var20);
               }

               var24 = 1.0F;
               this.field_77022_g.func_78600_a(var14, var10.func_77960_j(), var24);
               GL11.glPopMatrix();
            }
         } else {
            int var15;
            float var17;
            if(var10.func_77973_b().func_77623_v()) {
               if(field_82407_g) {
                  GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                  GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                  GL11.glDisable(2896);
               } else {
                  GL11.glScalef(0.5F, 0.5F, 0.5F);
               }

               this.func_76985_a("/gui/items.png");

               for(var15 = 0; var15 <= 1; ++var15) {
                  var16 = var10.func_77973_b().func_77618_c(var10.func_77960_j(), var15);
                  var17 = 1.0F;
                  if(this.field_77024_a) {
                     int var18 = Item.field_77698_e[var10.field_77993_c].func_82790_a(var10, var15);
                     var19 = (float)(var18 >> 16 & 255) / 255.0F;
                     var20 = (float)(var18 >> 8 & 255) / 255.0F;
                     float var21 = (float)(var18 & 255) / 255.0F;
                     GL11.glColor4f(var19 * var17, var20 * var17, var21 * var17, 1.0F);
                  }

                  this.func_77020_a(var16, var13);
               }
            } else {
               if(field_82407_g) {
                  GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                  GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                  GL11.glDisable(2896);
               } else {
                  GL11.glScalef(0.5F, 0.5F, 0.5F);
               }

               var15 = var10.func_77954_c();
               if(var14 != null) {
                  this.func_76985_a("/terrain.png");
               } else {
                  this.func_76985_a("/gui/items.png");
               }

               if(this.field_77024_a) {
                  var16 = Item.field_77698_e[var10.field_77993_c].func_82790_a(var10, 0);
                  var17 = (float)(var16 >> 16 & 255) / 255.0F;
                  var24 = (float)(var16 >> 8 & 255) / 255.0F;
                  var19 = (float)(var16 & 255) / 255.0F;
                  var20 = 1.0F;
                  GL11.glColor4f(var17 * var20, var24 * var20, var19 * var20, 1.0F);
               }

               this.func_77020_a(var15, var13);
            }
         }

         GL11.glDisable('\u803a');
         GL11.glPopMatrix();
      }
   }

   private void func_77020_a(int p_77020_1_, int p_77020_2_) {
      Tessellator var3 = Tessellator.field_78398_a;
      float var4 = (float)(p_77020_1_ % 16 * 16 + 0) / 256.0F;
      float var5 = (float)(p_77020_1_ % 16 * 16 + 16) / 256.0F;
      float var6 = (float)(p_77020_1_ / 16 * 16 + 0) / 256.0F;
      float var7 = (float)(p_77020_1_ / 16 * 16 + 16) / 256.0F;
      float var8 = 1.0F;
      float var9 = 0.5F;
      float var10 = 0.25F;

      for(int var11 = 0; var11 < p_77020_2_; ++var11) {
         GL11.glPushMatrix();
         if(var11 > 0) {
            float var12 = (this.field_77025_h.nextFloat() * 2.0F - 1.0F) * 0.3F;
            float var13 = (this.field_77025_h.nextFloat() * 2.0F - 1.0F) * 0.3F;
            float var14 = (this.field_77025_h.nextFloat() * 2.0F - 1.0F) * 0.3F;
            GL11.glTranslatef(var12, var13, var14);
         }

         GL11.glRotatef(180.0F - this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
         var3.func_78382_b();
         var3.func_78375_b(0.0F, 1.0F, 0.0F);
         var3.func_78374_a((double)(0.0F - var9), (double)(0.0F - var10), 0.0D, (double)var4, (double)var7);
         var3.func_78374_a((double)(var8 - var9), (double)(0.0F - var10), 0.0D, (double)var5, (double)var7);
         var3.func_78374_a((double)(var8 - var9), (double)(1.0F - var10), 0.0D, (double)var5, (double)var6);
         var3.func_78374_a((double)(0.0F - var9), (double)(1.0F - var10), 0.0D, (double)var4, (double)var6);
         var3.func_78381_a();
         GL11.glPopMatrix();
      }

   }

   public void func_77015_a(FontRenderer p_77015_1_, RenderEngine p_77015_2_, ItemStack p_77015_3_, int p_77015_4_, int p_77015_5_) {
      int var6 = p_77015_3_.field_77993_c;
      int var7 = p_77015_3_.func_77960_j();
      int var8 = p_77015_3_.func_77954_c();
      int var10;
      float var12;
      float var13;
      float var16;
      if(var6 < 256 && RenderBlocks.func_78597_b(Block.field_71973_m[var6].func_71857_b())) {
         p_77015_2_.func_78342_b(p_77015_2_.func_78341_b("/terrain.png"));
         Block var15 = Block.field_71973_m[var6];
         GL11.glPushMatrix();
         GL11.glTranslatef((float)(p_77015_4_ - 2), (float)(p_77015_5_ + 3), -3.0F + this.field_77023_b);
         GL11.glScalef(10.0F, 10.0F, 10.0F);
         GL11.glTranslatef(1.0F, 0.5F, 1.0F);
         GL11.glScalef(1.0F, 1.0F, -1.0F);
         GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         var10 = Item.field_77698_e[var6].func_82790_a(p_77015_3_, 0);
         var16 = (float)(var10 >> 16 & 255) / 255.0F;
         var12 = (float)(var10 >> 8 & 255) / 255.0F;
         var13 = (float)(var10 & 255) / 255.0F;
         if(this.field_77024_a) {
            GL11.glColor4f(var16, var12, var13, 1.0F);
         }

         GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
         this.field_77022_g.field_78668_c = this.field_77024_a;
         this.field_77022_g.func_78600_a(var15, var7, 1.0F);
         this.field_77022_g.field_78668_c = true;
         GL11.glPopMatrix();
      } else {
         int var9;
         if(Item.field_77698_e[var6].func_77623_v()) {
            GL11.glDisable(2896);
            p_77015_2_.func_78342_b(p_77015_2_.func_78341_b("/gui/items.png"));

            for(var9 = 0; var9 <= 1; ++var9) {
               var10 = Item.field_77698_e[var6].func_77618_c(var7, var9);
               int var11 = Item.field_77698_e[var6].func_82790_a(p_77015_3_, var9);
               var12 = (float)(var11 >> 16 & 255) / 255.0F;
               var13 = (float)(var11 >> 8 & 255) / 255.0F;
               float var14 = (float)(var11 & 255) / 255.0F;
               if(this.field_77024_a) {
                  GL11.glColor4f(var12, var13, var14, 1.0F);
               }

               this.func_77016_a(p_77015_4_, p_77015_5_, var10 % 16 * 16, var10 / 16 * 16, 16, 16);
            }

            GL11.glEnable(2896);
         } else if(var8 >= 0) {
            GL11.glDisable(2896);
            if(var6 < 256) {
               p_77015_2_.func_78342_b(p_77015_2_.func_78341_b("/terrain.png"));
            } else {
               p_77015_2_.func_78342_b(p_77015_2_.func_78341_b("/gui/items.png"));
            }

            var9 = Item.field_77698_e[var6].func_82790_a(p_77015_3_, 0);
            float var17 = (float)(var9 >> 16 & 255) / 255.0F;
            var16 = (float)(var9 >> 8 & 255) / 255.0F;
            var12 = (float)(var9 & 255) / 255.0F;
            if(this.field_77024_a) {
               GL11.glColor4f(var17, var16, var12, 1.0F);
            }

            this.func_77016_a(p_77015_4_, p_77015_5_, var8 % 16 * 16, var8 / 16 * 16, 16, 16);
            GL11.glEnable(2896);
         }
      }

      GL11.glEnable(2884);
   }

   public void func_82406_b(FontRenderer p_82406_1_, RenderEngine p_82406_2_, ItemStack p_82406_3_, int p_82406_4_, int p_82406_5_) {
      if(p_82406_3_ != null) {
         this.func_77015_a(p_82406_1_, p_82406_2_, p_82406_3_, p_82406_4_, p_82406_5_);
         if(p_82406_3_ != null && p_82406_3_.func_77962_s()) {
            GL11.glDepthFunc(516);
            GL11.glDisable(2896);
            GL11.glDepthMask(false);
            p_82406_2_.func_78342_b(p_82406_2_.func_78341_b("%blur%/misc/glint.png"));
            this.field_77023_b -= 50.0F;
            GL11.glEnable(3042);
            GL11.glBlendFunc(774, 774);
            GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
            this.func_77018_a(p_82406_4_ * 431278612 + p_82406_5_ * 32178161, p_82406_4_ - 2, p_82406_5_ - 2, 20, 20);
            GL11.glDisable(3042);
            GL11.glDepthMask(true);
            this.field_77023_b += 50.0F;
            GL11.glEnable(2896);
            GL11.glDepthFunc(515);
         }

      }
   }

   private void func_77018_a(int p_77018_1_, int p_77018_2_, int p_77018_3_, int p_77018_4_, int p_77018_5_) {
      for(int var6 = 0; var6 < 2; ++var6) {
         if(var6 == 0) {
            GL11.glBlendFunc(768, 1);
         }

         if(var6 == 1) {
            GL11.glBlendFunc(768, 1);
         }

         float var7 = 0.00390625F;
         float var8 = 0.00390625F;
         float var9 = (float)(Minecraft.func_71386_F() % (long)(3000 + var6 * 1873)) / (3000.0F + (float)(var6 * 1873)) * 256.0F;
         float var10 = 0.0F;
         Tessellator var11 = Tessellator.field_78398_a;
         float var12 = 4.0F;
         if(var6 == 1) {
            var12 = -1.0F;
         }

         var11.func_78382_b();
         var11.func_78374_a((double)(p_77018_2_ + 0), (double)(p_77018_3_ + p_77018_5_), (double)this.field_77023_b, (double)((var9 + (float)p_77018_5_ * var12) * var7), (double)((var10 + (float)p_77018_5_) * var8));
         var11.func_78374_a((double)(p_77018_2_ + p_77018_4_), (double)(p_77018_3_ + p_77018_5_), (double)this.field_77023_b, (double)((var9 + (float)p_77018_4_ + (float)p_77018_5_ * var12) * var7), (double)((var10 + (float)p_77018_5_) * var8));
         var11.func_78374_a((double)(p_77018_2_ + p_77018_4_), (double)(p_77018_3_ + 0), (double)this.field_77023_b, (double)((var9 + (float)p_77018_4_) * var7), (double)((var10 + 0.0F) * var8));
         var11.func_78374_a((double)(p_77018_2_ + 0), (double)(p_77018_3_ + 0), (double)this.field_77023_b, (double)((var9 + 0.0F) * var7), (double)((var10 + 0.0F) * var8));
         var11.func_78381_a();
      }

   }

   public void func_77021_b(FontRenderer p_77021_1_, RenderEngine p_77021_2_, ItemStack p_77021_3_, int p_77021_4_, int p_77021_5_) {
      if(p_77021_3_ != null) {
         if(p_77021_3_.field_77994_a > 1) {
            String var6 = "" + p_77021_3_.field_77994_a;
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            p_77021_1_.func_78261_a(var6, p_77021_4_ + 19 - 2 - p_77021_1_.func_78256_a(var6), p_77021_5_ + 6 + 3, 16777215);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
         }

         if(p_77021_3_.func_77951_h()) {
            int var11 = (int)Math.round(13.0D - (double)p_77021_3_.func_77952_i() * 13.0D / (double)p_77021_3_.func_77958_k());
            int var7 = (int)Math.round(255.0D - (double)p_77021_3_.func_77952_i() * 255.0D / (double)p_77021_3_.func_77958_k());
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glDisable(3553);
            Tessellator var8 = Tessellator.field_78398_a;
            int var9 = 255 - var7 << 16 | var7 << 8;
            int var10 = (255 - var7) / 4 << 16 | 16128;
            this.func_77017_a(var8, p_77021_4_ + 2, p_77021_5_ + 13, 13, 2, 0);
            this.func_77017_a(var8, p_77021_4_ + 2, p_77021_5_ + 13, 12, 1, var10);
            this.func_77017_a(var8, p_77021_4_ + 2, p_77021_5_ + 13, var11, 1, var9);
            GL11.glEnable(3553);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         }

      }
   }

   private void func_77017_a(Tessellator p_77017_1_, int p_77017_2_, int p_77017_3_, int p_77017_4_, int p_77017_5_, int p_77017_6_) {
      p_77017_1_.func_78382_b();
      p_77017_1_.func_78378_d(p_77017_6_);
      p_77017_1_.func_78377_a((double)(p_77017_2_ + 0), (double)(p_77017_3_ + 0), 0.0D);
      p_77017_1_.func_78377_a((double)(p_77017_2_ + 0), (double)(p_77017_3_ + p_77017_5_), 0.0D);
      p_77017_1_.func_78377_a((double)(p_77017_2_ + p_77017_4_), (double)(p_77017_3_ + p_77017_5_), 0.0D);
      p_77017_1_.func_78377_a((double)(p_77017_2_ + p_77017_4_), (double)(p_77017_3_ + 0), 0.0D);
      p_77017_1_.func_78381_a();
   }

   public void func_77016_a(int p_77016_1_, int p_77016_2_, int p_77016_3_, int p_77016_4_, int p_77016_5_, int p_77016_6_) {
      float var7 = 0.00390625F;
      float var8 = 0.00390625F;
      Tessellator var9 = Tessellator.field_78398_a;
      var9.func_78382_b();
      var9.func_78374_a((double)(p_77016_1_ + 0), (double)(p_77016_2_ + p_77016_6_), (double)this.field_77023_b, (double)((float)(p_77016_3_ + 0) * var7), (double)((float)(p_77016_4_ + p_77016_6_) * var8));
      var9.func_78374_a((double)(p_77016_1_ + p_77016_5_), (double)(p_77016_2_ + p_77016_6_), (double)this.field_77023_b, (double)((float)(p_77016_3_ + p_77016_5_) * var7), (double)((float)(p_77016_4_ + p_77016_6_) * var8));
      var9.func_78374_a((double)(p_77016_1_ + p_77016_5_), (double)(p_77016_2_ + 0), (double)this.field_77023_b, (double)((float)(p_77016_3_ + p_77016_5_) * var7), (double)((float)(p_77016_4_ + 0) * var8));
      var9.func_78374_a((double)(p_77016_1_ + 0), (double)(p_77016_2_ + 0), (double)this.field_77023_b, (double)((float)(p_77016_3_ + 0) * var7), (double)((float)(p_77016_4_ + 0) * var8));
      var9.func_78381_a();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void func_76986_a(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      this.func_77014_a((EntityItem)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }

}
