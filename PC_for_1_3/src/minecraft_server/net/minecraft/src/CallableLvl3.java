package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLvl3 implements Callable
{
    final World field_77440_a;

    CallableLvl3(World par1World)
    {
        this.field_77440_a = par1World;
    }

    public String func_77439_a()
    {
        return this.field_77440_a.chunkProvider.func_73148_d();
    }

    public Object call()
    {
        return this.func_77439_a();
    }
}
