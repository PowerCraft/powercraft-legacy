package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Entity;
import net.minecraft.src.RenderManager;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityMobSpawner;
import net.minecraft.src.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer {

   public void func_76905_a(TileEntityMobSpawner p_76905_1_, double p_76905_2_, double p_76905_4_, double p_76905_6_, float p_76905_8_) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)p_76905_2_ + 0.5F, (float)p_76905_4_, (float)p_76905_6_ + 0.5F);
      Entity var9 = p_76905_1_.func_70382_c();
      if(var9 != null) {
         var9.func_70029_a(p_76905_1_.func_70314_l());
         float var10 = 0.4375F;
         GL11.glTranslatef(0.0F, 0.4F, 0.0F);
         GL11.glRotatef((float)(p_76905_1_.field_70393_c + (p_76905_1_.field_70392_b - p_76905_1_.field_70393_c) * (double)p_76905_8_) * 10.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
         GL11.glTranslatef(0.0F, -0.4F, 0.0F);
         GL11.glScalef(var10, var10, var10);
         var9.func_70012_b(p_76905_2_, p_76905_4_, p_76905_6_, 0.0F, 0.0F);
         RenderManager.field_78727_a.func_78719_a(var9, 0.0D, 0.0D, 0.0D, 0.0F, p_76905_8_);
      }

      GL11.glPopMatrix();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public void func_76894_a(TileEntity p_76894_1_, double p_76894_2_, double p_76894_4_, double p_76894_6_, float p_76894_8_) {
      this.func_76905_a((TileEntityMobSpawner)p_76894_1_, p_76894_2_, p_76894_4_, p_76894_6_, p_76894_8_);
   }
}
