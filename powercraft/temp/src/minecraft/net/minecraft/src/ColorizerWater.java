package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class ColorizerWater {

   private static int[] field_76915_a = new int[65536];


   public static void func_76914_a(int[] p_76914_0_) {
      field_76915_a = p_76914_0_;
   }

}
