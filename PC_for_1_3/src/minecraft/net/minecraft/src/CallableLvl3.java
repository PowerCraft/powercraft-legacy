package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLvl3 implements Callable
{
    /** Gets loaded Entities. */
    final World worldLvl3;

    CallableLvl3(World par1World)
    {
        this.worldLvl3 = par1World;
    }

    public String func_77439_a()
    {
        return this.worldLvl3.chunkProvider.makeString();
    }

    public Object call()
    {
        return this.func_77439_a();
    }
}
