package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPlayerInfo {

   public final String field_78831_a;
   private final String field_78830_c;
   public int field_78829_b;


   public GuiPlayerInfo(String p_i3109_1_) {
      this.field_78831_a = p_i3109_1_;
      this.field_78830_c = p_i3109_1_.toLowerCase();
   }
}
