package powercraft.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class PC_ShapelessRecipes implements IRecipe {

	/** Is the ItemStack that you get when craft the recipe. */
    private final PC_ItemStack recipeOutput;

    /** Is a List of ItemStack that composes the recipe. */
    private final List<PC_ItemStack> recipeItems;

    public PC_ShapelessRecipes(PC_ItemStack par1ItemStack, List<PC_ItemStack> par2List)
    {
        this.recipeOutput = par1ItemStack;
        this.recipeItems = par2List;
    }

    public ItemStack getRecipeOutput()
    {
        return recipeOutput.toItemStack();
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting par1InventoryCrafting, World par2World)
    {
        ArrayList var3 = new ArrayList(this.recipeItems);

        for (int var4 = 0; var4 < 3; ++var4)
        {
            for (int var5 = 0; var5 < 3; ++var5)
            {
            	ItemStack var6 = par1InventoryCrafting.getStackInRowAndColumn(var5, var4);

                if (var6 != null)
                {
                    boolean var7 = false;
                    Iterator var8 = var3.iterator();

                    while (var8.hasNext())
                    {
                    	PC_ItemStack var9 = (PC_ItemStack)var8.next();

                        if (var9.equals(var6))
                        {
                            var7 = true;
                            var3.remove(var9);
                            break;
                        }
                    }

                    if (!var7)
                    {
                        return false;
                    }
                }
            }
        }

        return var3.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting)
    {
        return recipeOutput.toItemStack();
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize()
    {
        return this.recipeItems.size();
    }

}
