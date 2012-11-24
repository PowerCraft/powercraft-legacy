package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import org.lwjgl.opengl.GLContext;

@SideOnly(Side.CLIENT)
public class OpenGlCapsChecker {

   public static boolean func_74371_a() {
      return GLContext.getCapabilities().GL_ARB_occlusion_query;
   }
}
