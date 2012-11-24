package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

final class CreativeTabMisc extends CreativeTabs {

   CreativeTabMisc(int p_i3637_1_, String p_i3637_2_) {
      super(p_i3637_1_, p_i3637_2_);
   }

   @SideOnly(Side.CLIENT)
   public int func_78012_e() {
      return Item.field_77775_ay.field_77779_bT;
   }
}
