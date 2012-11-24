package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class CallableTickingScreenName implements Callable
{
    /** Reference to the Minecraft object. */
    final Minecraft mc;

    public CallableTickingScreenName(Minecraft par1Minecraft)
    {
        this.mc = par1Minecraft;
    }

    public String getLWJGLVersion()
    {
        return this.mc.currentScreen.getClass().getCanonicalName();
    }

    public Object call()
    {
        return this.getLWJGLVersion();
    }
}
