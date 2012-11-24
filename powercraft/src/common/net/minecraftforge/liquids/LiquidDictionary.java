package net.minecraftforge.liquids;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.src.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

import com.google.common.collect.ImmutableMap;

public abstract class LiquidDictionary
{
    private static Map<String, LiquidStack> liquids = new HashMap<String, LiquidStack>();

    public static LiquidStack getOrCreateLiquid(String name, LiquidStack liquid)
    {
        LiquidStack existing = liquids.get(name);

        if (existing != null)
        {
            return existing.copy();
        }

        liquids.put(name, liquid.copy());
        MinecraftForge.EVENT_BUS.post(new LiquidRegisterEvent(name, liquid));
        return liquid;
    }

    public static LiquidStack getLiquid(String name, int amount)
    {
        LiquidStack liquid = liquids.get(name);

        if (liquid == null)
        {
            return null;
        }

        liquid = liquid.copy();
        liquid.amount = amount;
        return liquid;
    }

    public Map<String, LiquidStack> getDefinedLiquids()
    {
        return ImmutableMap.copyOf(liquids);
    }

    public static class LiquidRegisterEvent extends Event
    {
        public final String Name;
        public final LiquidStack Liquid;

        public LiquidRegisterEvent(String name, LiquidStack liquid)
        {
            this.Name = name;
            this.Liquid = liquid.copy();
        }
    }
}
