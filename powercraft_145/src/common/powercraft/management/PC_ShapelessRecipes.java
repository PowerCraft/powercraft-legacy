package powercraft.management;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class PC_ShapelessRecipes implements IRecipe, PC_IRecipeInputInfo {

	private final PC_ItemStack recipeOutput;
    private final List<PC_ItemStack> recipeItems;

    public PC_ShapelessRecipes(PC_ItemStack itemStack, Object...recipe)
    {
        recipeItems = new ArrayList<PC_ItemStack>();
        int var5 = recipe.length;

        recipeOutput = itemStack;
        
        for (Object o:recipe){

            if (o instanceof PC_ItemStack)
            {
            	recipeItems.add(((PC_ItemStack)o).copy());
            }
            else if (o instanceof Item)
            {
            	recipeItems.add(new PC_ItemStack(o));
            }
            else
            {
                if (!(o instanceof Block))
                {
                    throw new RuntimeException("Invalid shapeless recipy!");
                }

                recipeItems.add(new PC_ItemStack(o));
            }
        }
    }

    public ItemStack getRecipeOutput()
    {
        return recipeOutput.toItemStack();
    }

    public boolean matches(InventoryCrafting par1InventoryCrafting, World par2World)
    {
        ArrayList items = new ArrayList(recipeItems);

        for (int j = 0; j < 3; j++)
        {
            for (int i = 0; i < 3; i++)
            {
                ItemStack is = par1InventoryCrafting.getStackInRowAndColumn(i, j);

                if (is != null)
                {
                    boolean eq = false;
                    Iterator it = items.iterator();

                    while (it.hasNext())
                    {
                        PC_ItemStack var9 = (PC_ItemStack)it.next();

                        if (var9.equals(is))
                        {
                        	eq = true;
                        	items.remove(var9);
                            break;
                        }
                    }

                    if (!eq)
                    {
                        return false;
                    }
                }
            }
        }

        return items.isEmpty();
    }

    public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting)
    {
    	ItemStack itemStack = getRecipeOutput().copy();
    	if(itemStack.getItem() instanceof PC_Item){
    		((PC_Item)itemStack.getItem()).doCrafting(itemStack, par1InventoryCrafting);
    	}
        return itemStack;
    }

    public int getRecipeSize()
    {
        return this.recipeItems.size();
    }

    @Override
    public List<ItemStack> getExpectedInput(List<ItemStack> itemStacks)
    {
        for (int i = 0; i < recipeItems.size(); i++)
        {
            if (recipeItems.get(i) != null)
            {
                itemStacks.add(recipeItems.get(i).toItemStack());
            }
        }

        return itemStacks;
    }

}
