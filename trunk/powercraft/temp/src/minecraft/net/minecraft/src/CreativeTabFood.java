package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

final class CreativeTabFood extends CreativeTabs {

   CreativeTabFood(int p_i3639_1_, String p_i3639_2_) {
      super(p_i3639_1_, p_i3639_2_);
   }

   @SideOnly(Side.CLIENT)
   public int func_78012_e() {
      return Item.field_77706_j.field_77779_bT;
   }
}
