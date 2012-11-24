package net.minecraft.src;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class WeightedRandom
{
    public static int getTotalWeight(Collection par0Collection)
    {
        int var1 = 0;
        WeightedRandomItem var3;

        for (Iterator var2 = par0Collection.iterator(); var2.hasNext(); var1 += var3.itemWeight)
        {
            var3 = (WeightedRandomItem)var2.next();
        }

        return var1;
    }

    public static WeightedRandomItem getRandomItem(Random par0Random, Collection par1Collection, int par2)
    {
        if (par2 <= 0)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            int var3 = par0Random.nextInt(par2);
            Iterator var4 = par1Collection.iterator();
            WeightedRandomItem var5;

            do
            {
                if (!var4.hasNext())
                {
                    return null;
                }

                var5 = (WeightedRandomItem)var4.next();
                var3 -= var5.itemWeight;
            }
            while (var3 >= 0);

            return var5;
        }
    }

    public static WeightedRandomItem getRandomItem(Random par0Random, Collection par1Collection)
    {
        return getRandomItem(par0Random, par1Collection, getTotalWeight(par1Collection));
    }

    public static int getTotalWeight(WeightedRandomItem[] par0ArrayOfWeightedRandomItem)
    {
        int var1 = 0;
        WeightedRandomItem[] var2 = par0ArrayOfWeightedRandomItem;
        int var3 = par0ArrayOfWeightedRandomItem.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            WeightedRandomItem var5 = var2[var4];
            var1 += var5.itemWeight;
        }

        return var1;
    }

    public static WeightedRandomItem getRandomItem(Random par0Random, WeightedRandomItem[] par1ArrayOfWeightedRandomItem, int par2)
    {
        if (par2 <= 0)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            int var3 = par0Random.nextInt(par2);
            WeightedRandomItem[] var4 = par1ArrayOfWeightedRandomItem;
            int var5 = par1ArrayOfWeightedRandomItem.length;

            for (int var6 = 0; var6 < var5; ++var6)
            {
                WeightedRandomItem var7 = var4[var6];
                var3 -= var7.itemWeight;

                if (var3 < 0)
                {
                    return var7;
                }
            }

            return null;
        }
    }

    public static WeightedRandomItem getRandomItem(Random par0Random, WeightedRandomItem[] par1ArrayOfWeightedRandomItem)
    {
        return getRandomItem(par0Random, par1ArrayOfWeightedRandomItem, getTotalWeight(par1ArrayOfWeightedRandomItem));
    }
}
