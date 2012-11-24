package argo.saj;

import argo.saj.ThingWithPosition;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public final class InvalidSyntaxException extends Exception {

   private final int field_74665_a;
   private final int field_74664_b;


   InvalidSyntaxException(String p_i3241_1_, ThingWithPosition p_i3241_2_) {
      super("At line " + p_i3241_2_.func_74561_c() + ", column " + p_i3241_2_.func_74562_b() + ":  " + p_i3241_1_);
      this.field_74665_a = p_i3241_2_.func_74562_b();
      this.field_74664_b = p_i3241_2_.func_74561_c();
   }

   InvalidSyntaxException(String p_i3242_1_, Throwable p_i3242_2_, ThingWithPosition p_i3242_3_) {
      super("At line " + p_i3242_3_.func_74561_c() + ", column " + p_i3242_3_.func_74562_b() + ":  " + p_i3242_1_, p_i3242_2_);
      this.field_74665_a = p_i3242_3_.func_74562_b();
      this.field_74664_b = p_i3242_3_.func_74561_c();
   }
}
