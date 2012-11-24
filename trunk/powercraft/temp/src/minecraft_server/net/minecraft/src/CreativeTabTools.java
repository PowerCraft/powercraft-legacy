package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

final class CreativeTabTools extends CreativeTabs {

   CreativeTabTools(int p_i3640_1_, String p_i3640_2_) {
      super(p_i3640_1_, p_i3640_2_);
   }

   @SideOnly(Side.CLIENT)
   public int func_78012_e() {
      return Item.field_77708_h.field_77779_bT;
   }
}
