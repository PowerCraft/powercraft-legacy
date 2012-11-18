package net.minecraftforge.oredict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.src.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;

public class OreDictionary
{
    private static boolean hasInit = false;
    private static int maxID = 0;
    private static HashMap<String, Integer> oreIDs = new HashMap<String, Integer>();
    private static HashMap<Integer, ArrayList<ItemStack>> oreStacks = new HashMap<Integer, ArrayList<ItemStack>>();

    static
    {
        initVanillaEntries();
    }

    public static void initVanillaEntries()
    {
        if (!hasInit)
        {
            registerOre("logWood",     new ItemStack(Block.wood, 1, -1));
            registerOre("plankWood",   new ItemStack(Block.planks, 1, -1));
            registerOre("slabWood",    new ItemStack(Block.woodSingleSlab, 1, -1));
            registerOre("stairWood",   Block.stairCompactPlanks);
            registerOre("stairWood",   Block.stairsWoodBirch);
            registerOre("stairWood",   Block.stairsWoodJungle);
            registerOre("stairWood",   Block.stairsWoodSpruce);
            registerOre("stickWood",   Item.stick);
            registerOre("treeSapling", new ItemStack(Block.sapling, 1, -1));
            registerOre("treeLeaves",  new ItemStack(Block.leaves, 1, -1));
        }

        Map<ItemStack, String> replacements = new HashMap<ItemStack, String>();
        replacements.put(new ItemStack(Block.planks, 1, -1), "plankWood");
        replacements.put(new ItemStack(Item.stick), "stickWood");
        String[] dyes =
        {
            "dyeBlack",
            "dyeRed",
            "dyeGreen",
            "dyeBrown",
            "dyeBlue",
            "dyePurple",
            "dyeCyan",
            "dyeLightGray",
            "dyeGray",
            "dyePink",
            "dyeLime",
            "dyeYellow",
            "dyeLightBlue",
            "dyeMagenta",
            "dyeOrange",
            "dyeWhite"
        };

        for (int i = 0; i < 16; i++)
        {
            ItemStack dye = new ItemStack(Item.dyePowder, 1, i);

            if (!hasInit)
            {
                registerOre(dyes[i], dye);
            }

            replacements.put(dye, dyes[i]);
        }

        hasInit = true;
        ItemStack[] replaceStacks = replacements.keySet().toArray(new ItemStack[0]);
        ItemStack[] exclusions = new ItemStack[]
        {
            new ItemStack(Block.blockLapis),
            new ItemStack(Item.cookie),
        };
        List recipes = CraftingManager.getInstance().getRecipeList();
        List<IRecipe> recipesToRemove = new ArrayList<IRecipe>();
        List<IRecipe> recipesToAdd = new ArrayList<IRecipe>();

        for (Object obj : recipes)
        {
            if (obj instanceof ShapedRecipes)
            {
                ShapedRecipes recipe = (ShapedRecipes)obj;
                ItemStack output = recipe.getRecipeOutput();

                if (output != null && containsMatch(false, exclusions, output))
                {
                    continue;
                }

                if (containsMatch(true, recipe.recipeItems, replaceStacks))
                {
                    recipesToRemove.add(recipe);
                    recipesToAdd.add(new ShapedOreRecipe(recipe, replacements));
                }
            }
            else if (obj instanceof ShapelessRecipes)
            {
                ShapelessRecipes recipe = (ShapelessRecipes)obj;
                ItemStack output = recipe.getRecipeOutput();

                if (output != null && containsMatch(false, exclusions, output))
                {
                    continue;
                }

                if (containsMatch(true, (ItemStack[])recipe.recipeItems.toArray(new ItemStack[0]), replaceStacks))
                {
                    recipesToRemove.add((IRecipe)obj);
                    IRecipe newRecipe = new ShapelessOreRecipe(recipe, replacements);
                    recipesToAdd.add(newRecipe);
                }
            }
        }

        recipes.removeAll(recipesToRemove);
        recipes.addAll(recipesToAdd);

        if (recipesToRemove.size() > 0)
        {
            System.out.println("Replaced " + recipesToRemove.size() + " ore recipies");
        }
    }

    public static int getOreID(String name)
    {
        Integer val = oreIDs.get(name);

        if (val == null)
        {
            val = maxID++;
            oreIDs.put(name, val);
            oreStacks.put(val, new ArrayList<ItemStack>());
        }

        return val;
    }

    public static String getOreName(int id)
    {
        for (Map.Entry<String, Integer> entry : oreIDs.entrySet())
        {
            if (id == entry.getValue())
            {
                return entry.getKey();
            }
        }

        return "Unknown";
    }

    public static ArrayList<ItemStack> getOres(String name)
    {
        return getOres(getOreID(name));
    }

    public static String[] getOreNames()
    {
        return oreIDs.keySet().toArray(new String[0]);
    }

    public static ArrayList<ItemStack> getOres(Integer id)
    {
        ArrayList<ItemStack> val = oreStacks.get(id);

        if (val == null)
        {
            val = new ArrayList<ItemStack>();
            oreStacks.put(id, val);
        }

        return val;
    }

    private static boolean containsMatch(boolean strict, ItemStack[] inputs, ItemStack... targets)
    {
        for (ItemStack input : inputs)
        {
            for (ItemStack target : targets)
            {
                if (itemMatches(target, input, strict))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean itemMatches(ItemStack target, ItemStack input, boolean strict)
    {
        if (input == null && target != null || input != null && target == null)
        {
            return false;
        }

        return (target.itemID == input.itemID && ((target.getItemDamage() == -1 && !strict) || target.getItemDamage() == input.getItemDamage()));
    }

    public static void registerOre(String name, Item      ore)
    {
        registerOre(name, new ItemStack(ore));
    }
    public static void registerOre(String name, Block     ore)
    {
        registerOre(name, new ItemStack(ore));
    }
    public static void registerOre(String name, ItemStack ore)
    {
        registerOre(name, getOreID(name), ore);
    }
    public static void registerOre(int    id,   Item      ore)
    {
        registerOre(id,   new ItemStack(ore));
    }
    public static void registerOre(int    id,   Block     ore)
    {
        registerOre(id,   new ItemStack(ore));
    }
    public static void registerOre(int    id,   ItemStack ore)
    {
        registerOre(getOreName(id), id, ore);
    }

    private static void registerOre(String name, int id, ItemStack ore)
    {
        ArrayList<ItemStack> ores = getOres(id);
        ore = ore.copy();
        ores.add(ore);
        MinecraftForge.EVENT_BUS.post(new OreRegisterEvent(name, ore));
    }

    public static class OreRegisterEvent extends Event
    {
        public final String Name;
        public final ItemStack Ore;

        public OreRegisterEvent(String name, ItemStack ore)
        {
            this.Name = name;
            this.Ore = ore;
        }
    }
}
