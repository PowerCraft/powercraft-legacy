package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public interface IProgressUpdate {

   void func_73720_a(String var1);

   @SideOnly(Side.CLIENT)
   void func_73721_b(String var1);

   void func_73719_c(String var1);

   void func_73718_a(int var1);

   @SideOnly(Side.CLIENT)
   void func_73717_a();
}
