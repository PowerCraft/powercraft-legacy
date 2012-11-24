package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

final class CreativeTabMaterial extends CreativeTabs {

   CreativeTabMaterial(int p_i3632_1_, String p_i3632_2_) {
      super(p_i3632_1_, p_i3632_2_);
   }

   @SideOnly(Side.CLIENT)
   public int func_78012_e() {
      return Item.field_77669_D.field_77779_bT;
   }
}
