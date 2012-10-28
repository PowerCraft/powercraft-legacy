package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class RegistrySimple implements IRegistry
{
    protected final Map field_82596_a = new HashMap();

    public Object func_82594_a(Object par1Obj)
    {
        return this.field_82596_a.get(par1Obj);
    }

    public void func_82595_a(Object par1Obj, Object par2Obj)
    {
        this.field_82596_a.put(par1Obj, par2Obj);
    }
}
