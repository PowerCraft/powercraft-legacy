package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import org.lwjgl.opengl.GLContext;

@SideOnly(Side.CLIENT)
public class OpenGlCapsChecker
{
    /**
     * Checks if we support OpenGL occlusion.
     */
    public static boolean checkARBOcclusion()
    {
        return GLContext.getCapabilities().GL_ARB_occlusion_query;
    }
}
