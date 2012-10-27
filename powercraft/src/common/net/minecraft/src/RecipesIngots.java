package net.minecraft.src;

public class RecipesIngots
{
    private Object[][] recipeItems;

    public RecipesIngots()
    {
        this.recipeItems = new Object[][] {{Block.blockGold, new ItemStack(Item.ingotGold, 9)}, {Block.blockSteel, new ItemStack(Item.ingotIron, 9)}, {Block.blockDiamond, new ItemStack(Item.diamond, 9)}, {Block.blockEmerald, new ItemStack(Item.emerald, 9)}, {Block.blockLapis, new ItemStack(Item.dyePowder, 9, 4)}};
    }

    /**
     * Adds the ingot recipes to the CraftingManager.
     */
    public void addRecipes(CraftingManager par1CraftingManager)
    {
        Object[][] var2 = this.recipeItems;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            Object[] var5 = var2[var4];
            Block var6 = (Block)var5[0];
            ItemStack var7 = (ItemStack)var5[1];
            par1CraftingManager.addRecipe(new ItemStack(var6), new Object[] {"###", "###", "###", '#', var7});
            par1CraftingManager.addRecipe(var7, new Object[] {"#", '#', var6});
        }

        par1CraftingManager.addRecipe(new ItemStack(Item.ingotGold), new Object[] {"###", "###", "###", '#', Item.goldNugget});
        par1CraftingManager.addRecipe(new ItemStack(Item.goldNugget, 9), new Object[] {"#", '#', Item.ingotGold});
    }
}
