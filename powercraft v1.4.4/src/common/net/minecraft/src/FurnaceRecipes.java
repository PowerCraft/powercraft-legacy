package net.minecraft.src;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FurnaceRecipes
{
    private static final FurnaceRecipes smeltingBase = new FurnaceRecipes();

    private Map smeltingList = new HashMap();
    private Map experienceList = new HashMap();
    private HashMap<List<Integer>, ItemStack> metaSmeltingList = new HashMap<List<Integer>, ItemStack>();
    private HashMap<List<Integer>, Float> metaExperience = new HashMap<List<Integer>, Float>();

    public static final FurnaceRecipes smelting()
    {
        return smeltingBase;
    }

    private FurnaceRecipes()
    {
        this.addSmelting(Block.oreIron.blockID, new ItemStack(Item.ingotIron), 0.7F);
        this.addSmelting(Block.oreGold.blockID, new ItemStack(Item.ingotGold), 1.0F);
        this.addSmelting(Block.oreDiamond.blockID, new ItemStack(Item.diamond), 1.0F);
        this.addSmelting(Block.sand.blockID, new ItemStack(Block.glass), 0.1F);
        this.addSmelting(Item.porkRaw.shiftedIndex, new ItemStack(Item.porkCooked), 0.35F);
        this.addSmelting(Item.beefRaw.shiftedIndex, new ItemStack(Item.beefCooked), 0.35F);
        this.addSmelting(Item.chickenRaw.shiftedIndex, new ItemStack(Item.chickenCooked), 0.35F);
        this.addSmelting(Item.fishRaw.shiftedIndex, new ItemStack(Item.fishCooked), 0.35F);
        this.addSmelting(Block.cobblestone.blockID, new ItemStack(Block.stone), 0.1F);
        this.addSmelting(Item.clay.shiftedIndex, new ItemStack(Item.brick), 0.3F);
        this.addSmelting(Block.cactus.blockID, new ItemStack(Item.dyePowder, 1, 2), 0.2F);
        this.addSmelting(Block.wood.blockID, new ItemStack(Item.coal, 1, 1), 0.15F);
        this.addSmelting(Block.oreEmerald.blockID, new ItemStack(Item.emerald), 1.0F);
        this.addSmelting(Item.potatoe.shiftedIndex, new ItemStack(Item.bakedPotato), 0.35F);
        this.addSmelting(Block.oreCoal.blockID, new ItemStack(Item.coal), 0.1F);
        this.addSmelting(Block.oreRedstone.blockID, new ItemStack(Item.redstone), 0.7F);
        this.addSmelting(Block.oreLapis.blockID, new ItemStack(Item.dyePowder, 1, 4), 0.2F);
    }

    public void addSmelting(int par1, ItemStack par2ItemStack, float par3)
    {
        this.smeltingList.put(Integer.valueOf(par1), par2ItemStack);
        this.experienceList.put(Integer.valueOf(par2ItemStack.itemID), Float.valueOf(par3));
    }

    @Deprecated
    public ItemStack getSmeltingResult(int par1)
    {
        return (ItemStack)this.smeltingList.get(Integer.valueOf(par1));
    }

    public Map getSmeltingList()
    {
        return this.smeltingList;
    }

    @Deprecated
    public float getExperience(int par1)
    {
        return this.experienceList.containsKey(Integer.valueOf(par1)) ? ((Float)this.experienceList.get(Integer.valueOf(par1))).floatValue() : 0.0F;
    }

    @Deprecated
    public void addSmelting(int itemID, int metadata, ItemStack itemstack)
    {
        addSmelting(itemID, metadata, itemstack, 0.0f);
    }

    public void addSmelting(int itemID, int metadata, ItemStack itemstack, float experience)
    {
        metaSmeltingList.put(Arrays.asList(itemID, metadata), itemstack);
        metaExperience.put(Arrays.asList(itemID, metadata), experience);
    }

    public ItemStack getSmeltingResult(ItemStack item)
    {
        if (item == null)
        {
            return null;
        }

        ItemStack ret = (ItemStack)metaSmeltingList.get(Arrays.asList(item.itemID, item.getItemDamage()));

        if (ret != null)
        {
            return ret;
        }

        return (ItemStack)smeltingList.get(Integer.valueOf(item.itemID));
    }

    public float getExperience(ItemStack item)
    {
        if (item == null || item.getItem() == null)
        {
            return 0;
        }

        float ret = item.getItem().getSmeltingExperience(item);

        if (ret < 0 && metaExperience.containsKey(Arrays.asList(item.itemID, item.getItemDamage())))
        {
            ret = metaExperience.get(Arrays.asList(item.itemID, item.getItemDamage()));
        }

        if (ret < 0 && experienceList.containsKey(item.itemID))
        {
            ret = ((Float)experienceList.get(item.itemID)).floatValue();
        }

        return (ret < 0 ? 0 : ret);
    }
}
