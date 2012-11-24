package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.io.InputStream;
import net.minecraft.src.RenderEngine;

@SideOnly(Side.CLIENT)
public interface ITexturePack {

   void func_77533_a(RenderEngine var1);

   void func_77535_b(RenderEngine var1);

   InputStream func_77532_a(String var1);

   String func_77536_b();

   String func_77538_c();

   String func_77531_d();

   String func_77537_e();

   int func_77534_f();
}
