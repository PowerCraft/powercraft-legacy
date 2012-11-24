package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPotion;
import net.minecraft.src.PotionHelper;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderSnowball extends Render {

   private int field_77027_a;


   public RenderSnowball(int p_i3208_1_) {
      this.field_77027_a = p_i3208_1_;
   }

   public void func_76986_a(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
      GL11.glEnable('\u803a');
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      this.func_76985_a("/gui/items.png");
      Tessellator var10 = Tessellator.field_78398_a;
      if(this.field_77027_a == 154) {
         int var11 = PotionHelper.func_77915_a(((EntityPotion)p_76986_1_).func_70196_i(), false);
         float var12 = (float)(var11 >> 16 & 255) / 255.0F;
         float var13 = (float)(var11 >> 8 & 255) / 255.0F;
         float var14 = (float)(var11 & 255) / 255.0F;
         GL11.glColor3f(var12, var13, var14);
         GL11.glPushMatrix();
         this.func_77026_a(var10, 141);
         GL11.glPopMatrix();
         GL11.glColor3f(1.0F, 1.0F, 1.0F);
      }

      this.func_77026_a(var10, this.field_77027_a);
      GL11.glDisable('\u803a');
      GL11.glPopMatrix();
   }

   private void func_77026_a(Tessellator p_77026_1_, int p_77026_2_) {
      float var3 = (float)(p_77026_2_ % 16 * 16 + 0) / 256.0F;
      float var4 = (float)(p_77026_2_ % 16 * 16 + 16) / 256.0F;
      float var5 = (float)(p_77026_2_ / 16 * 16 + 0) / 256.0F;
      float var6 = (float)(p_77026_2_ / 16 * 16 + 16) / 256.0F;
      float var7 = 1.0F;
      float var8 = 0.5F;
      float var9 = 0.25F;
      GL11.glRotatef(180.0F - this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
      p_77026_1_.func_78382_b();
      p_77026_1_.func_78375_b(0.0F, 1.0F, 0.0F);
      p_77026_1_.func_78374_a((double)(0.0F - var8), (double)(0.0F - var9), 0.0D, (double)var3, (double)var6);
      p_77026_1_.func_78374_a((double)(var7 - var8), (double)(0.0F - var9), 0.0D, (double)var4, (double)var6);
      p_77026_1_.func_78374_a((double)(var7 - var8), (double)(var7 - var9), 0.0D, (double)var4, (double)var5);
      p_77026_1_.func_78374_a((double)(0.0F - var8), (double)(var7 - var9), 0.0D, (double)var3, (double)var5);
      p_77026_1_.func_78381_a();
   }
}
