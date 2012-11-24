package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.IStatType;
import net.minecraft.src.StatBase;

final class StatTypeDistance implements IStatType {

   @SideOnly(Side.CLIENT)
   public String func_75843_a(int p_75843_1_) {
      double var2 = (double)p_75843_1_ / 100.0D;
      double var4 = var2 / 1000.0D;
      return var4 > 0.5D?StatBase.func_75969_k().format(var4) + " km":(var2 > 0.5D?StatBase.func_75969_k().format(var2) + " m":p_75843_1_ + " cm");
   }
}
