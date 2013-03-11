package net.minecraft.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.concurrent.Callable;

@SideOnly(Side.CLIENT)
public class CallableUpdatingScreenName implements Callable
{
    final Minecraft field_90055_a;

    public CallableUpdatingScreenName(Minecraft par1Minecraft)
    {
        this.field_90055_a = par1Minecraft;
    }

    public String func_90054_a()
    {
        return this.field_90055_a.currentScreen.getClass().getCanonicalName();
    }

    public Object call()
    {
        return this.func_90054_a();
    }
}
