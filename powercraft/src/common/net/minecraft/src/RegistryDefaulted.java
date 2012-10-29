package net.minecraft.src;

public class RegistryDefaulted extends RegistrySimple
{
    private final Object field_82597_b;

    public RegistryDefaulted(Object par1Obj)
    {
        this.field_82597_b = par1Obj;
    }

    public Object func_82594_a(Object par1Obj)
    {
        Object var2 = super.func_82594_a(par1Obj);
        return var2 == null ? this.field_82597_b : var2;
    }
}
