package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class CallableGLInfo implements Callable
{
    /** Gets Minecraft Open GL Info. */
    final Minecraft minecraftGLInfo;

    public CallableGLInfo(Minecraft par1Minecraft)
    {
        this.minecraftGLInfo = par1Minecraft;
    }

    public String func_74418_a()
    {
        return GL11.glGetString(GL11.GL_RENDERER) + " GL version " + GL11.glGetString(GL11.GL_VERSION) + ", " + GL11.glGetString(GL11.GL_VENDOR);
    }

    public Object call()
    {
        return this.func_74418_a();
    }
}
