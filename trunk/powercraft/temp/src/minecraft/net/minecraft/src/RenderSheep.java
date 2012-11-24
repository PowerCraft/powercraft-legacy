package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.ModelBase;
import net.minecraft.src.RenderLiving;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderSheep extends RenderLiving {

   public RenderSheep(ModelBase p_i3209_1_, ModelBase p_i3209_2_, float p_i3209_3_) {
      super(p_i3209_1_, p_i3209_3_);
      this.func_77042_a(p_i3209_2_);
   }

   protected int func_77113_a(EntitySheep p_77113_1_, int p_77113_2_, float p_77113_3_) {
      if(p_77113_2_ == 0 && !p_77113_1_.func_70892_o()) {
         this.func_76985_a("/mob/sheep_fur.png");
         float var4 = 1.0F;
         int var5 = p_77113_1_.func_70896_n();
         GL11.glColor3f(var4 * EntitySheep.field_70898_d[var5][0], var4 * EntitySheep.field_70898_d[var5][1], var4 * EntitySheep.field_70898_d[var5][2]);
         return 1;
      } else {
         return -1;
      }
   }

   // $FF: synthetic method
   // $FF: bridge method
   protected int func_77032_a(EntityLiving p_77032_1_, int p_77032_2_, float p_77032_3_) {
      return this.func_77113_a((EntitySheep)p_77032_1_, p_77032_2_, p_77032_3_);
   }
}
