package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.ModelSkeleton;
import net.minecraft.src.RenderBiped;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderSkeleton extends RenderBiped {

   public RenderSkeleton() {
      super(new ModelSkeleton(), 0.5F);
   }

   protected void func_82438_a(EntitySkeleton p_82438_1_, float p_82438_2_) {
      if(p_82438_1_.func_82202_m() == 1) {
         GL11.glScalef(1.2F, 1.2F, 1.2F);
      }

   }

   protected void func_82422_c() {
      GL11.glTranslatef(0.09375F, 0.1875F, 0.0F);
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected void func_77041_b(EntityLiving p_77041_1_, float p_77041_2_) {
      this.func_82438_a((EntitySkeleton)p_77041_1_, p_77041_2_);
   }
}
