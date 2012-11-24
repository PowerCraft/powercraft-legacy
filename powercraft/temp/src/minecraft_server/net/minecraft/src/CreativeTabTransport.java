package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;

final class CreativeTabTransport extends CreativeTabs {

   CreativeTabTransport(int p_i3636_1_, String p_i3636_2_) {
      super(p_i3636_1_, p_i3636_2_);
   }

   @SideOnly(Side.CLIENT)
   public int func_78012_e() {
      return Block.field_71954_T.field_71990_ca;
   }
}
