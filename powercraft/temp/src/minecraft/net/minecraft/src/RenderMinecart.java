package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelMinecart;
import net.minecraft.src.Render;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderMinecart extends Render {

   protected ModelBase field_77013_a;


   public RenderMinecart() {
      this.field_76989_e = 0.5F;
      this.field_77013_a = new ModelMinecart();
   }

   public void func_77012_a(EntityMinecart p_77012_1_, double p_77012_2_, double p_77012_4_, double p_77012_6_, float p_77012_8_, float p_77012_9_) {
      GL11.glPushMatrix();
      long var10 = (long)p_77012_1_.field_70157_k * 493286711L;
      var10 = var10 * var10 * 4392167121L + var10 * 98761L;
      float var12 = (((float)(var10 >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
      float var13 = (((float)(var10 >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
      float var14 = (((float)(var10 >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
      GL11.glTranslatef(var12, var13, var14);
      double var15 = p_77012_1_.field_70142_S + (p_77012_1_.field_70165_t - p_77012_1_.field_70142_S) * (double)p_77012_9_;
      double var17 = p_77012_1_.field_70137_T + (p_77012_1_.field_70163_u - p_77012_1_.field_70137_T) * (double)p_77012_9_;
      double var19 = p_77012_1_.field_70136_U + (p_77012_1_.field_70161_v - p_77012_1_.field_70136_U) * (double)p_77012_9_;
      double var21 = 0.30000001192092896D;
      Vec3 var23 = p_77012_1_.func_70489_a(var15, var17, var19);
      float var24 = p_77012_1_.field_70127_C + (p_77012_1_.field_70125_A - p_77012_1_.field_70127_C) * p_77012_9_;
      if(var23 != null) {
         Vec3 var25 = p_77012_1_.func_70495_a(var15, var17, var19, var21);
         Vec3 var26 = p_77012_1_.func_70495_a(var15, var17, var19, -var21);
         if(var25 == null) {
            var25 = var23;
         }

         if(var26 == null) {
            var26 = var23;
         }

         p_77012_2_ += var23.field_72450_a - var15;
         p_77012_4_ += (var25.field_72448_b + var26.field_72448_b) / 2.0D - var17;
         p_77012_6_ += var23.field_72449_c - var19;
         Vec3 var27 = var26.func_72441_c(-var25.field_72450_a, -var25.field_72448_b, -var25.field_72449_c);
         if(var27.func_72433_c() != 0.0D) {
            var27 = var27.func_72432_b();
            p_77012_8_ = (float)(Math.atan2(var27.field_72449_c, var27.field_72450_a) * 180.0D / 3.141592653589793D);
            var24 = (float)(Math.atan(var27.field_72448_b) * 73.0D);
         }
      }

      GL11.glTranslatef((float)p_77012_2_, (float)p_77012_4_, (float)p_77012_6_);
      GL11.glRotatef(180.0F - p_77012_8_, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
      float var28 = (float)p_77012_1_.func_70496_j() - p_77012_9_;
      float var30 = (float)p_77012_1_.func_70491_i() - p_77012_9_;
      if(var30 < 0.0F) {
         var30 = 0.0F;
      }

      if(var28 > 0.0F) {
         GL11.glRotatef(MathHelper.func_76126_a(var28) * var28 * var30 / 10.0F * (float)p_77012_1_.func_70493_k(), 1.0F, 0.0F, 0.0F);
      }

      if(p_77012_1_.field_70505_a != 0) {
         this.func_76985_a("/terrain.png");
         float var29 = 0.75F;
         GL11.glScalef(var29, var29, var29);
         if(p_77012_1_.field_70505_a == 1) {
            GL11.glTranslatef(0.0F, 0.5F, 0.0F);
            (new RenderBlocks()).func_78600_a(Block.field_72077_au, 0, p_77012_1_.func_70013_c(p_77012_9_));
            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.5F, 0.0F, -0.5F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         } else if(p_77012_1_.field_70505_a == 2) {
            GL11.glTranslatef(0.0F, 0.3125F, 0.0F);
            (new RenderBlocks()).func_78600_a(Block.field_72051_aB, 0, p_77012_1_.func_70013_c(p_77012_9_));
            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.3125F, 0.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         }

         GL11.glScalef(1.0F / var29, 1.0F / var29, 1.0F / var29);
      }

      this.func_76985_a("/item/cart.png");
      GL11.glScalef(-1.0F, -1.0F, 1.0F);
      this.field_77013_a.func_78088_a(p_77012_1_, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
      GL11.glPopMatrix();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void func_76986_a(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      this.func_77012_a((EntityMinecart)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }
}
