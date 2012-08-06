package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;

public class CallableTexturePack implements Callable
{
    /** The Minecraft instance. */
    final Minecraft mc;

    public CallableTexturePack(Minecraft par1Minecraft)
    {
        this.mc = par1Minecraft;
    }

    public String func_79001_a()
    {
        return this.mc.gameSettings.skin;
    }

    public Object call()
    {
        return this.func_79001_a();
    }
}
