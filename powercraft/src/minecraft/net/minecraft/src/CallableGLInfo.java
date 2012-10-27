package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class CallableGLInfo implements Callable
{
    /** Gets Minecraft Open GL Info. */
    final Minecraft minecraftGLInfo;

    public CallableGLInfo(Minecraft par1Minecraft)
    {
        this.minecraftGLInfo = par1Minecraft;
    }

    /**
     * Gets what OpenGL version you have.
     */
    public String getGLVersion()
    {
        return GL11.glGetString(GL11.GL_RENDERER) + " GL version " + GL11.glGetString(GL11.GL_VERSION) + ", " + GL11.glGetString(GL11.GL_VENDOR);
    }

    public Object call()
    {
        return this.getGLVersion();
    }
}
