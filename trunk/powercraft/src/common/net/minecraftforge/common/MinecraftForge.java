package net.minecraftforge.common;

import java.lang.reflect.Constructor;
import java.util.*;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModContainer;

import net.minecraft.src.*;
import net.minecraftforge.common.ForgeHooks.GrassEntry;
import net.minecraftforge.common.ForgeHooks.SeedEntry;
import net.minecraftforge.event.EventBus;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.oredict.OreDictionary;

public class MinecraftForge
{
    public static final EventBus EVENT_BUS = new EventBus();
    public static boolean SPAWNER_ALLOW_ON_INVERTED = false;
    private static final ForgeInternalHandler INTERNAL_HANDLER = new ForgeInternalHandler();

    public static void addGrassPlant(Block block, int metadata, int weight)
    {
        ForgeHooks.grassList.add(new GrassEntry(block, metadata, weight));
    }

    public static void addGrassSeed(ItemStack seed, int weight)
    {
        ForgeHooks.seedList.add(new SeedEntry(seed, weight));
    }

    public static void setToolClass(Item tool, String toolClass, int harvestLevel)
    {
        ForgeHooks.toolClasses.put(tool, Arrays.asList(toolClass, harvestLevel));
    }

    public static void setBlockHarvestLevel(Block block, int metadata, String toolClass, int harvestLevel)
    {
        List key = Arrays.asList(block, metadata, toolClass);
        ForgeHooks.toolHarvestLevels.put(key, harvestLevel);
        ForgeHooks.toolEffectiveness.add(key);
    }

    public static void removeBlockEffectiveness(Block block, int metadata, String toolClass)
    {
        List key = Arrays.asList(block, metadata, toolClass);
        ForgeHooks.toolEffectiveness.remove(key);
    }

    public static void setBlockHarvestLevel(Block block, String toolClass, int harvestLevel)
    {
        for (int metadata = 0; metadata < 16; metadata++)
        {
            List key = Arrays.asList(block, metadata, toolClass);
            ForgeHooks.toolHarvestLevels.put(key, harvestLevel);
            ForgeHooks.toolEffectiveness.add(key);
        }
    }

    public static int getBlockHarvestLevel(Block block, int metadata, String toolClass)
    {
        ForgeHooks.initTools();
        List key = Arrays.asList(block, metadata, toolClass);
        Integer harvestLevel = (Integer)ForgeHooks.toolHarvestLevels.get(key);

        if (harvestLevel == null)
        {
            return -1;
        }

        return harvestLevel;
    }

    public static void removeBlockEffectiveness(Block block, String toolClass)
    {
        for (int metadata = 0; metadata < 16; metadata++)
        {
            List key = Arrays.asList(block, metadata, toolClass);
            ForgeHooks.toolEffectiveness.remove(key);
        }
    }

    public static void initialize()
    {
        System.out.printf("MinecraftForge v%s Initialized\n", ForgeVersion.getVersion());
        FMLLog.info("MinecraftForge v%s Initialized", ForgeVersion.getVersion());
        Block filler = new Block(0, Material.air);
        Block.blocksList[0] = null;
        Block.opaqueCubeLookup[0] = false;
        Block.lightOpacity[0] = 0;

        for (int x = 256; x < 4096; x++)
        {
            if (Item.itemsList[x] != null)
            {
                Block.blocksList[x] = filler;
            }
        }

        boolean[] temp = new boolean[4096];

        for (int x = 0; x < EntityEnderman.carriableBlocks.length; x++)
        {
            temp[x] = EntityEnderman.carriableBlocks[x];
        }

        EntityEnderman.carriableBlocks = temp;
        EVENT_BUS.register(INTERNAL_HANDLER);
        OreDictionary.getOreName(0);
    }

    public static String getBrandingVersion()
    {
        return "Minecraft Forge " + ForgeVersion.getVersion();
    }
}
