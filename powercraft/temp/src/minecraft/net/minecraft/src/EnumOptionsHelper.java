package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.EnumOptions;

@SideOnly(Side.CLIENT)
// $FF: synthetic class
class EnumOptionsHelper {

   // $FF: synthetic field
   static final int[] field_74414_a = new int[EnumOptions.values().length];


   static {
      try {
         field_74414_a[EnumOptions.INVERT_MOUSE.ordinal()] = 1;
      } catch (NoSuchFieldError var15) {
         ;
      }

      try {
         field_74414_a[EnumOptions.VIEW_BOBBING.ordinal()] = 2;
      } catch (NoSuchFieldError var14) {
         ;
      }

      try {
         field_74414_a[EnumOptions.ANAGLYPH.ordinal()] = 3;
      } catch (NoSuchFieldError var13) {
         ;
      }

      try {
         field_74414_a[EnumOptions.ADVANCED_OPENGL.ordinal()] = 4;
      } catch (NoSuchFieldError var12) {
         ;
      }

      try {
         field_74414_a[EnumOptions.AMBIENT_OCCLUSION.ordinal()] = 5;
      } catch (NoSuchFieldError var11) {
         ;
      }

      try {
         field_74414_a[EnumOptions.RENDER_CLOUDS.ordinal()] = 6;
      } catch (NoSuchFieldError var10) {
         ;
      }

      try {
         field_74414_a[EnumOptions.CHAT_COLOR.ordinal()] = 7;
      } catch (NoSuchFieldError var9) {
         ;
      }

      try {
         field_74414_a[EnumOptions.CHAT_LINKS.ordinal()] = 8;
      } catch (NoSuchFieldError var8) {
         ;
      }

      try {
         field_74414_a[EnumOptions.CHAT_LINKS_PROMPT.ordinal()] = 9;
      } catch (NoSuchFieldError var7) {
         ;
      }

      try {
         field_74414_a[EnumOptions.USE_SERVER_TEXTURES.ordinal()] = 10;
      } catch (NoSuchFieldError var6) {
         ;
      }

      try {
         field_74414_a[EnumOptions.SNOOPER_ENABLED.ordinal()] = 11;
      } catch (NoSuchFieldError var5) {
         ;
      }

      try {
         field_74414_a[EnumOptions.USE_FULLSCREEN.ordinal()] = 12;
      } catch (NoSuchFieldError var4) {
         ;
      }

      try {
         field_74414_a[EnumOptions.ENABLE_VSYNC.ordinal()] = 13;
      } catch (NoSuchFieldError var3) {
         ;
      }

      try {
         field_74414_a[EnumOptions.SHOW_CAPE.ordinal()] = 14;
      } catch (NoSuchFieldError var2) {
         ;
      }

      try {
         field_74414_a[EnumOptions.TOUCHSCREEN.ordinal()] = 15;
      } catch (NoSuchFieldError var1) {
         ;
      }

   }
}
