package argo.format;

import argo.jdom.JsonNodeType;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
// $FF: synthetic class
class CompactJsonFormatter_JsonNodeType {

   // $FF: synthetic field
   static final int[] field_74790_a = new int[JsonNodeType.values().length];


   static {
      try {
         field_74790_a[JsonNodeType.ARRAY.ordinal()] = 1;
      } catch (NoSuchFieldError var7) {
         ;
      }

      try {
         field_74790_a[JsonNodeType.OBJECT.ordinal()] = 2;
      } catch (NoSuchFieldError var6) {
         ;
      }

      try {
         field_74790_a[JsonNodeType.STRING.ordinal()] = 3;
      } catch (NoSuchFieldError var5) {
         ;
      }

      try {
         field_74790_a[JsonNodeType.NUMBER.ordinal()] = 4;
      } catch (NoSuchFieldError var4) {
         ;
      }

      try {
         field_74790_a[JsonNodeType.FALSE.ordinal()] = 5;
      } catch (NoSuchFieldError var3) {
         ;
      }

      try {
         field_74790_a[JsonNodeType.TRUE.ordinal()] = 6;
      } catch (NoSuchFieldError var2) {
         ;
      }

      try {
         field_74790_a[JsonNodeType.NULL.ordinal()] = 7;
      } catch (NoSuchFieldError var1) {
         ;
      }

   }
}
