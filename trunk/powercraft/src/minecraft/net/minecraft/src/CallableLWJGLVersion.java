package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;
import org.lwjgl.Sys;

@SideOnly(Side.CLIENT)
public class CallableLWJGLVersion implements Callable
{
    /** Gets Minecraft LWJGL version. */
    final Minecraft minecraftLWJGLVersion;

    public CallableLWJGLVersion(Minecraft par1Minecraft)
    {
        this.minecraftLWJGLVersion = par1Minecraft;
    }

    public String getLWJGLVersion()
    {
        return Sys.getVersion();
    }

    public Object call()
    {
        return this.getLWJGLVersion();
    }
}
