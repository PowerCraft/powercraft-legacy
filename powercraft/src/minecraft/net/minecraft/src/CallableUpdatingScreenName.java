package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;

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
