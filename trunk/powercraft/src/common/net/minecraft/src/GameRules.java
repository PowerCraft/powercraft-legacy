package net.minecraft.src;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

public class GameRules
{
    private TreeMap field_82771_a = new TreeMap();

    public GameRules()
    {
        this.func_82769_a("doFireTick", "true");
        this.func_82769_a("mobGriefing", "true");
        this.func_82769_a("keepInventory", "false");
        this.func_82769_a("doMobSpawning", "true");
        this.func_82769_a("doMobLoot", "true");
        this.func_82769_a("doTileDrops", "true");
        this.func_82769_a("commandBlockOutput", "true");
    }

    public void func_82769_a(String par1Str, String par2Str)
    {
        this.field_82771_a.put(par1Str, new GameRuleValue(par2Str));
    }

    public void func_82764_b(String par1Str, String par2Str)
    {
        GameRuleValue var3 = (GameRuleValue)this.field_82771_a.get(par1Str);

        if (var3 != null)
        {
            var3.func_82757_a(par2Str);
        }
        else
        {
            this.func_82769_a(par1Str, par2Str);
        }
    }

    public String func_82767_a(String par1Str)
    {
        GameRuleValue var2 = (GameRuleValue)this.field_82771_a.get(par1Str);
        return var2 != null ? var2.func_82756_a() : "";
    }

    public boolean func_82766_b(String par1Str)
    {
        GameRuleValue var2 = (GameRuleValue)this.field_82771_a.get(par1Str);
        return var2 != null ? var2.func_82758_b() : false;
    }

    public NBTTagCompound func_82770_a()
    {
        NBTTagCompound var1 = new NBTTagCompound("GameRules");
        Iterator var2 = this.field_82771_a.keySet().iterator();

        while (var2.hasNext())
        {
            String var3 = (String)var2.next();
            GameRuleValue var4 = (GameRuleValue)this.field_82771_a.get(var3);
            var1.setString(var3, var4.func_82756_a());
        }

        return var1;
    }

    public void func_82768_a(NBTTagCompound par1NBTTagCompound)
    {
        Collection var2 = par1NBTTagCompound.getTags();
        Iterator var3 = var2.iterator();

        while (var3.hasNext())
        {
            NBTBase var4 = (NBTBase)var3.next();
            String var5 = var4.getName();
            String var6 = par1NBTTagCompound.getString(var4.getName());
            this.func_82764_b(var5, var6);
        }
    }

    public String[] func_82763_b()
    {
        return (String[])this.field_82771_a.keySet().toArray(new String[0]);
    }

    public boolean func_82765_e(String par1Str)
    {
        return this.field_82771_a.containsKey(par1Str);
    }
}
