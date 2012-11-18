package net.minecraftforge.common;

import java.util.*;

import net.minecraft.src.*;

public class ChestGenHooks
{
    public static final String MINESHAFT_CORRIDOR       = "mineshaftCorridor";
    public static final String PYRAMID_DESERT_CHEST     = "pyramidDesertyChest";
    public static final String PYRAMID_JUNGLE_CHEST     = "pyramidJungleChest";
    public static final String PYRAMID_JUNGLE_DISPENSER = "pyramidJungleDispenser";
    public static final String STRONGHOLD_CORRIDOR      = "strongholdCorridor";
    public static final String STRONGHOLD_LIBRARY       = "strongholdLibrary";
    public static final String STRONGHOLD_CROSSING      = "strongholdCrossing";
    public static final String VILLAGE_BLACKSMITH       = "villageBlacksmith";
    public static final String BONUS_CHEST              = "bonusChest";

    private static final HashMap<String, ChestGenHooks> chestInfo = new HashMap<String, ChestGenHooks>();
    private static boolean hasInit = false;
    static
    {
        init();
    }

    private static void init()
    {
        if (hasInit)
        {
            return;
        }

        addInfo(MINESHAFT_CORRIDOR,       StructureMineshaftPieces.mineshaftChestContents,                         3,  7);
        addInfo(PYRAMID_DESERT_CHEST,     ComponentScatteredFeatureDesertPyramid.itemsToGenerateInTemple,          2,  7);
        addInfo(PYRAMID_JUNGLE_CHEST,     ComponentScatteredFeatureJunglePyramid.junglePyramidsChestContents,      2,  7);
        addInfo(PYRAMID_JUNGLE_DISPENSER, ComponentScatteredFeatureJunglePyramid.junglePyramidsDispenserContents,  2,  2);
        addInfo(STRONGHOLD_CORRIDOR,      ComponentStrongholdChestCorridor.strongholdChestContents,                2,  4);
        addInfo(STRONGHOLD_LIBRARY,       ComponentStrongholdLibrary.strongholdLibraryChestContents,               1,  5);
        addInfo(STRONGHOLD_CROSSING,      ComponentStrongholdRoomCrossing.strongholdRoomCrossingChestContents,                           1,  5);
        addInfo(VILLAGE_BLACKSMITH,       ComponentVillageHouse2.villageBlacksmithChestContents,                   3,  9);
        addInfo(BONUS_CHEST,              WorldServer.bonusChestContent,                                          10, 10);
    }

    private static void addInfo(String category, WeightedRandomChestContent[] items, int min, int max)
    {
        chestInfo.put(category, new ChestGenHooks(category, items, min, max));
    }

    public static ChestGenHooks getInfo(String category)
    {
        if (!chestInfo.containsKey(category))
        {
            chestInfo.put(category, new ChestGenHooks(category));
        }

        return chestInfo.get(category);
    }

    public static ItemStack[] generateStacks(Random rand, ItemStack source, int min, int max)
    {
        int count = min + (rand.nextInt(max - min + 1));
        ItemStack[] ret;

        if (source.getItem() == null)
        {
            ret = new ItemStack[0];
        }
        else if (count > source.getItem().getItemStackLimit())
        {
            ret = new ItemStack[count];

            for (int x = 0; x < count; x++)
            {
                ret[x] = source.copy();
                ret[x].stackSize = 1;
            }
        }
        else
        {
            ret = new ItemStack[1];
            ret[0] = source.copy();
            ret[0].stackSize = count;
        }

        return ret;
    }

    public static WeightedRandomChestContent[] getItems(String category)
    {
        return getInfo(category).getItems();
    }
    public static int getCount(String category, Random rand)
    {
        return getInfo(category).getCount(rand);
    }
    public static void addItem(String category, WeightedRandomChestContent item)
    {
        getInfo(category).addItem(item);
    }
    public static void removeItem(String category, ItemStack item)
    {
        getInfo(category).removeItem(item);
    }

    private String category;
    private int countMin = 0;
    private int countMax = 0;
    private ArrayList<WeightedRandomChestContent> contents = new ArrayList<WeightedRandomChestContent>();

    public ChestGenHooks(String category)
    {
        this.category = category;
    }

    public ChestGenHooks(String category, WeightedRandomChestContent[] items, int min, int max)
    {
        this(category);

        for (WeightedRandomChestContent item : items)
        {
            contents.add(item);
        }

        countMin = min;
        countMax = max;
    }

    public void addItem(WeightedRandomChestContent item)
    {
        contents.add(item);
    }

    public void removeItem(ItemStack item)
    {
        Iterator<WeightedRandomChestContent> itr = contents.iterator();

        while (itr.hasNext())
        {
            WeightedRandomChestContent cont = itr.next();

            if (item.isItemEqual(cont.itemStack) || (item.getItemDamage() == -1 && item.itemID == cont.itemStack.itemID))
            {
                itr.remove();
            }
        }
    }

    public WeightedRandomChestContent[] getItems()
    {
        return contents.toArray(new WeightedRandomChestContent[contents.size()]);
    }

    public int getCount(Random rand)
    {
        return countMin < countMax ? countMin + rand.nextInt(countMax - countMin) : countMin;
    }

    public int getMin()
    {
        return countMin;
    }
    public int getMax()
    {
        return countMax;
    }
    public void setMin(int value)
    {
        countMin = value;
    }
    public void setMax(int value)
    {
        countMax = value;
    }
}
