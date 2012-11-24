package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityRenderer;
import net.minecraft.src.World;

@SideOnly(Side.CLIENT)
public abstract class TileEntitySpecialRenderer {

   protected TileEntityRenderer field_76898_b;


   public abstract void func_76894_a(TileEntity var1, double var2, double var4, double var6, float var8);

   protected void func_76897_a(String p_76897_1_) {
      RenderEngine var2 = this.field_76898_b.field_76960_e;
      if(var2 != null) {
         var2.func_78342_b(var2.func_78341_b(p_76897_1_));
      }

   }

   protected void func_82392_a(String p_82392_1_, String p_82392_2_) {
      RenderEngine var3 = this.field_76898_b.field_76960_e;
      if(var3 != null) {
         var3.func_78342_b(var3.func_78350_a(p_82392_1_, p_82392_2_));
      }

   }

   public void func_76893_a(TileEntityRenderer p_76893_1_) {
      this.field_76898_b = p_76893_1_;
   }

   public void func_76896_a(World p_76896_1_) {}

   public FontRenderer func_76895_b() {
      return this.field_76898_b.func_76954_a();
   }
}
