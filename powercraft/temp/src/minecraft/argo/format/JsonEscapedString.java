package argo.format;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
final class JsonEscapedString {

   private final String field_74791_a;


   JsonEscapedString(String p_i3220_1_) {
      this.field_74791_a = p_i3220_1_.replace("\\", "\\\\").replace("\"", "\\\"").replace("\b", "\\b").replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
   }

   public String toString() {
      return this.field_74791_a;
   }
}
