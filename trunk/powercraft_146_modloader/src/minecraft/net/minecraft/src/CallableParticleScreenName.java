package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;

public class CallableParticleScreenName implements Callable
{
    final Minecraft field_90053_a;

    public CallableParticleScreenName(Minecraft par1Minecraft)
    {
        this.field_90053_a = par1Minecraft;
    }

    public String func_90052_a()
    {
        return this.field_90053_a.currentScreen.getClass().getCanonicalName();
    }

    public Object call()
    {
        return this.func_90052_a();
    }
}
