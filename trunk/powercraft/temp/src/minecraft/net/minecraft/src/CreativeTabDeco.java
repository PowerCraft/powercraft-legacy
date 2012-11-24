package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;

final class CreativeTabDeco extends CreativeTabs {

   CreativeTabDeco(int p_i3634_1_, String p_i3634_2_) {
      super(p_i3634_1_, p_i3634_2_);
   }

   @SideOnly(Side.CLIENT)
   public int func_78012_e() {
      return Block.field_72107_ae.field_71990_ca;
   }
}
