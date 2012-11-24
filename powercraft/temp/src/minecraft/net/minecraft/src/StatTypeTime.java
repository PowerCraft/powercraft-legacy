package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.IStatType;
import net.minecraft.src.StatBase;

final class StatTypeTime implements IStatType {

   @SideOnly(Side.CLIENT)
   public String func_75843_a(int p_75843_1_) {
      double var2 = (double)p_75843_1_ / 20.0D;
      double var4 = var2 / 60.0D;
      double var6 = var4 / 60.0D;
      double var8 = var6 / 24.0D;
      double var10 = var8 / 365.0D;
      return var10 > 0.5D?StatBase.func_75969_k().format(var10) + " y":(var8 > 0.5D?StatBase.func_75969_k().format(var8) + " d":(var6 > 0.5D?StatBase.func_75969_k().format(var6) + " h":(var4 > 0.5D?StatBase.func_75969_k().format(var4) + " m":var2 + " s")));
   }
}
