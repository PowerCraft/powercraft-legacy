package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableScreenName implements Callable
{
    final EntityRenderer field_90032_a;

    CallableScreenName(EntityRenderer par1EntityRenderer)
    {
        this.field_90032_a = par1EntityRenderer;
    }

    public String func_90031_a()
    {
        return EntityRenderer.getRendererMinecraft(this.field_90032_a).currentScreen.getClass().getCanonicalName();
    }

    public Object call()
    {
        return this.func_90031_a();
    }
}
