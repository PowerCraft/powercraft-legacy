package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

final class CreativeTabCombat extends CreativeTabs {

   CreativeTabCombat(int p_i3641_1_, String p_i3641_2_) {
      super(p_i3641_1_, p_i3641_2_);
   }

   @SideOnly(Side.CLIENT)
   public int func_78012_e() {
      return Item.field_77672_G.field_77779_bT;
   }
}
