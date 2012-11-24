package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.EnumOS;

@SideOnly(Side.CLIENT)
// $FF: synthetic class
public class EnumOSHelper {

   // $FF: synthetic field
   public static final int[] field_90049_a = new int[EnumOS.values().length];


   static {
      try {
         field_90049_a[EnumOS.LINUX.ordinal()] = 1;
      } catch (NoSuchFieldError var4) {
         ;
      }

      try {
         field_90049_a[EnumOS.SOLARIS.ordinal()] = 2;
      } catch (NoSuchFieldError var3) {
         ;
      }

      try {
         field_90049_a[EnumOS.WINDOWS.ordinal()] = 3;
      } catch (NoSuchFieldError var2) {
         ;
      }

      try {
         field_90049_a[EnumOS.MACOS.ordinal()] = 4;
      } catch (NoSuchFieldError var1) {
         ;
      }

   }
}
