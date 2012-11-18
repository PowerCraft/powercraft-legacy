package net.minecraftforge.common;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.WeightedRandom;
import net.minecraft.src.WeightedRandomItem;

public class DungeonHooks
{
    private static int dungeonLootAttempts = 8;
    private static ArrayList<DungeonMob> dungeonMobs = new ArrayList<DungeonMob>();
    private static ArrayList<DungeonLoot> dungeonLoot = new ArrayList<DungeonLoot>();

    public static void setDungeonLootTries(int number)
    {
        dungeonLootAttempts = number;
    }

    public static int getDungeonLootTries()
    {
        return dungeonLootAttempts;
    }

    public static float addDungeonMob(String name, int rarity)
    {
        if (rarity <= 0)
        {
            throw new IllegalArgumentException("Rarity must be greater then zero");
        }

        for (DungeonMob mob : dungeonMobs)
        {
            if (name.equals(mob.type))
            {
                return mob.itemWeight += rarity;
            }
        }

        dungeonMobs.add(new DungeonMob(rarity, name));
        return rarity;
    }

    public static int removeDungeonMob(String name)
    {
        for (DungeonMob mob : dungeonMobs)
        {
            if (name.equals(mob.type))
            {
                dungeonMobs.remove(mob);
                return mob.itemWeight;
            }
        }

        return 0;
    }

    public static String getRandomDungeonMob(Random rand)
    {
        DungeonMob mob = (DungeonMob)WeightedRandom.getRandomItem(rand, dungeonMobs);

        if (mob == null)
        {
            return "";
        }

        return mob.type;
    }

    public static void addDungeonLoot(ItemStack item, int rarity)
    {
        addDungeonLoot(item, rarity, 1, 1);
    }

    public static float addDungeonLoot(ItemStack item, int rarity, int minCount, int maxCount)
    {
        for (DungeonLoot loot : dungeonLoot)
        {
            if (loot.equals(item, minCount, maxCount))
            {
                return loot.itemWeight += rarity;
            }
        }

        dungeonLoot.add(new DungeonLoot(rarity, item, minCount, maxCount));
        return rarity;
    }

    public static void removeDungeonLoot(ItemStack item)
    {
        removeDungeonLoot(item, -1, 0);
    }

    public static void removeDungeonLoot(ItemStack item, int minCount, int maxCount)
    {
        ArrayList<DungeonLoot> lootTmp = (ArrayList<DungeonLoot>)dungeonLoot.clone();

        if (minCount < 0)
        {
            for (DungeonLoot loot : lootTmp)
            {
                if (loot.equals(item))
                {
                    dungeonLoot.remove(loot);
                }
            }
        }
        else
        {
            for (DungeonLoot loot : lootTmp)
            {
                if (loot.equals(item, minCount, maxCount))
                {
                    dungeonLoot.remove(loot);
                }
            }
        }
    }

    public static ItemStack getRandomDungeonLoot(Random rand)
    {
        DungeonLoot ret = (DungeonLoot)WeightedRandom.getRandomItem(rand, dungeonLoot);

        if (ret != null)
        {
            return ret.generateStack(rand);
        }

        return null;
    }

    public static class DungeonLoot extends WeightedRandomItem
    {
        private ItemStack itemStack;
        private int minCount = 1;
        private int maxCount = 1;

        public DungeonLoot(int weight, ItemStack item, int min, int max)
        {
            super(weight);
            this.itemStack = item;
            minCount = min;
            maxCount = max;
        }

        public ItemStack generateStack(Random rand)
        {
            ItemStack ret = this.itemStack.copy();
            ret.stackSize = minCount + (rand.nextInt(maxCount - minCount + 1));
            return ret;
        }

        public boolean equals(ItemStack item, int min, int max)
        {
            return (min == minCount && max == maxCount && item.isItemEqual(this.itemStack));
        }

        public boolean equals(ItemStack item)
        {
            return item.isItemEqual(this.itemStack);
        }
    }

    public static class DungeonMob extends WeightedRandomItem
    {
        public String type;
        public DungeonMob(int weight, String type)
        {
            super(weight);
            this.type = type;
        }

        @Override
        public boolean equals(Object target)
        {
            if (target instanceof DungeonMob)
            {
                return this.type.equals(((DungeonMob)target).type);
            }

            return false;
        }
    }

    public void addDungeonLoot(DungeonLoot loot)
    {
        dungeonLoot.add(loot);
    }

    public boolean removeDungeonLoot(DungeonLoot loot)
    {
        return dungeonLoot.remove(loot);
    }

    static
    {
        addDungeonMob("Skeleton", 100);
        addDungeonMob("Zombie",   200);
        addDungeonMob("Spider",   100);
        addDungeonLoot(new ItemStack(Item.saddle),          100);
        addDungeonLoot(new ItemStack(Item.ingotIron),       100, 1, 4);
        addDungeonLoot(new ItemStack(Item.bread),           100);
        addDungeonLoot(new ItemStack(Item.wheat),           100, 1, 4);
        addDungeonLoot(new ItemStack(Item.gunpowder),       100, 1, 4);
        addDungeonLoot(new ItemStack(Item.silk),            100, 1, 4);
        addDungeonLoot(new ItemStack(Item.bucketEmpty),     100);
        addDungeonLoot(new ItemStack(Item.appleGold),       001);
        addDungeonLoot(new ItemStack(Item.redstone),        050, 1, 4);
        addDungeonLoot(new ItemStack(Item.record13),        005);
        addDungeonLoot(new ItemStack(Item.recordCat),       005);
        addDungeonLoot(new ItemStack(Item.dyePowder, 1, 3), 100);
    }
}
