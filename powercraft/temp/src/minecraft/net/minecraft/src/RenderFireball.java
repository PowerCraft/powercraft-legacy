package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFireball;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderFireball extends Render {

   private float field_77002_a;


   public RenderFireball(float p_i3204_1_) {
      this.field_77002_a = p_i3204_1_;
   }

   public void func_77001_a(EntityFireball p_77001_1_, double p_77001_2_, double p_77001_4_, double p_77001_6_, float p_77001_8_, float p_77001_9_) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)p_77001_2_, (float)p_77001_4_, (float)p_77001_6_);
      GL11.glEnable('\u803a');
      float var10 = this.field_77002_a;
      GL11.glScalef(var10 / 1.0F, var10 / 1.0F, var10 / 1.0F);
      byte var11 = 46;
      this.func_76985_a("/gui/items.png");
      Tessellator var12 = Tessellator.field_78398_a;
      float var13 = (float)(var11 % 16 * 16 + 0) / 256.0F;
      float var14 = (float)(var11 % 16 * 16 + 16) / 256.0F;
      float var15 = (float)(var11 / 16 * 16 + 0) / 256.0F;
      float var16 = (float)(var11 / 16 * 16 + 16) / 256.0F;
      float var17 = 1.0F;
      float var18 = 0.5F;
      float var19 = 0.25F;
      GL11.glRotatef(180.0F - this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
      var12.func_78382_b();
      var12.func_78375_b(0.0F, 1.0F, 0.0F);
      var12.func_78374_a((double)(0.0F - var18), (double)(0.0F - var19), 0.0D, (double)var13, (double)var16);
      var12.func_78374_a((double)(var17 - var18), (double)(0.0F - var19), 0.0D, (double)var14, (double)var16);
      var12.func_78374_a((double)(var17 - var18), (double)(1.0F - var19), 0.0D, (double)var14, (double)var15);
      var12.func_78374_a((double)(0.0F - var18), (double)(1.0F - var19), 0.0D, (double)var13, (double)var15);
      var12.func_78381_a();
      GL11.glDisable('\u803a');
      GL11.glPopMatrix();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void func_76986_a(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      this.func_77001_a((EntityFireball)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }
}
