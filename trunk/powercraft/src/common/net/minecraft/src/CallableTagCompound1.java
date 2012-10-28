package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableTagCompound1 implements Callable
{
    final String field_82585_a;

    final NBTTagCompound field_82584_b;

    CallableTagCompound1(NBTTagCompound par1NBTTagCompound, String par2Str)
    {
        this.field_82584_b = par1NBTTagCompound;
        this.field_82585_a = par2Str;
    }

    public String func_82583_a()
    {
        return NBTBase.field_82578_b[((NBTBase)NBTTagCompound.func_82579_a(this.field_82584_b).get(this.field_82585_a)).getId()];
    }

    public Object call()
    {
        return this.func_82583_a();
    }
}
