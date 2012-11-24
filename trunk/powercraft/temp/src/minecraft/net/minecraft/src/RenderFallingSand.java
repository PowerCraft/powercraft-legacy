package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.BlockAnvil;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Render;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderFallingSand extends Render {

   private RenderBlocks field_77004_a = new RenderBlocks();


   public RenderFallingSand() {
      this.field_76989_e = 0.5F;
   }

   public void func_77003_a(EntityFallingSand p_77003_1_, double p_77003_2_, double p_77003_4_, double p_77003_6_, float p_77003_8_, float p_77003_9_) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)p_77003_2_, (float)p_77003_4_, (float)p_77003_6_);
      this.func_76985_a("/terrain.png");
      Block var10 = Block.field_71973_m[p_77003_1_.field_70287_a];
      World var11 = p_77003_1_.func_70283_d();
      GL11.glDisable(2896);
      if(var10 instanceof BlockAnvil && var10.func_71857_b() == 35) {
         this.field_77004_a.field_78669_a = var11;
         Tessellator var12 = Tessellator.field_78398_a;
         var12.func_78382_b();
         var12.func_78373_b((double)((float)(-MathHelper.func_76128_c(p_77003_1_.field_70165_t)) - 0.5F), (double)((float)(-MathHelper.func_76128_c(p_77003_1_.field_70163_u)) - 0.5F), (double)((float)(-MathHelper.func_76128_c(p_77003_1_.field_70161_v)) - 0.5F));
         this.field_77004_a.func_85096_a((BlockAnvil)var10, MathHelper.func_76128_c(p_77003_1_.field_70165_t), MathHelper.func_76128_c(p_77003_1_.field_70163_u), MathHelper.func_76128_c(p_77003_1_.field_70161_v), p_77003_1_.field_70285_b);
         var12.func_78373_b(0.0D, 0.0D, 0.0D);
         var12.func_78381_a();
      } else if(var10 != null) {
         this.field_77004_a.func_83018_a(var10);
         this.field_77004_a.func_78588_a(var10, var11, MathHelper.func_76128_c(p_77003_1_.field_70165_t), MathHelper.func_76128_c(p_77003_1_.field_70163_u), MathHelper.func_76128_c(p_77003_1_.field_70161_v), p_77003_1_.field_70285_b);
      }

      GL11.glEnable(2896);
      GL11.glPopMatrix();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void func_76986_a(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      this.func_77003_a((EntityFallingSand)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }
}
