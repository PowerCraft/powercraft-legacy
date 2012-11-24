package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.concurrent.Callable;

@SideOnly(Side.CLIENT)
class CallableLastTickedParticle implements Callable
{
    final EntityFX field_90041_a;

    final EffectRenderer field_90040_b;

    CallableLastTickedParticle(EffectRenderer par1EffectRenderer, EntityFX par2EntityFX)
    {
        this.field_90040_b = par1EffectRenderer;
        this.field_90041_a = par2EntityFX;
    }

    public String func_90039_a()
    {
        return this.field_90041_a != null ? this.field_90041_a.toString() : null;
    }

    public Object call()
    {
        return this.func_90039_a();
    }
}
