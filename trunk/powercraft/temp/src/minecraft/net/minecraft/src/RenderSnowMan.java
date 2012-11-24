package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntitySnowman;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModelSnowMan;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderLiving;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderSnowMan extends RenderLiving {

   private ModelSnowMan field_77094_a;


   public RenderSnowMan() {
      super(new ModelSnowMan(), 0.5F);
      this.field_77094_a = (ModelSnowMan)super.field_77045_g;
      this.func_77042_a(this.field_77094_a);
   }

   protected void func_77093_a(EntitySnowman p_77093_1_, float p_77093_2_) {
      super.func_77029_c(p_77093_1_, p_77093_2_);
      ItemStack var3 = new ItemStack(Block.field_72061_ba, 1);
      if(var3 != null && var3.func_77973_b().field_77779_bT < 256) {
         GL11.glPushMatrix();
         this.field_77094_a.field_78195_c.func_78794_c(0.0625F);
         if(RenderBlocks.func_78597_b(Block.field_71973_m[var3.field_77993_c].func_71857_b())) {
            float var4 = 0.625F;
            GL11.glTranslatef(0.0F, -0.34375F, 0.0F);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(var4, -var4, var4);
         }

         this.field_76990_c.field_78721_f.func_78443_a(p_77093_1_, var3, 0);
         GL11.glPopMatrix();
      }

   }

   // $FF: synthetic method
   protected void func_77029_c(EntityLiving p_77029_1_, float p_77029_2_) {
      this.func_77093_a((EntitySnowman)p_77029_1_, p_77029_2_);
   }
}
