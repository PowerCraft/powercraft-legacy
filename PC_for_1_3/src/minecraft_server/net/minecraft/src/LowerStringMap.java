package net.minecraft.src;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class LowerStringMap implements Map
{
    private final Map field_76117_a = new LinkedHashMap();

    public int size()
    {
        return this.field_76117_a.size();
    }

    public boolean isEmpty()
    {
        return this.field_76117_a.isEmpty();
    }

    public boolean containsKey(Object par1Obj)
    {
        return this.field_76117_a.containsKey(par1Obj.toString().toLowerCase());
    }

    public boolean containsValue(Object par1Obj)
    {
        return this.field_76117_a.containsKey(par1Obj);
    }

    public Object get(Object par1Obj)
    {
        return this.field_76117_a.get(par1Obj.toString().toLowerCase());
    }

    public Object func_76116_a(String par1Str, Object par2Obj)
    {
        return this.field_76117_a.put(par1Str.toLowerCase(), par2Obj);
    }

    public Object remove(Object par1Obj)
    {
        return this.field_76117_a.remove(par1Obj.toString().toLowerCase());
    }

    public void putAll(Map par1Map)
    {
        Iterator var2 = par1Map.entrySet().iterator();

        while (var2.hasNext())
        {
            Entry var3 = (Entry)var2.next();
            this.func_76116_a((String)var3.getKey(), var3.getValue());
        }
    }

    public void clear()
    {
        this.field_76117_a.clear();
    }

    public Set keySet()
    {
        return this.field_76117_a.keySet();
    }

    public Collection values()
    {
        return this.field_76117_a.values();
    }

    public Set entrySet()
    {
        return this.field_76117_a.entrySet();
    }

    public Object put(Object par1Obj, Object par2Obj)
    {
        return this.func_76116_a((String)par1Obj, par2Obj);
    }
}
