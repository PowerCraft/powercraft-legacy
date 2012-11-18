package powercraft.core;

import java.util.List;

import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class PC_ShapedRecipes implements IRecipe, PC_ICraftingInputGetter
{
    private int recipeWidth;

    private int recipeHeight;

    private PC_ItemStack[] recipeItems;

    private PC_ItemStack recipeOutput;

    public PC_ShapedRecipes(int par1, int par2, PC_ItemStack[] par3ArrayOfItemStack, PC_ItemStack par4ItemStack)
    {
        this.recipeWidth = par1;
        this.recipeHeight = par2;
        this.recipeItems = par3ArrayOfItemStack;
        this.recipeOutput = par4ItemStack;
    }

    public ItemStack getRecipeOutput()
    {
        return recipeOutput.toItemStack();
    }

    public boolean matches(InventoryCrafting par1InventoryCrafting, World par2World)
    {
        for (int var3 = 0; var3 <= 3 - this.recipeWidth; ++var3)
        {
            for (int var4 = 0; var4 <= 3 - this.recipeHeight; ++var4)
            {
                if (this.checkMatch(par1InventoryCrafting, var3, var4, true))
                {
                    return true;
                }

                if (this.checkMatch(par1InventoryCrafting, var3, var4, false))
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkMatch(InventoryCrafting par1InventoryCrafting, int par2, int par3, boolean par4)
    {
        for (int var5 = 0; var5 < 3; ++var5)
        {
            for (int var6 = 0; var6 < 3; ++var6)
            {
                int var7 = var5 - par2;
                int var8 = var6 - par3;
                PC_ItemStack var9 = null;

                if (var7 >= 0 && var8 >= 0 && var7 < this.recipeWidth && var8 < this.recipeHeight)
                {
                    if (par4)
                    {
                        var9 = this.recipeItems[this.recipeWidth - var7 - 1 + var8 * this.recipeWidth];
                    }
                    else
                    {
                        var9 = this.recipeItems[var7 + var8 * this.recipeWidth];
                    }
                }

                ItemStack var10 = par1InventoryCrafting.getStackInRowAndColumn(var5, var6);

                if (var10 != null || var9 != null)
                {
                    if (var10 == null && var9 != null || var10 != null && var9 == null)
                    {
                        return false;
                    }

                    if (!var9.equals(var10))
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting)
    {
        return this.getRecipeOutput().copy();
    }

    public int getRecipeSize()
    {
        return this.recipeWidth * this.recipeHeight;
    }

    @Override
    public List<ItemStack> getExpectedInput(List<ItemStack> itemStacks)
    {
        for (PC_ItemStack itemStack: recipeItems)
        {
            if (itemStack != null)
            {
                itemStacks.add(itemStack.toItemStack());
            }
        }

        return itemStacks;
    }
}
