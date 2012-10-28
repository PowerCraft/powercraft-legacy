package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableTagCompound2 implements Callable
{
    final int field_82588_a;

    final NBTTagCompound field_82587_b;

    CallableTagCompound2(NBTTagCompound par1NBTTagCompound, int par2)
    {
        this.field_82587_b = par1NBTTagCompound;
        this.field_82588_a = par2;
    }

    public String func_82586_a()
    {
        return NBTBase.field_82578_b[this.field_82588_a];
    }

    public Object call()
    {
        return this.func_82586_a();
    }
}
