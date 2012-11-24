package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

final class CreativeTabRedstone extends CreativeTabs {

   CreativeTabRedstone(int p_i3635_1_, String p_i3635_2_) {
      super(p_i3635_1_, p_i3635_2_);
   }

   @SideOnly(Side.CLIENT)
   public int func_78012_e() {
      return Item.field_77767_aC.field_77779_bT;
   }
}
